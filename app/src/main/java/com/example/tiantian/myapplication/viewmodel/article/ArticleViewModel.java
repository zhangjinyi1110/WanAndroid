package com.example.tiantian.myapplication.viewmodel.article;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.repository.article.ArticleRepository;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultException;

import static com.example.tiantian.myapplication.fragment.ArticleFragment.NOT_CID;

public class ArticleViewModel extends BaseViewModel<ArticleRepository> {

    private MutableLiveData<Article> articleData;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Article> getArticleData() {
        if (articleData == null)
            articleData = new MutableLiveData<>();
        return articleData;
    }

    public void getArticle(int page, int cid) {
        if (cid == NOT_CID) {
            getArticle(page);
            return;
        }
        repository.getArticle(page, cid, new BaseSubscriber<Article>() {
            @Override
            public void onSuccess(Article article) {
                articleData.setValue(article);
            }

            @Override
            public void onFailure(HttpResultException exception) {

            }
        });
    }

    private void getArticle(int page) {
        repository.getArticle(page, new BaseSubscriber<Article>() {
            @Override
            public void onSuccess(Article article) {
                articleData.setValue(article);
            }

            @Override
            public void onFailure(HttpResultException exception) {

            }
        });
    }

}
