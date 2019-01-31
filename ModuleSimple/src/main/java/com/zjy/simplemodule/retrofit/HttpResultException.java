package com.zjy.simplemodule.retrofit;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.text.ParseException;

import retrofit2.HttpException;

public class HttpResultException extends Exception {

    private int errorType;
    private String errorMessage;
    private Throwable throwable;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    private static final int ERROR_PARSE = -1;
    private static final int ERROR_CONNECT = -2;
    private static final int ERROR_SSL = -3;
    private static final int ERROR_UNKNOW = -4;
    private static final int ERROR_UNNET = -5;

    public HttpResultException(Throwable throwable) {
        this.throwable = throwable;
        init();
    }

    private void init() {
        if (throwable instanceof NullResultException) {
            Log.e(getClass().getSimpleName(), "init: ");
            errorType = ((NullResultException) throwable).getErrorCode();
            errorMessage = ((NullResultException) throwable).getErrorMsg();
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    errorType = httpException.code();
                    errorMessage = "网络错误";
                    break;
            }
        }
//        else if (throwable instanceof ServerException) {
//            ServerException resultException = (ServerException) e;
//            ex.message = resultException.message;
//            return ex;
//        }
        else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            errorType = ERROR_PARSE;
            errorMessage = "解析错误";
        } else if (throwable instanceof ConnectException) {
            errorMessage = "连接失败";
            errorType = ERROR_CONNECT;
        } else if (throwable instanceof javax.net.ssl.SSLHandshakeException) {
            errorMessage = "证书验证失败";
            errorType = ERROR_SSL;
        } else {
            errorMessage = "未知错误";
            errorType = ERROR_UNKNOW;
        }
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
