package com.zjy.simplemodule.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.zjy.simplemodule.utils.ActivityManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseRepository {

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

}
