package com.zjy.simplemodule.retrofit;

public class NullResultException extends Exception {

    private String errorMsg;
    private int errorCode;

    public NullResultException(HttpResult httpResult) {
        this.errorCode = httpResult.getErrorCode();
        this.errorMsg = httpResult.getErrorMsg();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
