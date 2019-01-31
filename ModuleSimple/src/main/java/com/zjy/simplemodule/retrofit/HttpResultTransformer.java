package com.zjy.simplemodule.retrofit;

import android.content.Context;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HttpResultTransformer<T> implements FlowableTransformer<HttpResult<T>, T> {

    private Context context;

    public HttpResultTransformer() {
    }

    public HttpResultTransformer(Context context) {
        this.context = context;
    }

    @Override
    public Publisher<T> apply(Flowable<HttpResult<T>> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .flatMap(new HttpResultFunction<T>())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
