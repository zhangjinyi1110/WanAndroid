package com.zjy.simplemodule.base;

import android.app.Application;

import com.zjy.simplemodule.utils.ActivityManager;
import com.zjy.simplemodule.utils.CacheUtils;

public class BaseApplication extends Application {

    private CacheUtils cacheUtils;
    private ActivityManager activityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        CacheUtils.init(this);
        ActivityManager.init(this);
    }

    public CacheUtils getCacheUtils() {
        return cacheUtils;
    }

    public void setCacheUtils(CacheUtils cacheUtils) {
        this.cacheUtils = cacheUtils;
    }

    public void setActivityManager(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    public ActivityManager getActivityManager() {
        return activityManager;
    }
}
