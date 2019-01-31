package com.example.tiantian.myapplication.repository.settings;

import android.util.Log;

import com.example.tiantian.myapplication.api.LoginService;
import com.zjy.simplemodule.base.BaseRepository;
import com.zjy.simplemodule.base.Contracts;
import com.zjy.simplemodule.retrofit.AutoDisposeUtils;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultTransformer;
import com.zjy.simplemodule.retrofit.RetrofitUtils;
import com.zjy.simplemodule.utils.DiskCache;
import com.zjy.simplemodule.utils.SharedPreferencesUtils;

public class SettingsRepository extends BaseRepository {

    @Override
    protected void init() {

    }

    public void logout(BaseSubscriber<String> subscriber) {
        RetrofitUtils.getInstance()
                .createService(LoginService.class)
                .logout()
                .compose(new HttpResultTransformer<String>())
                .as(AutoDisposeUtils.<String>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public long getCacheSize() {
        return DiskCache.with(getCurrActivity()).size();
    }


    public void clearCache() {
        DiskCache.with(getCurrActivity()).clear();
    }

    public boolean isLogin() {
        int size = SharedPreferencesUtils.with(getCurrActivity()).getStringSet(Contracts.COOKIE_NAME).size();
        return size > 0;
    }

}
