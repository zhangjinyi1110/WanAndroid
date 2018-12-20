package com.example.tiantian.myapplication.repository.wxarticle;

import android.arch.lifecycle.MutableLiveData;

import com.example.tiantian.myapplication.api.WXArticleService;
import com.example.tiantian.myapplication.base.BaseRepository;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.example.tiantian.myapplication.flowable.HttpListResultTransformer;
import com.example.tiantian.myapplication.utils.ActivityManager;
import com.example.tiantian.myapplication.utils.HttpSubscriber;
import com.example.tiantian.myapplication.utils.RxLifecyclerUtils;

import java.util.List;

public class ChapterRepository extends BaseRepository {

    private MutableLiveData<List<Chapters>> chapterList;

    public MutableLiveData<List<Chapters>> getChapterList() {
        if (chapterList == null) {
            chapterList = new MutableLiveData<>();
        }
        getService(WXArticleService.class)
                .getChapters()
                .compose(new HttpListResultTransformer<Chapters>())
                .as(RxLifecyclerUtils.<List<Chapters>>bind(ActivityManager.getInstance().getCurr()))
                .subscribe(new HttpSubscriber<List<Chapters>>() {
                    @Override
                    public void success(List<Chapters> chapters) {
                        chapterList.setValue(chapters);
                    }
                });
        return chapterList;
    }

}
