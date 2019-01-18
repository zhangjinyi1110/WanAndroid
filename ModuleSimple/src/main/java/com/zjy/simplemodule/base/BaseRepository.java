package com.zjy.simplemodule.base;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.zjy.simplemodule.utils.ActivityManager;

public abstract class BaseRepository {

    protected final String TAG = getClass().getSimpleName();
    private Context context;

    public void with(Context context) {
        this.context = context;
        init();
    }

    protected abstract void init();

    public Context getContext() {
        return context;
    }

    public FragmentActivity getCurrActivity() {
        return ActivityManager.getInstance().getCurrActivity();
    }

    public void close() {
    }

}
