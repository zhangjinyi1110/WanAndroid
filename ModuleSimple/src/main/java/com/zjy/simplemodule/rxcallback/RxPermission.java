package com.zjy.simplemodule.rxcallback;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.zjy.simplemodule.utils.ActivityManager;

public class RxPermission {

    private RxFragment fragment;
    private FragmentManager manager;
    private boolean isWith = false;
    private final String TAG = getClass().getSimpleName();
    private static RxPermission rxPermission;
    private boolean filter;//是否过滤以允许权限

    public static RxPermission getInstance() {
        if (rxPermission == null) {
            synchronized (RxPermission.class) {
                if (rxPermission == null) {
                    rxPermission = new RxPermission();
                }
            }
        }
        return rxPermission;
    }

    public RxPermission with(Fragment fragment) {
        manager = fragment.getChildFragmentManager();
        init(manager);
        return rxPermission;
    }

    public RxPermission with(FragmentActivity activity) {
        manager = activity.getSupportFragmentManager();
        init(manager);
        return rxPermission;
    }

    private void init(FragmentManager manager) {
        if (fragment == null || fragment.getActivity() == null)
            fragment = getFragment(manager);
        filter = false;
        isWith = true;
    }

    private RxFragment getFragment(FragmentManager manager) {
        RxFragment fragment = findFragment(manager);
        if (fragment == null) {
            fragment = new RxFragment();
            manager.beginTransaction()
                    .add(fragment, TAG)
                    .commitNow();
        }
        return fragment;
    }

    private RxFragment findFragment(FragmentManager manager) {
        return (RxFragment) manager.findFragmentByTag(TAG);
    }

    public RxAddPermission addPermission(String permission) {
        check();
        return new RxAddPermission(fragment, filter).addPermission(permission);
    }

    public RxPermissionSimple requestForSimple(String permission) {
        return requestForSimple(new String[]{permission});
    }

    public RxPermissionResult requestForResult(String permission) {
        return requestForResult(new String[]{permission});
    }

    public RxPermissionSimple requestForSimple(String[] permissions) {
        check();
        return new RxPermissionSimple(fragment, permissions, filter);
    }

    public RxPermissionResult requestForResult(String[] permissions) {
        check();
        return new RxPermissionResult(fragment, permissions, filter);
    }

    //过滤以允许的权限
    public RxPermission filter() {
        filter = true;
        return rxPermission;
    }

    private void check() {
        if (!isWith) {
            manager = ActivityManager.getInstance().getCurrActivity().getSupportFragmentManager();
            init(manager);
        }
        isWith = false;
    }

}
