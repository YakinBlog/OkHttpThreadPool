package com.yakin.oktp.internal;

import com.yakin.oktp.callback.ProgressCallback;
import com.yakin.oktp.model.ObjectRequest;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class ProgressTouchableRequestBody extends RequestBody {

    private InputStream inputStream;
    private String contentType;
    private long contentLength;
    private ProgressCallback callback;
    private ObjectRequest request;

    public ProgressTouchableRequestBody(InputStream input, long contentLength, String contentType, ExecutionContext context) {
        this.inputStream = input;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.callback = context.getProgressCallback();
        this.request = context.getRequest();
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(this.contentType);
    }

    @Override
    public long contentLength() {
        return this.contentLength;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = Okio.source(this.inputStream);
        long total = 0;
        long read, toRead, remain;

        while (total < contentLength) {
            remain = contentLength - total;
            toRead = Math.min(remain, 2048);

            read = source.read(sink.buffer(), toRead);
            if (read == -1) {
                break;
            }

            total += read;
            sink.flush();

            if (callback != null && total != 0) {
                callback.onProgress(request, total, contentLength);
            }
        }
        if (source != null) {
            source.close();
        }
    }
}