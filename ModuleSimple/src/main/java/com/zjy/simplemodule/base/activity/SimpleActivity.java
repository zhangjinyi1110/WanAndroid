package com.zjy.simplemodule.base.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.zjy.simplemodule.base.BaseViewModel;

import java.util.List;

public abstract class SimpleActivity<VM extends BaseViewModel> extends AbsActivity<VM> {

    private FragmentManager manager;
    private Fragment currFragment;

    @Override
    protected void initView(Bundle savedInstanceState) {
        manager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            for (Fragment f : manager.getFragments()) {
                manager.beginTransaction().remove(f).commit();
            }
            manager.getFragments().clear();
        }
        currFragment = getFragment();
        if (currFragment != null) {
            manager.beginTransaction()
                    .add(getContentId(), currFragment, currFragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
    }

    protected void changeFragment(Fragment fragment) {
        if (currFragment != fragment) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(currFragment);
            if (fragment.isAdded()) {
                transaction.show(fragment);
            } else {
                transaction.add(getContentId(), fragment, fragment.getClass().getSimpleName());
            }
            currFragment = fragment;
            transaction.commit();
        }
    }

    protected void showFragment(List<Fragment> fragments, Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment f : fragments) {
            transaction.hide(f);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(getContentId(), fragment, fragment.getClass().getSimpleName());
        }
        currFragment = fragment;
        transaction.commit();
    }

    protected void removeFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(currFragment);
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(getContentId(), fragment, fragment.getClass().getSimpleName());
        }
        transaction.commit();
        currFragment = fragment;
    }

    protected abstract int getContentId();

    protected abstract Fragment getFragment();

}
