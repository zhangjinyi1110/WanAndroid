package com.example.tiantian.myapplication.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static RetrofitHelper helper;
    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient.Builder clientBuilder;

    public static RetrofitHelper getInstance() {
        if (helper == null) {
            synchronized (RetrofitHelper.class) {
                if (helper == null) {
                    helper = new RetrofitHelper();
                }
            }
        }
        return helper;
    }

    public RetrofitHelper initRetrofit(Retrofit.Builder builder) {
        if (builder == null)
            this.retrofitBuilder = defaultRetrofitBuilder();
        else
            this.retrofitBuilder = builder;
        return helper;
    }

    public RetrofitHelper initClient(OkHttpClient.Builder builder) {
        if (builder == null)
            this.clientBuilder = defaultClientBuild();
        else
            this.clientBuilder = builder;
        if (this.retrofitBuilder != null)
            this.retrofitBuilder.client(clientBuilder.build());
        return helper;
    }

    public <S> S createService(Class<S> sClass) {
        if (retrofitBuilder == null) {
            initRetrofit(null);
        }
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(sClass);
    }

    private Retrofit.Builder defaultRetrofitBuilder() {
        if (clientBuilder == null) {
            initClient(null);
        }
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("http://wanandroid.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build());
        return builder;
    }

    private OkHttpClient.Builder defaultClientBuild() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        return builder;
    }

}
