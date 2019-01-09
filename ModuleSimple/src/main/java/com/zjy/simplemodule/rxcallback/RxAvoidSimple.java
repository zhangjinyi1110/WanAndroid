package com.zjy.simplemodule.rxcallback;

import android.content.Intent;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class RxAvoidSimple {

    private Observable<Boolean> observable;

    RxAvoidSimple(RxFragment fragment, Intent intent) {
//        this.fragment = fragment;
        observable = new RxAvoidResult(fragment, intent)
                .getObservable()
                .flatMap(new Function<Avoid, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Avoid avoid) throws Exception {
                        return Observable.just(avoid.isOk());
                    }
                });
    }

    public void callback(final RxCallback<Boolean> callback) {
        observable.subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean b) {
                callback.onResult(b);
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
