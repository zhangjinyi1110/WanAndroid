package com.example.tiantian.myapplication.base;

import android.content.Context;

import com.example.tiantian.myapplication.utils.ActivityManager;
import com.example.tiantian.myapplication.utils.RetrofitHelper;

public class BaseRepository {

    private Context context;

    public void with(Context context) {
        this.context = context;
    }

    public <S> S getService(Class<S> sClass) {
        return RetrofitHelper.getInstance()
                .createService(sClass);
    }

    protected BaseActivity getCurr() {
        return ActivityManager.getInstance().getCurr();
    }
}
