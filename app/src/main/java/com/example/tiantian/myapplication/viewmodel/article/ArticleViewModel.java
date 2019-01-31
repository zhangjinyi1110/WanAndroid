package com.example.tiantian.myapplication.viewmodel.article;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.base.Contracts;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.data.wxarticle.ArticleData;
import com.example.tiantian.myapplication.repository.article.ArticleRepository;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultException;
import com.zjy.simplemodule.utils.Callback;
import com.zjy.simplemodule.utils.ToastUtils;

import java.util.List;

import static com.example.tiantian.myapplication.fragment.ArticleFragment.NOT_CID;

public class ArticleViewModel extends BaseViewModel<ArticleRepository> {

    private MutableLiveData<Article> articleData;
    private MutableLiveData<Article> searchResult;
    private MutableLiveData<Article> collectList;
    private MutableLiveData<Article> updateList;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Article> getArticleData() {
        if (articleData == null)
            articleData = repository.getArticleData();
        return articleData;
    }

    public void getArticle(final int page, final int cid) {
        if (cid == NOT_CID) {
            getArticle(page);
            return;
        }
        repository.getArticle(page, cid, new BaseSubscriber<Article>() {
            @Override
            public void onSuccess(Article article) {
                articleData.setValue(article);
//                DiskCache.with(repository.getCurrActivity()).saveAndClose(Contracts.ARTICLE_CACHE + "/" + cid, article);
//                if (page == 0) {
//                    repository.getArticleCache(cid);
//                }
            }

            @Override
            public void onFailure(HttpResultException exception) {

            }
        });
    }

    private void getArticle(final int page) {
        repository.getArticle(page, new BaseSubscriber<Article>() {
            @Override
            public void onSuccess(Article article) {
                articleData.setValue(article);
//                DiskCache.with(repository.getCurrActivity()).saveAndClose(Contracts.ARTICLE_CACHE + "/" + NOT_CID, article);
            }

            @Override
            public void onFailure(HttpResultException exception) {
                ToastUtils.showToastShort(repository.getCurrActivity(), exception.getThrowable().toString());
//                if (page == 0) {
//                    repository.getArticleCache(NOT_CID);
//                }
            }
        });
    }

    public MutableLiveData<Article> getUpdateList() {
        if (updateList == null)
            updateList = new MutableLiveData<>();
        return updateList;
    }

    @SuppressLint("CheckResult")
    public void updateArticle(final int count, final int page, final List<ArticleData> list) {
        repository.getArticle(page, new BaseSubscriber<Article>() {
            @Override
            public void onSuccess(Article article) {
                list.addAll(article.getDatas());
                if (page < count) {
                    updateArticle(count, page + 1, list);
                } else {
                    article.setDatas(list);
                    updateList.setValue(article);
                }
            }

            @Override
            public void onFailure(HttpResultException exception) {
                updateArticle(count, page, list);
            }
        });
    }

    public MutableLiveData<Article> getSearchResult() {
        if (searchResult == null)
            searchResult = new MutableLiveData<>();
        return searchResult;
    }

    public void search(final int page, final String k) {
        repository.search(page, k, new BaseSubscriber<Article>() {
            @Override
            public void onSuccess(Article article) {
                searchResult.setValue(article);
            }

            @Override
            public void onFailure(HttpResultException exception) {

            }
        });
    }

    public MutableLiveData<Article> getCollectList() {
        if (collectList == null)
            collectList = repository.getCollectList();
        return collectList;
    }

    public void getCollectList(final int page) {
        repository.getCollectList(page, new BaseSubscriber<Article>() {
            @Override
            public void onSuccess(Article article) {
                collectList.setValue(article);
//                DiskCache.with(repository.getCurrActivity()).saveAndClose(Contracts.ARTICLE_CACHE + "/" + COLLECT, article);
            }

            @Override
            public void onFailure(HttpResultException exception) {
                if (exception.getErrorType() == -1001) {
                    collectList.setValue(null);
                }
            }

            @Override
            public void onEmpty() {
                collectList.setValue(null);
            }
        });
    }

    public void collect(final ArticleData data, final Callback<Boolean> callback) {
        repository.collect(data.getId(), new BaseSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFailure(HttpResultException exception) {
//                ToastUtils.showToastShort(repository.getCurrActivity(), exception.getErrorMessage());
            }

            @Override
            public void onEmpty() {
                callback.onCall(true);
                ToastUtils.showToastShort(repository.getCurrActivity(), "收藏成功");
                repository.getCurrActivity().sendBroadcast(new Intent(Contracts.ARTICLE_COLLECT)
                        .putExtra(Contracts.ARTICLE_COLLECT_ACTION, Contracts.ARTICLE_COLLECT_YES)
                        .putExtra(Contracts.ARTICLE_COLLECT_ID, data.getId()));
            }
        });
    }

    public void uncollect(final int id, final Callback<Boolean> callback) {
        repository.uncollect(id, new BaseSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFailure(HttpResultException exception) {

            }

            @Override
            public void onEmpty() {
                callback.onCall(true);
                ToastUtils.showToastShort(repository.getCurrActivity(), "已取消收藏");
                repository.getCurrActivity().sendBroadcast(new Intent(Contracts.ARTICLE_COLLECT)
                        .putExtra(Contracts.ARTICLE_COLLECT_ACTION, Contracts.ARTICLE_COLLECT_CANCEL)
                        .putExtra(Contracts.ARTICLE_COLLECT_ID, id));
            }
        });
    }
}
