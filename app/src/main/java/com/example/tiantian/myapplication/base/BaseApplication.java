package com.example.tiantian.myapplication.base;

import android.app.Application;

import com.example.tiantian.myapplication.utils.ActivityManager;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityManager.getInstance().init();
    }
}
