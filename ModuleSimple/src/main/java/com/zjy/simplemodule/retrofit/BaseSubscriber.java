package com.zjy.simplemodule.retrofit;

import android.content.Context;

import com.zjy.simplemodule.utils.ActivityManager;
import com.zjy.simplemodule.utils.NetWorkUtils;
import com.zjy.simplemodule.utils.ToastUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class BaseSubscriber<T> implements Subscriber<T> {

    private Context context;
    private boolean emptyFlag = true;

    public BaseSubscriber() {
    }

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        if (!NetWorkUtils.isNetworkAvailable(ActivityManager.getInstance().getCurrActivity())) {
            ToastUtils.showToastShort(ActivityManager.getInstance().getCurrActivity(), "没有网络服务");
            s.cancel();
            return;
        }
        if (context != null) {
            //show load
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
        emptyFlag = false;
    }

    @Override
    public void onError(Throwable t) {
        if (context != null) {
            //dismiss load
        }
        HttpResultException exception = new HttpResultException(t);
        ToastUtils.showToastShort(ActivityManager.getInstance().getCurrActivity(), exception.getErrorMessage());
        onFailure(exception);
    }

    @Override
    public void onComplete() {
        if (context != null) {
            //dismiss load
        }
        if (emptyFlag)
            onEmpty();
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(HttpResultException exception);

    public void onEmpty() {
    }
}
