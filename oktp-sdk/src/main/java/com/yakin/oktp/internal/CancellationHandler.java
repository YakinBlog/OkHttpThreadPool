package com.yakin.oktp.internal;

import okhttp3.Call;

public class CancellationHandler {

    private volatile boolean isCancelled;

    private volatile Call call;

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
        isCancelled = true;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCall(Call call) {
        this.call = call;
    }
}
