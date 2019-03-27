package com.yakin.oktp;

import com.yakin.oktp.callback.CompletedCallback;
import com.yakin.oktp.model.ObjectRequest;
import com.yakin.oktp.model.ObjectResult;
import com.yakin.oktp.internal.AsyncRequestTask;

public class Oktp implements IRequester {

    public static final RequestConfig Config = new RequestConfig();

    private IRequester requester;

    private Oktp(String endpoint) {
        requester = new RequestOperation(endpoint);
    }

    public static IRequester getRequester() {
        return new Oktp(null);
    }

    public static IRequester getRequester(String endpoint) {
        return new Oktp(endpoint);
    }

    @Override
    public ObjectResult get(ObjectRequest request) throws ClientException {
        return requester.get(request);
    }

    @Override
    public AsyncRequestTask get(ObjectRequest request, CompletedCallback completedCallback) {
        return requester.get(request, completedCallback);
    }

    @Override
    public ObjectResult delete(ObjectRequest request) throws ClientException {
        return requester.delete(request);
    }

    @Override
    public AsyncRequestTask delete(ObjectRequest request, CompletedCallback completedCallback) {
        return requester.delete(request, completedCallback);
    }

    @Override
    public ObjectResult head(ObjectRequest request) throws ClientException {
        return requester.head(request);
    }

    @Override
    public AsyncRequestTask head(ObjectRequest request, CompletedCallback completedCallback) {
        return requester.head(request, completedCallback);
    }

    @Override
    public ObjectResult put(ObjectRequest request) throws ClientException {
        return requester.put(request);
    }

    @Override
    public AsyncRequestTask put(ObjectRequest request, CompletedCallback completedCallback) {
        return requester.put(request, completedCallback);
    }

    @Override
    public ObjectResult post(ObjectRequest request) throws ClientException {
        return requester.post(request);
    }

    @Override
    public AsyncRequestTask post(ObjectRequest request, CompletedCallback completedCallback) {
        return requester.post(request, completedCallback);
    }
}
