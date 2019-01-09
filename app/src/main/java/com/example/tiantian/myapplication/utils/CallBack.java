package com.example.tiantian.myapplication.utils;

public interface CallBack<T> {

    void onSuccess(T t);
    void onFailure(Throwable throwable);

}
