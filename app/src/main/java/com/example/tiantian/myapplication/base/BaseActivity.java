package com.example.tiantian.myapplication.base;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.tiantian.myapplication.utils.ActivityManager;

import java.lang.reflect.ParameterizedType;

public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity implements BaseInitListener {

    protected B binding;
    protected VM viewModel;
    protected Fragment currFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        ActivityManager.getInstance().add(this);
        viewModel = getViewModel();
        if (getToolbar() != null) {
            setSupportActionBar(getToolbar());
            if (getSupportActionBar() != null) {
                initToolbar(getSupportActionBar());
            }
        }
        initView(savedInstanceState);
        initEvent();
        initData();
    }

    protected void initToolbar(ActionBar actionBar) {

    }

    protected Toolbar getToolbar() {
        return null;
    }

    @SuppressWarnings("unchecked")
    private VM getViewModel() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<VM> vmClass = (Class<VM>) type.getActualTypeArguments()[1];
        return ViewModelProviders.of(this).get(vmClass);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onHomeClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onHomeClick() {
        finish();
    }

    protected void setFragment(int content, Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(content, to).commit();
    }

    protected void showFragment(int content, Fragment to) {
        if (currFragment == null) {
            currFragment = to;
        }
        if(currFragment!=to){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(currFragment);
            if (to.isAdded()) {
                transaction.show(to);
            } else {
                transaction.add(content, to);
            }
            transaction.commit();
        }
    }
}
