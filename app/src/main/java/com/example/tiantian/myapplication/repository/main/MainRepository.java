package com.example.tiantian.myapplication.repository.main;

import com.example.tiantian.myapplication.api.MainService;
import com.example.tiantian.myapplication.data.main.BannerData;
import com.example.tiantian.myapplication.data.main.NaviData;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.zjy.simplemodule.base.BaseRepository;
import com.zjy.simplemodule.retrofit.AutoDisposeUtils;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultTransformer;
import com.zjy.simplemodule.retrofit.RetrofitUtils;

import java.util.List;

public class MainRepository extends BaseRepository {

    @Override
    protected void init() {

    }

    public void getBannerList(BaseSubscriber<List<BannerData>> subscriber) {
        RetrofitUtils.getInstance()
                .createService(MainService.class)
                .getBanner()
                .compose(new HttpResultTransformer<List<BannerData>>())
                .as(AutoDisposeUtils.<List<BannerData>>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void getHomeSystemList(BaseSubscriber<List<Chapters>> subscriber) {
        RetrofitUtils.getInstance()
                .createService(MainService.class)
                .getTree()
                .compose(new HttpResultTransformer<List<Chapters>>())
                .as(AutoDisposeUtils.<List<Chapters>>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void getHomeNavigationList(BaseSubscriber<List<NaviData>> subscriber) {
        RetrofitUtils.getInstance()
                .createService(MainService.class)
                .getNavigation()
                .compose(new HttpResultTransformer<List<NaviData>>())
                .as(AutoDisposeUtils.<List<NaviData>>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

}
