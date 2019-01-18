package com.example.tiantian.myapplication.activity.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.data.login.ViewData;
import com.example.tiantian.myapplication.databinding.ActivityLoginBinding;
import com.example.tiantian.myapplication.fragment.login.LoginFragment;
import com.example.tiantian.myapplication.fragment.login.RegisterFragment;
import com.zjy.simplemodule.base.activity.BindingSimpleActivity;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class LoginActivity extends BindingSimpleActivity<ActivityLoginBinding> {

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private PublishSubject<ViewData> loginSubject;
    private FragmentManager manager;

    @Override
    protected int getContentId() {
        return R.id.login_content;
    }

    @Override
    protected Fragment getFragment() {
        if (loginFragment == null)
            loginFragment = LoginFragment.newInstance();
        return loginFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected Toolbar getToolBar() {
        return (Toolbar) binding.loginToolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setTitle("登录");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        manager = getSupportFragmentManager();
        loginSubject = PublishSubject.create();
        loginFragment.setSubject(loginSubject);
        registerFragment = RegisterFragment.newInstance();
        registerFragment.setSubject(loginSubject);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initEvent() {
        super.initEvent();
        loginSubject.subscribe(new Consumer<ViewData>() {
            @Override
            public void accept(ViewData data) {
                anim(data.isFlag());
                getSupportActionBar().setTitle(data.isFlag() ? "登录" : "注册");
                if (data.getUser() != null) {
                    loginFragment.login(data.getUser().getUsername(), data.getUser().getPassword());
                }
            }
        });
    }

    private void anim(boolean flag) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (flag) {
            transaction.setCustomAnimations(R.animator.fragment_3d_reversal_enter, R.animator.fragment_3d_reversal_exit
                    , R.animator.fragment_second_3d_reversal_enter, R.animator.fragment_second_3d_reversal_exit)
                    .hide(registerFragment)
                    .show(loginFragment);
        } else {
            if (!registerFragment.isAdded()) {
                transaction.setCustomAnimations(R.animator.fragment_3d_reversal_enter, R.animator.fragment_3d_reversal_exit
                        , R.animator.fragment_second_3d_reversal_enter, R.animator.fragment_second_3d_reversal_exit)
                        .hide(loginFragment)
                        .add(R.id.login_content, registerFragment);
            } else {
                transaction.setCustomAnimations(R.animator.fragment_3d_reversal_enter, R.animator.fragment_3d_reversal_exit
                        , R.animator.fragment_second_3d_reversal_enter, R.animator.fragment_second_3d_reversal_exit)
                        .hide(loginFragment)
                        .show(registerFragment);
            }
        }
        transaction.commit();
    }

    @Override
    protected boolean isHideKeyboard() {
        return true;
    }
}
