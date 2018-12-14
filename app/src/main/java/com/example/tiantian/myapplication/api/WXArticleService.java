package com.example.tiantian.myapplication.api;

import com.example.tiantian.myapplication.data.HttpResult;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WXArticleService {

    @GET("wxarticle/chapters/json")
    Flowable<HttpResult<List<Chapters>>> getChapters();

    @GET("wxarticle/list/{id}/{page}/json")
    Flowable<HttpResult<Article>> getArticle(@Path("id") int id, @Path("page") int page);

}
