package com.zjy.simplemodule.rxcallback;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.zjy.simplemodule.utils.CheckSdkUtils;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class RxPermissionResult {

    private RxFragment fragment;
    private String[] permissions;
    private Observable<ArrayList<Permission>> observable;

    RxPermissionResult(RxFragment fragment, String[] permissions, boolean filter) {
        this.fragment = fragment;
        if (fragment.getActivity() != null && filter) {
            ArrayList<String> permissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(fragment.getActivity(), permission) == PackageManager.PERMISSION_GRANTED)
                    continue;
                permissionList.add(permission);
            }
            this.permissions = permissionList.toArray(new String[0]);
        } else
            this.permissions = permissions;
        init();
    }

    private void init() {
        observable = Observable.just(permissions)
                .flatMap(new Function<String[], ObservableSource<ArrayList<Permission>>>() {
                    @Override
                    public ObservableSource<ArrayList<Permission>> apply(String[] strings) throws Exception {
                        PublishSubject<ArrayList<Permission>> subject = PublishSubject.create();
                        fragment.requestPermissions(subject, permissions);
                        return subject;
                    }
                });
    }

    Observable<ArrayList<Permission>> getObservable() {
        return observable;
    }

    public void callback(final RxCallback<ArrayList<Permission>> callback) {
        if (CheckSdkUtils.lessThen(Build.VERSION_CODES.M)) {
            return;
        }
        observable.subscribe(new Observer<ArrayList<Permission>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ArrayList<Permission> permissions) {
                callback.onResult(permissions);
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
