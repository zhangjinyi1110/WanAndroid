package com.zjy.simplemodule.retrofit;

import android.util.Log;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class HttpResultFunction<T> implements Function<HttpResult<T>, Publisher<T>> {

    @Override
    public Publisher<T> apply(HttpResult<T> tHttpResult) throws Exception {
        if (tHttpResult.getData() == null) {
            if (tHttpResult.getErrorCode() == 0)
                return Flowable.empty();
            Log.e(getClass().getSimpleName(), "apply: " + tHttpResult.getErrorCode());
            return Flowable.error(new NullResultException(tHttpResult));
        }
        return Flowable.just(tHttpResult.getData());
    }
}
