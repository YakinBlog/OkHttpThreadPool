package com.yakin.oktp;

import com.yakin.oktp.callback.CompletedCallback;
import com.yakin.oktp.model.ObjectRequest;
import com.yakin.oktp.model.ObjectResult;
import com.yakin.oktp.internal.AsyncRequestTask;
import com.yakin.oktp.internal.ExecutionContext;
import com.yakin.oktp.internal.RequestTask;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

import static com.yakin.oktp.Oktp.Config;
import static com.yakin.oktp.model.ObjectRequest.HttpMethod;

class RequestOperation implements IRequester {

    private ExecutorService executorService;
    private OkHttpClient innerClient;

    public RequestOperation(String endpoint) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .retryOnConnectionFailure(false)
                .cache(null);

        if(endpoint != null) {
            final URI endpointURI = URI.create(endpoint);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return HttpsURLConnection.getDefaultHostnameVerifier().verify(endpointURI.getHost(), session);
                }
            });
        }

        builder.connectTimeout(Config.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(Config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(Config.getWriteTimeout(), TimeUnit.MILLISECONDS);

        if (Config.getProxyHost() != null && Config.getProxyPort() != 0) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Config.getProxyHost(), Config.getProxyPort())));
        }

        innerClient = builder.build();

        executorService = Executors.newFixedThreadPool(Config.getThreadPoolSize(), new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "oktp-thread");
                    }
                });
    }

    @Override
    public ObjectResult get(ObjectRequest request) throws ClientException {
        return get(request, null).getResult();
    }

    @Override
    public AsyncRequestTask get(ObjectRequest request, CompletedCallback completedCallback) {
        request.setMethod(HttpMethod.GET);

        ExecutionContext executionContext = new ExecutionContext(innerClient, request);
        executionContext.setCompletedCallback(completedCallback);

        ResponseParser<ObjectResult> parser = request.getResponseParser();
        Callable<ObjectResult> callable = new RequestTask(request, executionContext, parser, Config.getMaxErrorRetry());
        return AsyncRequestTask.wrapRequestTask(executorService.submit(callable), executionContext);
    }

    @Override
    public ObjectResult delete(ObjectRequest request) throws ClientException {
        return delete(request, null).getResult();
    }

    @Override
    public AsyncRequestTask delete(ObjectRequest request, CompletedCallback completedCallback) {
        request.setMethod(HttpMethod.DELETE);

        ExecutionContext executionContext = new ExecutionContext(innerClient, request);
        executionContext.setCompletedCallback(completedCallback);

        ResponseParser<ObjectResult> parser = request.getResponseParser();
        Callable<ObjectResult> callable = new RequestTask(request, executionContext, parser, Config.getMaxErrorRetry());
        return AsyncRequestTask.wrapRequestTask(executorService.submit(callable), executionContext);
    }

    @Override
    public ObjectResult head(ObjectRequest request) throws ClientException {
        return head(request, null).getResult();
    }

    @Override
    public AsyncRequestTask head(ObjectRequest request, CompletedCallback completedCallback) {
        request.setMethod(HttpMethod.HEAD);

        ExecutionContext executionContext = new ExecutionContext(innerClient, request);
        executionContext.setCompletedCallback(completedCallback);

        ResponseParser<ObjectResult> parser = request.getResponseParser();
        Callable<ObjectResult> callable = new RequestTask(request, executionContext, parser, Config.getMaxErrorRetry());
        return AsyncRequestTask.wrapRequestTask(executorService.submit(callable), executionContext);
    }

    @Override
    public ObjectResult put(ObjectRequest request) throws ClientException {
        return put(request, null).getResult();
    }

    @Override
    public AsyncRequestTask put(ObjectRequest request, CompletedCallback completedCallback) {
        request.setMethod(HttpMethod.PUT);

        ExecutionContext executionContext = new ExecutionContext(innerClient, request);
        executionContext.setCompletedCallback(completedCallback);
        executionContext.setProgressCallback(request.getProgressCallback());
        executionContext.setRetryCallback(request.getRetryCallback());

        ResponseParser<ObjectResult> parser = request.getResponseParser();
        Callable<ObjectResult> callable = new RequestTask(request, executionContext, parser, Config.getMaxErrorRetry());
        return AsyncRequestTask.wrapRequestTask(executorService.submit(callable), executionContext);
    }

    @Override
    public ObjectResult post(ObjectRequest request) throws ClientException {
        return post(request, null).getResult();
    }

    @Override
    public AsyncRequestTask post(ObjectRequest request, CompletedCallback completedCallback) {
        request.setMethod(HttpMethod.POST);

        ExecutionContext executionContext = new ExecutionContext(innerClient, request);
        executionContext.setCompletedCallback(completedCallback);
        executionContext.setProgressCallback(request.getProgressCallback());
        executionContext.setRetryCallback(request.getRetryCallback());

        ResponseParser<ObjectResult> parser = request.getResponseParser();
        Callable<ObjectResult> callable = new RequestTask(request, executionContext, parser, Config.getMaxErrorRetry());
        return AsyncRequestTask.wrapRequestTask(executorService.submit(callable), executionContext);
    }
}
