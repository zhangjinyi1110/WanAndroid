package com.example.tiantian.myapplication.base;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseViewModel> extends Fragment implements BaseInitListener {

    private AppCompatActivity appCompatActivity;
    private boolean isRefresh = true;
    private boolean isLasy = true;
    protected B binding;
    protected VM viewModel;
    public final String TAG = getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        viewModel = getViewModel();
        initView(savedInstanceState);
        initEvent();
        initData();
        return binding.getRoot();
    }

    @SuppressWarnings("unchecked")
    private VM getViewModel() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<VM> vmClass = (Class<VM>) type.getActualTypeArguments()[1];
        return ViewModelProviders.of(this).get(vmClass);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isRefresh && isLasy) {
            refresh();
            isRefresh = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void refresh() {

    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public void setLasy(boolean lasy) {
        isLasy = lasy;
    }

    public AppCompatActivity getSelfActivity() {
        return appCompatActivity;
    }
}
