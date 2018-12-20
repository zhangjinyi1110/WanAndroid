package com.example.tiantian.myapplication.api;

import com.example.tiantian.myapplication.data.HttpResult;
import com.example.tiantian.myapplication.data.main.BannerData;
import com.example.tiantian.myapplication.data.main.CommonWeb;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface MainService {

    @GET("banner/json")
    Flowable<HttpResult<List<BannerData>>> getBanner();

    @GET("friend/json")
    Flowable<HttpResult<List<CommonWeb>>> getCommonWeb();

}
