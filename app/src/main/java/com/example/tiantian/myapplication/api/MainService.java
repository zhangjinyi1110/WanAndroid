package com.example.tiantian.myapplication.api;

import com.example.tiantian.myapplication.data.main.BannerData;
import com.example.tiantian.myapplication.data.main.NaviData;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.zjy.simplemodule.retrofit.HttpResult;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface MainService {

    @GET("banner/json")
    Flowable<HttpResult<List<BannerData>>> getBanner();

    @GET("tree/json")
    Flowable<HttpResult<List<Chapters>>> getTree();

    @GET("navi/json")
    Flowable<HttpResult<List<NaviData>>> getNavigation();

}
