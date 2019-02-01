package com.zjy.simplemodule.retrofit;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;

public class NetWorkConfig {

    private static String baseUrl;
    private static List<Interceptor> interceptors;
    private static long writeTimeout = 10;
    private static long readTimeout = 10;
    private static long connectTimeout = 10;
    private static TimeUnit timeoutUnit = TimeUnit.SECONDS;
    private Context context;

    private NetWorkConfig(Context context) {
        this.context = context;
        interceptors = new ArrayList<>();
    }

    public static NetWorkConfig with(Context context) {
        return new NetWorkConfig(context);
    }

    public NetWorkConfig base(String url) {
        NetWorkConfig.baseUrl = url;
        return this;
    }

    public NetWorkConfig addInterceptor(Interceptor interceptor) {
        NetWorkConfig.interceptors.add(interceptor);
        return this;
    }

    public NetWorkConfig addInterceptor(List<Interceptor> interceptors) {
        NetWorkConfig.interceptors.addAll(interceptors);
        return this;
    }

    public NetWorkConfig readTimeout(long readTimeout) {
        NetWorkConfig.readTimeout = readTimeout;
        return this;
    }

    public NetWorkConfig connectTimeout(long connectTimeout) {
        NetWorkConfig.connectTimeout = connectTimeout;
        return this;
    }

    public NetWorkConfig writeTimeout(long writeTimeout) {
        NetWorkConfig.writeTimeout = writeTimeout;
        return this;
    }

    public NetWorkConfig timeout(long timeout) {
        NetWorkConfig.writeTimeout = timeout;
        NetWorkConfig.connectTimeout = timeout;
        NetWorkConfig.readTimeout = timeout;
        return this;
    }

    public NetWorkConfig timeoutUnit(TimeUnit timeoutUnit) {
        NetWorkConfig.timeoutUnit = timeoutUnit;
        return this;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public static TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    public static long getConnectTimeout() {
        return connectTimeout;
    }

    public static long getWriteTimeout() {
        return writeTimeout;
    }

    public static long getReadTimeout() {
        return readTimeout;
    }
}
