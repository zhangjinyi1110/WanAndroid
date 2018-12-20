package com.example.tiantian.myapplication.utils;

import android.app.Activity;

import com.example.tiantian.myapplication.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    private List<BaseActivity> activities;

    private static ActivityManager manager;

    public static ActivityManager getInstance() {
        if (manager == null) {
            synchronized (ActivityManager.class) {
                if (manager == null) {
                    manager = new ActivityManager();
                }
            }
        }
        return manager;
    }

    public void init() {
        activities = new ArrayList<>();
    }

    public void add(BaseActivity activity) {
        activities.add(activity);
    }

    public void remove(BaseActivity activity) {
        activities.remove(activity);
    }

    public BaseActivity getCurr() {
        if (activities.size() == 0) {
            return null;
        }
        return activities.get(activities.size() - 1);
    }

}
