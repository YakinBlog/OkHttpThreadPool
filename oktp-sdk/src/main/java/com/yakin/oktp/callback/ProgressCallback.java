package com.yakin.oktp.callback;

public interface ProgressCallback<T> {

    void onProgress(T request, long currentSize, long totalSize);
}