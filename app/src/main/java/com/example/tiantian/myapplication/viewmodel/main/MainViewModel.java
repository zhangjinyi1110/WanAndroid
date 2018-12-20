package com.example.tiantian.myapplication.viewmodel.main;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.data.main.BannerData;
import com.example.tiantian.myapplication.data.main.CommonWeb;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.repository.main.MainRepository;

import java.util.List;

public class MainViewModel extends BaseViewModel<MainRepository> {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<BannerData>> getBannerList() {
        return repository.getBannerList();
    }

    public MutableLiveData<List<CommonWeb>> getCommonWebList() {
        return repository.getCommonWebList();
    }

    public MutableLiveData<Article> getHomeArticle(int page) {
        return repository.getHomeArticle(page);
    }
}
