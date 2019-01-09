package com.zjy.simplemodule.rxcallback;

import android.os.Build;
import android.util.Log;

import com.zjy.simplemodule.utils.CheckSdkUtils;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class RxPermissionSimple {

    private Observable<Boolean> observable;

    RxPermissionSimple(RxFragment fragment, String[] permissions, boolean filter) {
        observable = new RxPermissionResult(fragment, permissions, filter)
                .getObservable()
                .flatMap(new Function<ArrayList<Permission>, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(ArrayList<Permission> permissionList) throws Exception {
                        for (Permission permission : permissionList) {
                            if (!permission.isGranted()) {
                                return Observable.just(false);
                            }
                        }
                        return Observable.just(true);
                    }
                });
    }

    Observable<Boolean> getObservable() {
        return observable;
    }

    public void callback(final RxCallback<Boolean> callback) {
        if (CheckSdkUtils.lessThen(Build.VERSION_CODES.M)) {
            return;
        }
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
