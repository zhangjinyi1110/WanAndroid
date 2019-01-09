package com.example.tiantian.myapplication.api;

import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.zjy.simplemodule.retrofit.HttpResult;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {

    @GET("article/list/{page}/json")
    Flowable<HttpResult<Article>> getArticle(@Path("page") int page, @Query("cid") int cid);

    @GET("article/list/{page}/json")
    Flowable<HttpResult<Article>> getArticle(@Path("page") int page);

}
