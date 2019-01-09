package com.zjy.simplemodule.rxcallback;

import android.content.Intent;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class RxAvoidResult {

    private RxFragment fragment;
    private Observable<Avoid> observable;

    RxAvoidResult(RxFragment fragment, Intent intent) {
        this.fragment = fragment;
        observable =  Observable.just(intent)
                .flatMap(new Function<Intent, ObservableSource<Avoid>>() {
                    @Override
                    public ObservableSource<Avoid> apply(Intent intent) throws Exception {
                        PublishSubject<Avoid> subject = PublishSubject.create();
                        RxAvoidResult.this.fragment.avoid(subject, intent);
                        return subject;
                    }
                });
    }

    Observable<Avoid> getObservable() {
        return observable;
    }

    public void callback(final RxCallback<Avoid> callback) {
        observable.subscribe(new Observer<Avoid>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Avoid avoid) {
                callback.onResult(avoid);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(getClass().getSimpleName(), "onError: " + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
