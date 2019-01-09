package com.zjy.simplemodule.base.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

import com.zjy.simplemodule.base.BaseViewModel;

public abstract class SimpleBindingActivity<VM extends BaseViewModel, B extends ViewDataBinding> extends SimpleActivity<VM> {

    protected B binding;

    @Override
    protected boolean isBinding() {
        return true;
    }

    @Override
    protected void bindingView() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }
}
