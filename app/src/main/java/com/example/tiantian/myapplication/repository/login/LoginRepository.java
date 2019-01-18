package com.example.tiantian.myapplication.repository.login;

import com.example.tiantian.myapplication.api.LoginService;
import com.example.tiantian.myapplication.data.login.User;
import com.zjy.simplemodule.base.BaseRepository;
import com.zjy.simplemodule.retrofit.AutoDisposeUtils;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultTransformer;
import com.zjy.simplemodule.retrofit.RetrofitUtils;

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
        RetrofitUtils.getInstance()
                .createService(LoginService.class)
                .login(username, password)
                .compose(new HttpResultTransformer<User>())
                .as(AutoDisposeUtils.<User>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void logoout(BaseSubscriber<Object> subscriber) {
        RetrofitUtils.getInstance()
                .createService(LoginService.class)
                .logout()
                .compose(new HttpResultTransformer<>())
                .as(AutoDisposeUtils.bind(getCurrActivity()))
                .subscribe(subscriber);
    }
}
