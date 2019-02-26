package com.yakin.oktp.model;

import java.util.LinkedHashMap;
import java.util.Map;

class HttpHeader {

    private Map<String, String> headers = new LinkedHashMap<>();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        if (!this.headers.isEmpty()) {
            this.headers.clear();
        }
        this.headers.putAll(headers);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
