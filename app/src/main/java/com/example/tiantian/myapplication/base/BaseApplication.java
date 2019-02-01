package com.example.tiantian.myapplication.base;

import com.zjy.simplemodule.interceptor.SyncCookieInterceptor;
import com.zjy.simplemodule.retrofit.NetWorkConfig;

import java.util.concurrent.TimeUnit;

public class BaseApplication extends com.zjy.simplemodule.base.BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkConfig.with(this)
                .base("http://www.wanandroid.com/")
                .timeout(8000)
                .timeoutUnit(TimeUnit.MILLISECONDS)
                .addInterceptor(new SyncCookieInterceptor(this));
    }
}
