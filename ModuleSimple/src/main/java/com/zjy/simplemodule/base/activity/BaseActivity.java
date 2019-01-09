package com.zjy.simplemodule.base.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zjy.simplemodule.utils.ActivityManager;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isBinding()) {
            setContentView(getLayoutId());
        } else {
            bindingView();
        }
        if (isViewModel()) {
            initViewModel();
        }
        ActivityManager.getInstance().addActivity(this);
        if (getToolBar() != null) {
            setSupportActionBar(getToolBar());
            initToolbar(getSupportActionBar());
        }
        initView(savedInstanceState);
        initEvent();
        initData();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();

    protected void bindingView() {
    }

    protected void initViewModel() {

    }

    protected boolean isViewModel() {
        return false;
    }

    protected Toolbar getToolBar() {
        return null;
    }

    protected void initToolbar(ActionBar actionBar) {
    }

    protected boolean isBinding() {
        return false;
    }

    protected BaseActivity getSelf() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return onHomeClick();
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean onHomeClick() {
        finish();
        return true;
    }

}
