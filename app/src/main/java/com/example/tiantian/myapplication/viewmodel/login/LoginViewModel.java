package com.example.tiantian.myapplication.viewmodel.login;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.data.login.User;
import com.example.tiantian.myapplication.repository.login.LoginRepository;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultException;
import com.zjy.simplemodule.utils.ToastUtils;

public class LoginViewModel extends BaseViewModel<LoginRepository> {

    private MutableLiveData<User> registerData;
    private MutableLiveData<User> loginData;
    private MutableLiveData<Object> logoutData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<User> getRegisterData() {
        if (registerData == null)
            registerData = new MutableLiveData<>();
        return registerData;
    }

    public void register(String username, String password, String repassword) {
        repository.register(username, password, repassword, new BaseSubscriber<User>() {
            @Override
            public void onSuccess(User user) {
                ToastUtils.showToastShort(repository.getCurrActivity(), "注册成功");
                registerData.setValue(user);
            }

            @Override
            public void onFailure(HttpResultException exception) {
                ToastUtils.showToastShort(repository.getCurrActivity(), "注册失败，请重试");
            }
        });
    }

    public MutableLiveData<User> getLoginData() {
        if (loginData == null)
            loginData = new MutableLiveData<>();
        return loginData;
    }

    public void login(String username, String password) {
        repository.login(username, password, new BaseSubscriber<User>() {
            @Override
            public void onSuccess(User user) {
                ToastUtils.showToastShort(repository.getCurrActivity(), "登录成功");
                loginData.setValue(user);
            }

            @Override
            public void onFailure(HttpResultException exception) {
                ToastUtils.showToastShort(repository.getCurrActivity(), "登录失败，请重试");
            }
        });
    }

    public MutableLiveData<Object> getLogoutData() {
        if (logoutData == null)
            logoutData = new MutableLiveData<>();
        return logoutData;
    }

    public void logout() {
        repository.logoout(new BaseSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {
                logoutData.setValue(new Object());
            }

            @Override
            public void onFailure(HttpResultException exception) {

            }
        });
    }
}
