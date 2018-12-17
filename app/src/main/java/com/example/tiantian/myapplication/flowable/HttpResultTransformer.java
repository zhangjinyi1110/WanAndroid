package com.example.tiantian.myapplication.flowable;

import android.app.Activity;

import com.example.tiantian.myapplication.data.HttpResult;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HttpResultTransformer<T> implements FlowableTransformer<HttpResult<T>, T> {

    private Activity activity;

    public HttpResultTransformer() {
    }

    public HttpResultTransformer(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Publisher<T> apply(Flowable<HttpResult<T>> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .flatMap(new HttpResultFunction<T>())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        if(activity!=null){
                            loadShow();
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(activity!=null){
                            loadDismiss();
                        }
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(activity!=null){
                            loadDismiss();
                        }
                    }
                })
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(activity!=null){
                            loadDismiss();
                        }
                    }
                });
    }

    private void loadDismiss() {

    }

    private void loadShow() {

    }
}
