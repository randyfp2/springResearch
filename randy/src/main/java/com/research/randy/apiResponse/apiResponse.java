package com.research.randy.apiResponse;

public class apiResponse<T> {
    private String responseCode;
    private String responseMessage;
    private T result;

    // Constructor
    public apiResponse(String responseCode, String responseMessage, T result) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.result = result;
    }

    // Getters and Setters
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
