package com.example.tiantian.myapplication.repository.main;

import android.arch.lifecycle.MutableLiveData;

import com.example.tiantian.myapplication.api.MainService;
import com.example.tiantian.myapplication.api.WXArticleService;
import com.example.tiantian.myapplication.base.BaseRepository;
import com.example.tiantian.myapplication.data.main.BannerData;
import com.example.tiantian.myapplication.data.main.CommonWeb;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.flowable.HttpListResultTransformer;
import com.example.tiantian.myapplication.flowable.HttpResultTransformer;
import com.example.tiantian.myapplication.utils.ActivityManager;
import com.example.tiantian.myapplication.utils.HttpSubscriber;
import com.example.tiantian.myapplication.utils.RxLifecyclerUtils;

import java.util.List;

public class MainRepository extends BaseRepository {

    private MainService service;
    private MutableLiveData<List<BannerData>> bannerList;
    private MutableLiveData<List<CommonWeb>> commonWebList;
    private MutableLiveData<Article> homeArticle;

    public MainService getService() {
        if (service == null) {
            service = getService(MainService.class);
        }
        return service;
    }

    public MutableLiveData<List<BannerData>> getBannerList() {
        if(bannerList==null){
            bannerList = new MutableLiveData<>();
        }
        getBanner();
        return bannerList;
    }

    public MutableLiveData<List<CommonWeb>> getCommonWebList() {
        if(commonWebList==null){
            commonWebList = new MutableLiveData<>();
        }
        getService().getCommonWeb()
                .compose(new HttpListResultTransformer<CommonWeb>())
                .as(RxLifecyclerUtils.<List<CommonWeb>>bind(getCurr()))
                .subscribe(new HttpSubscriber<List<CommonWeb>>() {
                    @Override
                    public void success(List<CommonWeb> commonWebs) {
                        commonWebList.setValue(commonWebs);
                    }
                });
        return commonWebList;
    }

    public MutableLiveData<Article> getHomeArticle(int page) {
        if(homeArticle==null){
            homeArticle = new MutableLiveData<>();
        }
        getService(WXArticleService.class).getHomeArticle(page)
                .compose(new HttpResultTransformer<Article>())
                .as(RxLifecyclerUtils.<Article>bind(getCurr()))
                .subscribe(new HttpSubscriber<Article>() {
                    @Override
                    public void success(Article homeArticle) {
                        MainRepository.this.homeArticle.setValue(homeArticle);
                    }
                });
        return homeArticle;
    }

    private void getBanner() {
        getService().getBanner()
                .compose(new HttpListResultTransformer<BannerData>())
                .as(RxLifecyclerUtils.<List<BannerData>>bind(ActivityManager.getInstance().getCurr()))
                .subscribe(new HttpSubscriber<List<BannerData>>() {
                    @Override
                    public void success(List<BannerData> bannerData) {
                        bannerList.setValue(bannerData);
                    }
                });
    }
}
