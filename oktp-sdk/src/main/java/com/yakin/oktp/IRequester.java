package com.yakin.oktp;

import com.yakin.oktp.callback.CompletedCallback;
import com.yakin.oktp.model.ObjectRequest;
import com.yakin.oktp.model.ObjectResult;
import com.yakin.oktp.internal.AsyncRequestTask;

public interface IRequester<Request extends ObjectRequest, Result extends ObjectResult> {

    Result get(Request request) throws ClientException;

    AsyncRequestTask get(Request request, CompletedCallback<Request, Result> completedCallback);

    Result delete(Request request) throws ClientException;

    AsyncRequestTask delete(Request request, CompletedCallback<Request, Result> completedCallback);

    Result head(Request request) throws ClientException;

    AsyncRequestTask head(Request request, CompletedCallback<Request, Result> completedCallback);

    Result put(Request request) throws ClientException;

    AsyncRequestTask put(Request request, CompletedCallback<Request, Result> completedCallback);

    Result post(Request request) throws ClientException;

    AsyncRequestTask post(Request request, CompletedCallback<Request, Result> completedCallback);
}
