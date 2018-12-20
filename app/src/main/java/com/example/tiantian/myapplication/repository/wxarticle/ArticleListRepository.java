package com.example.tiantian.myapplication.repository.wxarticle;

import android.arch.lifecycle.MutableLiveData;

import com.example.tiantian.myapplication.api.WXArticleService;
import com.example.tiantian.myapplication.base.BaseRepository;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.flowable.HttpResultTransformer;
import com.example.tiantian.myapplication.utils.HttpSubscriber;
import com.example.tiantian.myapplication.utils.RxLifecyclerUtils;

public class ArticleListRepository extends BaseRepository {

    private MutableLiveData<Article> article;

    public MutableLiveData<Article> getArticle(String id, int page) {
        if(article==null){
            article = new MutableLiveData<>();
        }
        getService(WXArticleService.class)
                .getArticle(id, page)
                .compose(new HttpResultTransformer<Article>())
                .as(RxLifecyclerUtils.<Article>bind(getCurr()))
                .subscribe(new HttpSubscriber<Article>() {
                    @Override
                    public void success(Article article) {
                        ArticleListRepository.this.article.setValue(article);
                    }
                });
        return article;
    }

    public MutableLiveData<Article> getArticle(int page) {
        if(article==null){
            article = new MutableLiveData<>();
        }
        getService(WXArticleService.class)
                .getHomeArticle(page)
                .compose(new HttpResultTransformer<Article>())
                .as(RxLifecyclerUtils.<Article>bind(getCurr()))
                .subscribe(new HttpSubscriber<Article>() {
                    @Override
                    public void success(Article article) {
                        ArticleListRepository.this.article.setValue(article);
                    }
                });
        return article;
    }
}
