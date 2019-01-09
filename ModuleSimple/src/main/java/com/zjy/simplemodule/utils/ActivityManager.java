package com.zjy.simplemodule.utils;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    private static ActivityManager manager;
    private List<FragmentActivity> activities;

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

    private ActivityManager() {
        activities = new ArrayList<>();
    }

    public ActivityManager addActivity(FragmentActivity activity) {
        activities.add(activity);
        return manager;
    }

    public ActivityManager removeActivity(FragmentActivity activity) {
        activities.remove(activity);
        return manager;
    }

    public FragmentActivity getCurrActivity() {
        return activities.get(activities.size() - 1);
    }

    public ActivityManager clear() {
        for (FragmentActivity a : activities) {
            a.finish();
        }
        return manager;
    }

}
