package com.yakin.oktp.internal;

import com.yakin.oktp.callback.CompletedCallback;
import com.yakin.oktp.callback.ProgressCallback;
import com.yakin.oktp.callback.RetryCallback;
import com.yakin.oktp.model.ObjectRequest;

import okhttp3.OkHttpClient;

public class ExecutionContext {

    private ObjectRequest request;
    private OkHttpClient client;
    private CancellationHandler cancellationHandler = new CancellationHandler();

    private CompletedCallback completedCallback;
    private ProgressCallback progressCallback;
    private RetryCallback retryCallback;

    public ExecutionContext(OkHttpClient client, ObjectRequest request) {
        setClient(client);
        setRequest(request);
    }

    public ObjectRequest getRequest() {
        return request;
    }

    public void setRequest(ObjectRequest request) {
        this.request = request;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public CancellationHandler getCancellationHandler() {
        return cancellationHandler;
    }

    public void setCompletedCallback(CompletedCallback completedCallback) {
        this.completedCallback = completedCallback;
    }

    public CompletedCallback getCompletedCallback() {
        return completedCallback;
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
}
