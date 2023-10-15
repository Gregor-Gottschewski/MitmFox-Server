package com.gregorgott.mitmfoxserver.server.methodHandlers;

public class Method {
    private String response;
    private int status;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getResponseLength() {
        return response.isEmpty() ? -1 : response.getBytes().length;
    }

    public int getStatus() {
        return status == 0 ? 200 : status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
