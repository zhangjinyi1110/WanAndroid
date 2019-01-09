package com.example.tiantian.myapplication.viewmodel.main;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.data.main.BannerData;
import com.example.tiantian.myapplication.data.main.NaviData;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.example.tiantian.myapplication.repository.main.MainRepository;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultException;

import java.util.List;

public class MainViewModel extends BaseViewModel<MainRepository> {

    private MutableLiveData<List<BannerData>> bannerList;
    private MutableLiveData<List<Chapters>> chapterList;
    private MutableLiveData<List<NaviData>> naviList;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<BannerData>> getBannerList() {
        if (bannerList == null)
            bannerList = new MutableLiveData<>();
        return bannerList;
    }

    public void getHomeBannerList() {
        repository.getBannerList(new BaseSubscriber<List<BannerData>>() {
            @Override
            public void onSuccess(List<BannerData> bannerData) {
                bannerList.setValue(bannerData);
            }

            @Override
            public void onFailure(HttpResultException exception) {

            }
        });
    }

    public MutableLiveData<List<Chapters>> getChapterList() {
        if (chapterList == null)
            chapterList = new MutableLiveData<>();
        return chapterList;
    }

    public void getHomeChapter() {
        repository.getHomeSystemList(new BaseSubscriber<List<Chapters>>() {
            @Override
            public void onSuccess(List<Chapters> chapters) {
                chapterList.setValue(chapters);
            }

            @Override
            public void onFailure(HttpResultException exception) {

            }
        });
    }

    public MutableLiveData<List<NaviData>> getNaviList() {
        if (naviList == null)
            naviList = new MutableLiveData<>();
        return naviList;
    }

    public void getHomeNavi() {
        repository.getHomeNavigationList(new BaseSubscriber<List<NaviData>>() {
            @Override
            public void onSuccess(List<NaviData> naviData) {
                naviList.setValue(naviData);
            }

            @Override
            public void onFailure(HttpResultException exception) {

            }
        });
    }
}
