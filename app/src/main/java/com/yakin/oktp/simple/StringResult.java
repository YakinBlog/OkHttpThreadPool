package com.yakin.oktp.simple;

import com.yakin.oktp.model.ObjectResult;

public class StringResult extends ObjectResult {

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
