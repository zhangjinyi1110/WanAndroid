package com.zjy.simplemodule.retrofit;

import android.util.Log;

import com.zjy.simplemodule.interceptor.AddCookieInterceptor;
import com.zjy.simplemodule.interceptor.TwoInterceptor;
import com.zjy.simplemodule.utils.ActivityManager;

import java.util.concurrent.TimeUnit;

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
        return createService(sClass, "http://www.wanandroid.com/");
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
                    Log.e(getClass().getSimpleName(), "log: " + message);
                }
            });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder()
                    .writeTimeout(10000, TimeUnit.SECONDS)
                    .readTimeout(10000, TimeUnit.SECONDS)
                    .connectTimeout(10000, TimeUnit.SECONDS)
                    .addInterceptor(new TwoInterceptor())
                    .addInterceptor(new AddCookieInterceptor(ActivityManager.getInstance().getCurrActivity()))
//                    .addInterceptor(interceptor)
                    .build();
        }
        return client;
    }

}
