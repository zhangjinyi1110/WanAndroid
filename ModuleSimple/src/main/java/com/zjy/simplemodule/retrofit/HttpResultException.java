package com.zjy.simplemodule.retrofit;

public class HttpResultException extends Exception {

    private int errorType;
    private String errorMessage;
    private Throwable throwable;

    public HttpResultException(int errorType, String errorMessage, Throwable throwable) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.throwable = throwable;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
