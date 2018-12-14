package com.example.tiantian.myapplication.flowable;

import android.app.Activity;

import com.example.tiantian.myapplication.data.HttpResult;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HttpListResultTransformer<T> implements FlowableTransformer<HttpResult<List<T>>, List<T>> {

    private Activity activity;

    public HttpListResultTransformer() {
    }

    public HttpListResultTransformer(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Publisher<List<T>> apply(Flowable<HttpResult<List<T>>> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .flatMap(new HttpListResultFunction<T>())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (activity != null) {
                            //load dismiss
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (activity != null) {
                            //load dismiss
                        }
                    }
                })
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (activity != null) {
                            //load dismiss
                        }
                    }
                })
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        if (activity != null) {
                            // load show
                        }
                    }
                });
    }
}
