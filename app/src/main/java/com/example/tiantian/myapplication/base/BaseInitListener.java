package com.example.tiantian.myapplication.base;

import android.os.Bundle;

public interface BaseInitListener {

    int getLayoutId();

    void initView(Bundle savedInstanceState);

    void initEvent();

    void initData();

}
