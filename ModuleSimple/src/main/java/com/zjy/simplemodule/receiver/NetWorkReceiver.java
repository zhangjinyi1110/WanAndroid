package com.zjy.simplemodule.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.zjy.simplemodule.base.activity.BaseActivity;
import com.zjy.simplemodule.utils.ActivityManager;
import com.zjy.simplemodule.utils.NetWorkUtils;

import java.util.List;

public class NetWorkReceiver extends BroadcastReceiver {

//    private final String TAG = "tag---" + getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean flag = NetWorkUtils.isNetworkAvailable(context);
        List<FragmentActivity> activities = ActivityManager.getInstance().getActivities();
        for (FragmentActivity a : activities) {
            if (a instanceof BaseActivity) {
                if (!flag)
                    ((BaseActivity) a).showNotNetView();
                else
                    ((BaseActivity) a).hideNotNetView();
            }
        }
    }
}
