package com.zjy.simplemodule.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    private FragmentActivity activity;
    private boolean isFirst = true;
    private boolean isLazy = true;
    protected final String TAG = getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            activity = (FragmentActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isViewModel())
            initViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (isBinding()) {
            view = bindView(inflater, container);
        } else {
            view = inflater.inflate(getLayoutId(), container, false);
        }
        initView(savedInstanceState);
        initEvent();
        if (!isLazy() || getUserVisibleHint()) {
            initData();
            setLazy(false);
        }
        isFirst = false;
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFirst && isLazy()) {
            initData();
            setLazy(false);
        }
    }

    protected View bindView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    protected boolean isBinding() {
        return false;
    }

    protected boolean isViewModel() {
        return false;
    }

    protected void initViewModel(){

    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    public FragmentActivity getSelfActivity() {
        return activity;
    }
}
