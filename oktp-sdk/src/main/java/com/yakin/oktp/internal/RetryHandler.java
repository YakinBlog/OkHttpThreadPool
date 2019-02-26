package com.yakin.oktp.internal;

import com.yakin.oktp.ClientException;
import com.yakin.oktp.model.ObjectResult;

import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

public class RetryHandler {

    public enum RetryType {
        ShouldNotRetry,
        ShouldRetry,
    }

    private int maxRetryCount;

    public RetryHandler(int maxRetryCount) {
        setMaxRetryCount(maxRetryCount);
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public RetryType shouldRetry(Exception e, ObjectResult responseResult, int currentRetryCount) {
        if (currentRetryCount >= maxRetryCount) {
            return RetryType.ShouldNotRetry;
        }

        if (e instanceof ClientException) {
            if (((ClientException) e).isCanceled()) {
                return RetryType.ShouldNotRetry;
            }

            Exception localException = (Exception) e.getCause();
            if (localException instanceof InterruptedIOException
                    && !(localException instanceof SocketTimeoutException)) {
                return RetryType.ShouldNotRetry;
            } else if (localException instanceof IllegalArgumentException) {
                return RetryType.ShouldNotRetry;
            }
            return RetryType.ShouldRetry;
        } if (responseResult != null && responseResult.getResponseCode() >= 500) {
            return RetryType.ShouldRetry;
        } else {
            return RetryType.ShouldNotRetry;
        }
    }

    public long timeInterval(int currentRetryCount, RetryType retryType) {
        if(retryType == RetryType.ShouldRetry) {
            return (long)Math.pow(2, currentRetryCount) * 200;
        }
        return 0;
    }
}
