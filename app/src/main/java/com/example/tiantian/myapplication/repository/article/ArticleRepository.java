package com.example.tiantian.myapplication.repository.article;

import com.example.tiantian.myapplication.api.ArticleService;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.zjy.simplemodule.base.BaseRepository;
import com.zjy.simplemodule.retrofit.AutoDisposeUtils;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultTransformer;
import com.zjy.simplemodule.retrofit.RetrofitUtils;

public class ArticleRepository extends BaseRepository {
    @Override
    protected void init() {

    }

    public void getArticle(int page, int cid, BaseSubscriber<Article> subscriber) {
        RetrofitUtils.getInstance()
                .createService(ArticleService.class)
                .getArticle(page, cid)
                .compose(new HttpResultTransformer<Article>())
                .as(AutoDisposeUtils.<Article>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    public void getArticle(int page, BaseSubscriber<Article> subscriber) {
        RetrofitUtils.getInstance()
                .createService(ArticleService.class)
                .getArticle(page)
                .compose(new HttpResultTransformer<Article>())
                .as(AutoDisposeUtils.<Article>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

}
