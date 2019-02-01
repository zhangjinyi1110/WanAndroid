package com.zjy.simplemodule.retrofit;

import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static RetrofitUtils utils;
    private OkHttpClient client;

    public static RetrofitUtils getInstance() {
        if (utils == null) {
            synchronized (RetrofitUtils.class) {
                if (utils == null) {
                    utils = new RetrofitUtils();
                }
            }
        }
        return utils;
    }

    public <S> S createService(Class<S> sClass) {
        return createService(sClass, NetWorkConfig.getBaseUrl());
    }

    public <S> S createService(Class<S> sClass, String url) {
        return getRetrofit(url).create(sClass);
    }

    public Retrofit getRetrofit(String url) {
        return getRetrofit(url,getClient());
    }

    public Retrofit getRetrofit(String url, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    private OkHttpClient getClient() {
        if (client == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("retrofit", "log: " + message);
                }
            });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .writeTimeout(NetWorkConfig.getWriteTimeout(), NetWorkConfig.getTimeoutUnit())
                    .readTimeout(NetWorkConfig.getReadTimeout(), NetWorkConfig.getTimeoutUnit())
                    .connectTimeout(NetWorkConfig.getConnectTimeout(), NetWorkConfig.getTimeoutUnit())
                    .addInterceptor(interceptor);
            for (Interceptor i : NetWorkConfig.getInterceptors()) {
                builder.addInterceptor(i);
            }
            client = builder.build();
        }
        return client;
    }

}
