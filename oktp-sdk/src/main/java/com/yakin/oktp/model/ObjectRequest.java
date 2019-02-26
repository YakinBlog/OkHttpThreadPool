package com.yakin.oktp.model;

import com.yakin.oktp.callback.ProgressCallback;
import com.yakin.oktp.callback.RetryCallback;
import com.yakin.oktp.ResponseParser;

import java.io.InputStream;
import java.net.URI;

public class ObjectRequest<Result extends ObjectResult> extends HttpHeader {

    public enum HttpMethod {
        DELETE, GET, HEAD, POST, PUT
    }

    private URI service;
    private HttpMethod method;

    private ResponseParser<Result> responseParser;
    private ProgressCallback progressCallback;
    private RetryCallback retryCallback;

    public ObjectRequest() {
        addHeader("Content-Type", "application/octet-stream");
    }

    public void setService(URI service) {
        this.service = service;
    }

    public URI getService() {
        return service;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setResponseParser(ResponseParser<Result> responseParser) {
        this.responseParser = responseParser;
    }

    public ResponseParser<Result> getResponseParser() {
        return responseParser;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    public RetryCallback getRetryCallback() {
        return retryCallback;
    }

    public InputStream getContent() throws Exception {
        return null;
    }

    public long getContentLength() throws Exception {
        return 0;
    }
}
