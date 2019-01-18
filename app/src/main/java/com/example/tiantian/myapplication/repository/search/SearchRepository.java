package com.example.tiantian.myapplication.repository.search;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.example.tiantian.myapplication.activity.SearchResultActivity;
import com.example.tiantian.myapplication.api.SearchService;
import com.example.tiantian.myapplication.base.Contracts;
import com.example.tiantian.myapplication.data.search.SearchHot;
import com.example.tiantian.myapplication.rxjava.ListToStringFunction;
import com.example.tiantian.myapplication.rxjava.StringToListFunction;
import com.example.tiantian.myapplication.utils.TextUtils;
import com.zjy.simplemodule.base.BaseRepository;
import com.zjy.simplemodule.retrofit.AutoDisposeUtils;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultTransformer;
import com.zjy.simplemodule.retrofit.RetrofitUtils;
import com.zjy.simplemodule.utils.DiskCache;
import com.zjy.simplemodule.utils.KeyBroadUtils;
import com.zjy.simplemodule.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SearchRepository extends BaseRepository {
    @Override
    protected void init() {

    }

    public void getSearchHot(BaseSubscriber<List<SearchHot>> subscriber) {
        RetrofitUtils.getInstance()
                .createService(SearchService.class)
                .getSearchHot()
                .compose(new HttpResultTransformer<List<SearchHot>>())
                .as(AutoDisposeUtils.<List<SearchHot>>bind(getCurrActivity()))
                .subscribe(subscriber);
    }

    @SuppressLint("CheckResult")
    public void getSearchHistory(Consumer<ArrayList<String>> consumer) {
        String historyList = DiskCache.with(getCurrActivity()).cachePath(Contracts.SEARCH_HISTORY_PATH).get(Contracts.SEARCH_HISTORY);
        Observable.just(TextUtils.isEmpty(historyList))
                .map(new StringToListFunction())
                .subscribe(consumer);
    }

    public void historyClear() {
        DiskCache.with(getCurrActivity()).cachePath(Contracts.SEARCH_HISTORY_PATH).remove(Contracts.SEARCH_HISTORY);
    }

    @SuppressLint("CheckResult")
    public void historyDelete(final String name, Consumer<ArrayList<String>> consumer) {
        String history = DiskCache.with(getCurrActivity()).cachePath(Contracts.SEARCH_HISTORY_PATH).get(Contracts.SEARCH_HISTORY);
        Observable.just(TextUtils.isEmpty(history))
                .map(new StringToListFunction())
                .flatMap(new Function<ArrayList<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(ArrayList<String> strings) {
                        return Observable.fromIterable(strings);
                    }
                })
                .collect(new Callable<ArrayList<String>>() {
                             @Override
                             public ArrayList<String> call() {
                                 return new ArrayList<>();
                             }
                         }
                        , new BiConsumer<ArrayList<String>, String>() {
                            @Override
                            public void accept(ArrayList<String> strings, String s) {
                                if (!s.equals(name))
                                    strings.add(s);
                            }
                        })
                .doOnSuccess(new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> strings) {
                        Observable.just(strings)
                                .map(new ListToStringFunction())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) {
                                        DiskCache.with(getCurrActivity())
                                                .cachePath(Contracts.SEARCH_HISTORY_PATH)
                                                .save(Contracts.SEARCH_HISTORY, s);
                                    }
                                });
                    }
                })
                .subscribe(consumer);
    }

    @SuppressLint("CheckResult")
    public void historyUpdate(final String name, Consumer<ArrayList<String>> consumer) {
        String history = DiskCache.with(getCurrActivity()).cachePath(Contracts.SEARCH_HISTORY_PATH).get(Contracts.SEARCH_HISTORY);
        Observable.just(TextUtils.isEmpty(history))
                .map(new StringToListFunction())//string转化list
                .flatMap(new Function<ArrayList<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(ArrayList<String> strings) {
                        return Observable.fromIterable(strings);//逐个发送
                    }
                })
                .collect(new Callable<ArrayList<String>>() {//创建新的list
                             @Override
                             public ArrayList<String> call() {
                                 return new ArrayList<>();
                             }
                         }
                        , new BiConsumer<ArrayList<String>, String>() {//添加
                            @Override
                            public void accept(ArrayList<String> strings, String s) {
                                if (!s.equals(name)) {
                                    strings.add(s);
                                }
                            }
                        })
                .doOnSuccess(new Consumer<ArrayList<String>>() {//成功前的操作，将新的list转化成string传入磁盘缓存
                    @Override
                    public void accept(ArrayList<String> strings) {
                        strings.add(0, name);
                        Observable.just(strings)
                                .map(new ListToStringFunction())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) {
                                        DiskCache.with(getCurrActivity())
                                                .cachePath(Contracts.SEARCH_HISTORY_PATH)
                                                .save(Contracts.SEARCH_HISTORY, s);
                                    }
                                });
                    }
                })
                .subscribe(consumer);

    }

    public void search(String name) {
        if (android.text.TextUtils.isEmpty(name)) {
            ToastUtils.showToastShort(getCurrActivity(), "请输入索引词");
            return;
        }
        getCurrActivity().startActivity(new Intent(getCurrActivity(), SearchResultActivity.class)
                .putExtra("title", name)
                .putExtra("k", name));
        KeyBroadUtils.hide(getCurrActivity(), getCurrActivity().getCurrentFocus());
    }

    @Override
    public void close() {
        DiskCache.close();
    }
}
