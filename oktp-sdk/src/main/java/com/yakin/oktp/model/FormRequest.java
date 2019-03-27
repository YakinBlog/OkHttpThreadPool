package com.yakin.oktp.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormRequest extends ObjectRequest {

    private Map<String, String> mapBody = new LinkedHashMap<>();
    private String stringBody;
    private long contentLength;
    private byte[] body;

    public FormRequest() {
        addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    }

    public void setMapBody(Map<String, String> mapBody) {
        if(!this.mapBody.isEmpty()) {
            this.mapBody.clear();
        }
        this.mapBody.putAll(mapBody);
        this.contentLength = 0;
    }

    public void addMapBody(String key, String value) {
        this.mapBody.put(key, value);
        this.contentLength = 0;
    }

    public void setStringBody(String stringBody) {
        this.stringBody = stringBody;
        this.contentLength = 0;
    }

    private void processBody() throws Exception {
        if(contentLength == 0) {
            StringBuilder builder = new StringBuilder(stringBody);
            for (Map.Entry<String, String> entry : mapBody.entrySet()) {
                if(builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
            }
            this.body = builder.toString().getBytes("UTF-8");
            this.contentLength = body.length;
        }
    }

    @Override
    public InputStream getContent() throws Exception {
        processBody();
        return new ByteArrayInputStream(body);
    }

    @Override
    public long getContentLength() throws Exception {
        processBody();
        return contentLength;
    }
}
