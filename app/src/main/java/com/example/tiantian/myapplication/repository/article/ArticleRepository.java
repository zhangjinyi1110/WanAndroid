package com.example.tiantian.myapplication.repository.article;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.tiantian.myapplication.api.ArticleService;
import com.example.tiantian.myapplication.api.CollectService;
import com.example.tiantian.myapplication.api.SearchService;
import com.example.tiantian.myapplication.base.Contracts;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.zjy.simplemodule.base.BaseRepository;
import com.zjy.simplemodule.retrofit.AutoDisposeUtils;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultTransformer;
import com.zjy.simplemodule.retrofit.RetrofitUtils;
import com.zjy.simplemodule.utils.DiskCache;

import static com.example.tiantian.myapplication.fragment.ArticleFragment.COLLECT;
import static com.example.tiantian.myapplication.fragment.ArticleFragment.NOT_CID;

public class ArticleRepository extends BaseRepository {

    private MutableLiveData<Article> articleData;
    private MutableLiveData<Article> collectList;

    @Override
    protected void init() {

    }

    public MutableLiveData<Article> getCollectList() {
        if (collectList == null)
            collectList = new MutableLiveData<>();
        return collectList;
    }

    public MutableLiveData<Article> getArticleData() {
        if (articleData == null)
            articleData = new MutableLiveData<>();
        return articleData;
    }

    public void getArticle(int page, int cid, BaseSubscriber<Article> subscriber) {
        RetrofitUtils.getInstance()
                .createService(ArticleService.class)
                .getArticle(page, cid)
                .compose(new HttpResultTransformer<Article>())
                .as(AutoDisposeUtils.<Article>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void getArticleCache(int cid) {
        Article article = DiskCache.with(getCurrActivity()).getAndClose(Contracts.ARTICLE_CACHE + "/" + cid);
        if (article != null)
            articleData.setValue(article);
    }

    public void getArticle(int page, BaseSubscriber<Article> subscriber) {
        RetrofitUtils.getInstance()
                .createService(ArticleService.class)
                .getArticle(page)
                .compose(new HttpResultTransformer<Article>())
                .as(AutoDisposeUtils.<Article>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void search(int page, String k, BaseSubscriber<Article> subscriber) {
        RetrofitUtils.getInstance()
                .createService(SearchService.class)
                .search(page, k)
                .compose(new HttpResultTransformer<Article>())
                .as(AutoDisposeUtils.<Article>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void collect(int id, BaseSubscriber<Object> subscriber) {
        RetrofitUtils.getInstance()
                .createService(CollectService.class)
                .collect(id)
                .compose(new HttpResultTransformer<>())
                .as(AutoDisposeUtils.bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void getCollectList(int page, BaseSubscriber<Article> subscriber) {
        RetrofitUtils.getInstance()
                .createService(CollectService.class)
                .getCollect(page)
                .compose(new HttpResultTransformer<Article>())
                .as(AutoDisposeUtils.<Article>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void getCollectCache() {
        Article article = DiskCache.with(getCurrActivity()).getAndClose(Contracts.ARTICLE_CACHE + "/" + COLLECT);
        if (article != null)
            collectList.setValue(article);
    }

    public void uncollect(int id, BaseSubscriber<Object> subscriber) {
        RetrofitUtils.getInstance()
                .createService(CollectService.class)
                .uncollect(id)
                .compose(new HttpResultTransformer<>())
                .as(AutoDisposeUtils.bind(getCurrActivity()))
                .subscribe(subscriber);
    }

}
