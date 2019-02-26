package com.yakin.oktp.internal;

import android.os.Handler;
import android.os.Looper;

import com.yakin.oktp.ClientException;
import com.yakin.oktp.ResponseParser;
import com.yakin.oktp.model.ObjectRequest;
import com.yakin.oktp.model.ObjectResult;
import com.yakin.oktp.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestTask implements Callable {

    private ResponseParser<ObjectResult> responseParser;
    private ObjectRequest requestMessage;
    private ExecutionContext context;
    private OkHttpClient client;
    private RetryHandler retryHandler;
    private int currentRetryCount;

    private Handler handler = new Handler(Looper.getMainLooper());

    public RequestTask(ObjectRequest request, ExecutionContext context, ResponseParser<ObjectResult> parser, int maxRetry) {
        this.requestMessage = request;
        this.responseParser = parser;
        this.context = context;
        this.client = context.getClient();
        this.retryHandler = new RetryHandler(maxRetry);
    }

    @Override
    public ObjectResult call() throws Exception {
        ObjectResult responseResult = null;
        ClientException exception = null;
        Call call = null;

        try {
            if (context.getCancellationHandler().isCancelled()) {
                throw new InterruptedIOException("This task is cancelled!");
            }

            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder = requestBuilder.url(requestMessage.getService().toString());
            for (String key : requestMessage.getHeaders().keySet()) {
                requestBuilder = requestBuilder.addHeader(key, requestMessage.getHeaders().get(key));
            }

            // set request body
            switch (requestMessage.getMethod()) {
                case POST:
                case PUT:
                    InputStream inputStream = requestMessage.getContent();
                    if (inputStream != null) {
                        long length = requestMessage.getContentLength();
                        String contentType = requestMessage.getHeader("Content-Type");
                        requestBuilder = requestBuilder.method(requestMessage.getMethod().toString(),
                                new ProgressTouchableRequestBody(inputStream, length, contentType, context));
                    } else {
                        requestBuilder = requestBuilder.method(requestMessage.getMethod().toString()
                                , RequestBody.create(null, new byte[0]));
                    }
                    break;
                case GET:
                    requestBuilder = requestBuilder.get();
                    break;
                case HEAD:
                    requestBuilder = requestBuilder.head();
                    break;
                case DELETE:
                    requestBuilder = requestBuilder.delete();
                    break;
                default:
                    break;
            }

            Request request = requestBuilder.build();
            call = client.newCall(request);
            context.getCancellationHandler().setCall(call);
            Response response = call.execute();

            responseResult = buildResponseResult(requestMessage, response);
        } catch (Exception e) {
            LogUtil.e(e, "[ErrorMessage]: " + e.getLocalizedMessage());
            exception = new ClientException(e.getMessage(), e);
        }

        if (exception == null) {
            try {
                if(responseParser != null) {
                    responseResult = responseParser.parse(responseResult);
                }

                final ObjectResult result = responseResult;
                getUIThreadHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (context.getCompletedCallback() != null) {
                            context.getCompletedCallback().onSuccess(context.getRequest(), result);
                        }
                    }
                });
                return result;
            } catch (IOException e) {
                exception = new ClientException(e.getMessage(), e);
            }
        }

        // reconstruct exception caused by manually cancelling
        if ((call != null && call.isCanceled())
                || context.getCancellationHandler().isCancelled()) {
            exception = new ClientException("Task is cancelled!", exception.getCause(), true);
        }

        RetryHandler.RetryType retryType = retryHandler.shouldRetry(exception, responseResult, currentRetryCount);
        LogUtil.d("[run] - retry, retry type: " + retryType);
        if (retryType == RetryHandler.RetryType.ShouldRetry) {
            currentRetryCount ++;
            getUIThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (context.getRetryCallback() != null) {
                        context.getRetryCallback().onRetryCallback();
                    }
                }
            });

            try {
                Thread.sleep(retryHandler.timeInterval(currentRetryCount, retryType));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LogUtil.e(e, "[ErrorMessage]: " + e.getLocalizedMessage());
            }

            return call();
        } else {
            final ClientException resultException = exception;
            getUIThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (context.getCompletedCallback() != null) {
                        context.getCompletedCallback().onFailure(context.getRequest(), resultException);
                    }
                }
            });
            throw exception;
        }
    }

    private ObjectResult buildResponseResult(ObjectRequest request, Response response) {
        ObjectResult responseResult = new ObjectResult();
        responseResult.setRequest(request);
        responseResult.setResponse(response);
        responseResult.setResponseCode(response.code());
        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            responseResult.addHeader(responseHeaders.name(i), responseHeaders.value(i));
        }
        return responseResult;
    }

    private Handler getUIThreadHandler() {
        return handler;
    }
}
