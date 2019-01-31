package com.example.tiantian.myapplication.fragment.login;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.base.Contracts;
import com.example.tiantian.myapplication.data.login.User;
import com.example.tiantian.myapplication.data.login.ViewData;
import com.example.tiantian.myapplication.databinding.FragmentLoginBinding;
import com.example.tiantian.myapplication.utils.TextUtils;
import com.example.tiantian.myapplication.viewmodel.login.LoginViewModel;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;
import com.zjy.simplemodule.utils.SharedPreferencesUtils;

import io.reactivex.subjects.PublishSubject;

public class LoginFragment extends AbsBindingFragment<LoginViewModel, FragmentLoginBinding> {

    private PublishSubject<ViewData> subject;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected void observe() {
        viewModel.getLoginData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
//                    DiskCache.with(getSelfActivity()).cachePath(Contracts.USER_PATH).saveAndClose(Contracts.USER_NAME, user.getUsername());
                    SharedPreferencesUtils.with(getSelfActivity()).put(Contracts.USER_NAME, user.getUsername());
                    getSelfActivity().setResult(Activity.RESULT_OK, new Intent().putExtra(Contracts.USER_NAME, user.getUsername()));
                    getSelfActivity().finish();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        binding.toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.onNext(new ViewData(false));
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        binding.loginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                login();
                return true;
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void login() {
        String username = TextUtils.viewGetText(binding.loginUsername);
        String password = TextUtils.viewGetText(binding.loginPassword);
        viewModel.login(username, password);
    }

    public void setSubject(PublishSubject<ViewData> subject) {
        this.subject = subject;
    }

    public void login(String username, String password) {
        binding.loginPassword.setText(password);
        binding.loginUsername.setText(username);
    }

}
