package com.example.tiantian.myapplication.viewmodel.wxarticle;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.repository.wxarticle.ChapterRepository;
import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;

import java.util.List;

public class ChapterViewModel extends BaseViewModel<ChapterRepository> {

    public ChapterViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Chapters>> getChapterList() {
        return repository.getChapterList();
    }

}
