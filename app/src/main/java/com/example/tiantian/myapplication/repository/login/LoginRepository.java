package com.example.tiantian.myapplication.repository.login;

import com.example.tiantian.myapplication.api.LoginService;
import com.example.tiantian.myapplication.data.login.User;
import com.zjy.simplemodule.base.BaseRepository;
import com.zjy.simplemodule.base.Contracts;
import com.zjy.simplemodule.retrofit.AutoDisposeUtils;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultTransformer;
import com.zjy.simplemodule.retrofit.RetrofitUtils;
import com.zjy.simplemodule.utils.DiskCache;
import com.zjy.simplemodule.utils.SharedPreferencesUtils;

public class LoginRepository extends BaseRepository {
    @Override
    protected void init() {

    }

    public void register(String username, String password, String repassword, BaseSubscriber<User> subscriber) {
        RetrofitUtils.getInstance()
                .createService(LoginService.class)
                .register(username, password, repassword)
                .compose(new HttpResultTransformer<User>())
                .as(AutoDisposeUtils.<User>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void login(String username, String password, BaseSubscriber<User> subscriber) {
//        DiskCache.with(getCurrActivity()).cachePath(Contracts.COOKIE_PATH).saveAndClose(Contracts.COOKIE_SYNC, true);
        SharedPreferencesUtils.with(getCurrActivity()).put(Contracts.COOKIE_SYNC, true);
        RetrofitUtils.getInstance()
                .createService(LoginService.class)
                .login(username, password)
                .compose(new HttpResultTransformer<User>())
                .as(AutoDisposeUtils.<User>bind(getCurrActivity()))
                .subscribe(subscriber);
    }
}
