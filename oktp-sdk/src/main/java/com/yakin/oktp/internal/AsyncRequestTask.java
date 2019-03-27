package com.yakin.oktp.internal;

import com.yakin.oktp.ClientException;
import com.yakin.oktp.model.ObjectResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncRequestTask<Result extends ObjectResult> {

    private Future<Result> future;

    private ExecutionContext context;

    private volatile boolean canceled;

    public static AsyncRequestTask<ObjectResult> wrapRequestTask(Future<ObjectResult> future, ExecutionContext context) {
        AsyncRequestTask<ObjectResult> asynTask = new AsyncRequestTask<>();
        asynTask.future = future;
        asynTask.context = context;
        return asynTask;
    }

    public void cancel() {
        canceled = true;
        if (context != null) {
            context.getCancellationHandler().cancel();
        }
    }

    public boolean isCompleted() {
        return future.isDone();
    }

    public Result getResult() throws ClientException {
        try {
            Result result = future.get();
            return result;
        } catch (InterruptedException e) {
            throw new ClientException("InterruptedException and message : " + e.getMessage(), e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ClientException) {
                throw (ClientException) cause;
            } else {
                throw new ClientException("Unexpected exception : " + cause.getMessage(), cause);
            }
        }
    }

    public void waitUntilFinished() {
        try {
            future.get();
        } catch (Exception ignore) {
        }
    }

    public boolean isCanceled() {
        return canceled;
    }
}
