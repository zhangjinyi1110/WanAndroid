package com.example.tiantian.myapplication.flowable;

import com.example.tiantian.myapplication.data.HttpResult;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class HttpListResultFunction<T> implements Function<HttpResult<List<T>>, Publisher<List<T>>> {
    @Override
    public Publisher<List<T>> apply(HttpResult<List<T>> httpResults) throws Exception {
        if(httpResults.getErrorCode()==-1){
            return Flowable.error(new Exception("error"));
        }
        return Flowable.just(httpResults.getData());
    }
}
