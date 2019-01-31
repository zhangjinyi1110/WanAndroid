package com.example.tiantian.myapplication.fragment.login;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.data.login.User;
import com.example.tiantian.myapplication.data.login.ViewData;
import com.example.tiantian.myapplication.databinding.FragmentRegisterBinding;
import com.example.tiantian.myapplication.utils.TextUtils;
import com.example.tiantian.myapplication.viewmodel.login.LoginViewModel;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;
import com.zjy.simplemodule.utils.ToastUtils;

import io.reactivex.subjects.PublishSubject;

public class RegisterFragment extends AbsBindingFragment<LoginViewModel, FragmentRegisterBinding> {

    private PublishSubject<ViewData> subject;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    protected void observe() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        binding.toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.onNext(new ViewData(true));
            }
        });
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        binding.registerTwoPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                register();
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        viewModel.getRegisterData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    user.setPassword(binding.registerPassword.getText().toString());
                    subject.onNext(new ViewData(true, user));
                }
            }
        });
    }

    private void register() {
        String username = binding.registerUsername.getText().toString();
        String password = TextUtils.viewGetText(binding.registerPassword);
        String repassword = TextUtils.viewGetText(binding.registerTwoPassword);
        viewModel.register(username, password, repassword);
    }

    public void setSubject(PublishSubject<ViewData> subject) {
        this.subject = subject;
    }

}
