package com.example.tiantian.myapplication.viewmodel.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.repository.settings.SettingsRepository;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.base.Contracts;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultException;
import com.zjy.simplemodule.utils.SharedPreferencesUtils;
import com.zjy.simplemodule.utils.ToastUtils;

import java.util.HashSet;

public class SettingsViewModel extends BaseViewModel<SettingsRepository> {

    private MutableLiveData<Boolean> logoutFlag;
    private MutableLiveData<String> cacheSize;
    private MutableLiveData<Boolean> loginFlag;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getLogoutFlag() {
        if (logoutFlag == null)
            logoutFlag = new MutableLiveData<>();
        return logoutFlag;
    }

    public void logout() {
        repository.logout(new BaseSubscriber<String>() {
            @Override
            public void onSuccess(String o) {
            }

            @Override
            public void onFailure(HttpResultException exception) {
                ToastUtils.showToastShort(repository.getCurrActivity(), exception.getErrorMessage());
            }

            @Override
            public void onEmpty() {
                FragmentActivity activity = repository.getCurrActivity();
                activity.setResult(Activity.RESULT_OK, new Intent().putExtra(com.example.tiantian.myapplication.base.Contracts.USER_NAME, activity.getString(R.string.text_to_login)));
                logoutFlag.setValue(true);
                activity.sendBroadcast(new Intent(com.example.tiantian.myapplication.base.Contracts.LOGOUT));
                ToastUtils.showToastShort(repository.getCurrActivity(), "已退出登录");
//                DiskCache.with(repository.getCurrActivity()).cachePath(Contracts.COOKIE_PATH).remove(Contracts.COOKIE_NAME);
//                DiskCache.close();
                SharedPreferencesUtils.with(repository.getCurrActivity()).put(Contracts.COOKIE_NAME, new HashSet<String>());
            }
        });
    }

    public MutableLiveData<String> getCacheSize() {
        if (cacheSize == null)
            cacheSize = new MutableLiveData<>();
        return cacheSize;
    }

    public void getSize() {
        float cache = (float) repository.getCacheSize() / 1024;
        @SuppressLint("DefaultLocale")
        String size = String.format(cache < 102400 ? "%.2fKB" : "%.2fM", cache / 1024);
        cacheSize.setValue(size);
    }

    public void clearCache() {
        repository.clearCache();
        ToastUtils.showToastShort(repository.getCurrActivity(), "清理成功");
        getSize();
    }

    public MutableLiveData<Boolean> getLoginFlag() {
        if (loginFlag == null)
            loginFlag = new MutableLiveData<>();
        return loginFlag;
    }

    public void isLogin() {
        loginFlag.setValue(repository.isLogin());
    }

}
