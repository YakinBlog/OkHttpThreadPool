package com.yakin.oktp.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DataRequest extends ObjectRequest {

    private byte[] content;

    public DataRequest(byte[] content) {
        this.content = content;
    }

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public long getContentLength() {
        if(content != null) {
            return content.length;
        }
        return 0;
    }
}
