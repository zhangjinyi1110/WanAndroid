package com.example.tiantian.myapplication.viewmodel.wxarticle;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.repository.wxarticle.ArticleListRepository;

public class ArticleListViewModel extends BaseViewModel<ArticleListRepository> {

    public ArticleListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Article> getArticle(String id, int page) {
        return repository.getArticle(id, page);
    }

    public MutableLiveData<Article> getHomeArticle(int page) {
        return repository.getArticle(page);
    }

}
