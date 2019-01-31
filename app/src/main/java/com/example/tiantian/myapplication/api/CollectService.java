package com.example.tiantian.myapplication.api;

import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.zjy.simplemodule.retrofit.HttpResult;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CollectService {

    @GET("lg/collect/list/{page}/json")
    Flowable<HttpResult<Article>> getCollect(@Path("page") int page);

//    @FormUrlEncoded
    @POST("lg/collect/{id}/json")
    Flowable<HttpResult<Object>> collect(@Path("id") int id);

    @POST("lg/uncollect_originId/{id}/json")
    Flowable<HttpResult<Object>> uncollect(@Path("id") int id);

}
