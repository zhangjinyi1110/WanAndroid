package com.example.tiantian.myapplication.utils;

import android.util.Log;

import io.reactivex.subscribers.ResourceSubscriber;

public abstract class HttpSubscriber<T> extends ResourceSubscriber<T> {

    @Override
    public void onNext(T t) {
        success(t);
    }

    @Override
    public void onError(Throwable t) {
        failure(t);
    }

    @Override
    public void onComplete() {

    }

    public abstract void success(T t);

    public void failure(Throwable t) {
        Log.e("HttpFailure", "failure: " + t.toString());
    }

}
