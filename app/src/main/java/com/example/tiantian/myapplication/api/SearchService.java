package com.example.tiantian.myapplication.api;

import com.example.tiantian.myapplication.data.search.SearchHot;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.zjy.simplemodule.retrofit.HttpResult;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SearchService {

    @GET("hotkey/json")
    Flowable<HttpResult<List<SearchHot>>> getSearchHot();

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    Flowable<HttpResult<Article>> search(@Path("page") int page, @Field("k") String k);

}
