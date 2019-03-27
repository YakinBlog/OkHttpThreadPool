package com.yakin.oktp.model;

import okhttp3.Response;

public class ObjectResult<Request extends ObjectRequest> extends HttpHeader {

    private int responseCode;
    private Response response;
    private Request request;

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
