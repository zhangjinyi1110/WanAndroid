package com.example.tiantian.myapplication.utils;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class HttpSubscriber<T> implements Subscriber<T> {

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

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

    public void failure(Throwable t){
        Log.e("HttpFailure", "failure: " + t.toString());
    }

}
