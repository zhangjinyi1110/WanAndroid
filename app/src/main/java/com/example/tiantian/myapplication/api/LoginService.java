package com.example.tiantian.myapplication.api;

import com.example.tiantian.myapplication.data.login.User;
import com.zjy.simplemodule.retrofit.HttpResult;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {

    @FormUrlEncoded
    @POST("user/register")
    Flowable<HttpResult<User>> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);

    @FormUrlEncoded
    @POST("user/login")
    Flowable<HttpResult<User>> login(@Field("username") String username, @Field("password") String password);

    @GET("user/logout/json")
    Flowable<HttpResult<Object>> logout();

}
