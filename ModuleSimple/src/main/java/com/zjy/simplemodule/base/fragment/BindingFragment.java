package com.zjy.simplemodule.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BindingFragment<B extends ViewDataBinding> extends BaseFragment {

    protected B binding;

    @Override
    protected boolean isBinding() {
        return true;
    }

    @Override
    protected View bindView(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return binding.getRoot();
    }

}
