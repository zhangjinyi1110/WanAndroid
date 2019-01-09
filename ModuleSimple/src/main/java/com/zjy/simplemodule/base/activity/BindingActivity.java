package com.zjy.simplemodule.base.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public abstract class BindingActivity<B extends ViewDataBinding> extends BaseActivity {

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
