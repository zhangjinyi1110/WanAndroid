package com.example.tiantian.myapplication.viewmodel.search;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.activity.search.SearchActivity;
import com.example.tiantian.myapplication.data.search.SearchHot;
import com.example.tiantian.myapplication.repository.search.SearchRepository;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class SearchViewModel extends BaseViewModel<SearchRepository> {

    private MutableLiveData<List<SearchHot>> hotList;
    private MutableLiveData<List<String>> historyList;

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<SearchHot>> getHotList() {
        if (hotList == null)
            hotList = new MutableLiveData<>();
        return hotList;
    }

    public void getSearchHot() {
        repository.getSearchHot(new BaseSubscriber<List<SearchHot>>() {
            @Override
            public void onSuccess(List<SearchHot> searchHots) {
                hotList.setValue(searchHots);
            }

            @Override
            public void onFailure(HttpResultException exception) {

            }
        });
    }

    public MutableLiveData<List<String>> getHistoryList() {
        if (historyList == null)
            historyList = new MutableLiveData<>();
        return historyList;
    }

    public void getHistory() {
        repository.getSearchHistory(new Consumer<ArrayList<String>>() {
            @Override
            public void accept(ArrayList<String> strings) {
                historyList.setValue(strings);
            }
        });
    }

    public void updateHistory(String name, int type) {
        switch (type) {
            case SearchActivity.TYPE_DELETE:
                repository.historyDelete(name, new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> strings) {
                        historyList.setValue(strings);
                    }
                });
                break;
            case SearchActivity.TYPE_CHANGE:
                repository.historyUpdate(name, new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> strings) {
                        historyList.setValue(strings);
                    }
                });
                break;
            case SearchActivity.TYPE_CLEAR:
                repository.historyClear();
                historyList.setValue(new ArrayList<String>());
                break;
        }
    }

    public void search(String name) {
        updateHistory(name, SearchActivity.TYPE_CHANGE);
        repository.search(name);
    }
}
