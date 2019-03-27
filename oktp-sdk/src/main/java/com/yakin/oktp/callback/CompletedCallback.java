package com.yakin.oktp.callback;

import com.yakin.oktp.ClientException;
import com.yakin.oktp.model.ObjectRequest;
import com.yakin.oktp.model.ObjectResult;

public interface CompletedCallback<Request extends ObjectRequest, Result extends ObjectResult> {

    void onSuccess(Request request, Result result);

    void onFailure(Request request, ClientException clientException);
}
