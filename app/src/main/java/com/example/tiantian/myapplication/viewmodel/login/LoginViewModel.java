package com.example.tiantian.myapplication.viewmodel.login;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.data.login.User;
import com.example.tiantian.myapplication.repository.login.LoginRepository;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.base.Contracts;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultException;
import com.zjy.simplemodule.utils.ActivityManager;
import com.zjy.simplemodule.utils.DiskCache;
import com.zjy.simplemodule.utils.SharedPreferencesUtils;
import com.zjy.simplemodule.utils.ToastUtils;

public class LoginViewModel extends BaseViewModel<LoginRepository> {

    private MutableLiveData<User> registerData;
    private MutableLiveData<User> loginData;

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
                ToastUtils.showToastShort(repository.getCurrActivity(), exception.getErrorMessage());
            }
        });
    }

    public MutableLiveData<User> getLoginData() {
        if (loginData == null)
            loginData = new MutableLiveData<>();
        return loginData;
    }

    public void login(String username, final String password) {
        repository.login(username, password, new BaseSubscriber<User>() {
            @Override
            public void onSuccess(User user) {
                ActivityManager.getInstance().removeActivity(repository.getCurrActivity());
                repository.getCurrActivity().sendBroadcast(new Intent(com.example.tiantian.myapplication.base.Contracts.LOGIN));
                ToastUtils.showToastShort(repository.getCurrActivity(), "登录成功");
                loginData.setValue(user);
//                DiskCache.with(repository.getCurrActivity()).cachePath(Contracts.COOKIE_PATH).saveAndClose(Contracts.COOKIE_SYNC, false);
                SharedPreferencesUtils.with(repository.getCurrActivity()).put(Contracts.COOKIE_SYNC, false);
            }

            @Override
            public void onFailure(HttpResultException exception) {
//                DiskCache.with(repository.getCurrActivity()).cachePath(Contracts.COOKIE_PATH).saveAndClose(Contracts.COOKIE_SYNC, false);
                SharedPreferencesUtils.with(repository.getCurrActivity()).put(Contracts.COOKIE_SYNC, false);
            }
        });
    }
}
