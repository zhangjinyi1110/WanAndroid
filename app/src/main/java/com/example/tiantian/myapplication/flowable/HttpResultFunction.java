package com.example.tiantian.myapplication.flowable;

import com.example.tiantian.myapplication.data.HttpResult;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class HttpResultFunction<T> implements Function<HttpResult<T>, Publisher<T>> {
    @Override
    public Publisher<T> apply(HttpResult<T> tHttpResult) throws Exception {
        if(tHttpResult.getErrorCode()==-1){
            return Flowable.error(new Exception("error"));
        }
        return Flowable.just(tHttpResult.getData());
    }
}
