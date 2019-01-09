package com.zjy.simplemodule.rxcallback;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.zjy.simplemodule.utils.ActivityManager;

public class RxAvoid {

    private RxFragment fragment;
    private FragmentManager manager;
    private boolean isWith = false;
    private final String TAG = getClass().getSimpleName();
    private static RxAvoid rxAvoid;

    public static RxAvoid getInstance() {
        if (rxAvoid == null) {
            synchronized (RxAvoid.class) {
                if (rxAvoid == null) {
                    rxAvoid = new RxAvoid();
                }
            }
        }
        return rxAvoid;
    }

    public RxAvoid with(Fragment fragment) {
        manager = fragment.getChildFragmentManager();
        init();
        return rxAvoid;
    }

    public RxAvoid with(FragmentActivity activity) {
        manager = activity.getSupportFragmentManager();
        init();
        return rxAvoid;
    }

    private void init() {
//        if (fragment == null || fragment.getActivity() == null)
        fragment = getFragment();
        isWith = true;
    }

    private RxFragment getFragment() {
        RxFragment fragment = findFragment();
        if (fragment == null) {
            fragment = new RxFragment();
            manager.beginTransaction()
                    .add(fragment, TAG)
                    .commitNow();
        }
        return fragment;
    }

    private RxFragment findFragment() {
        return (RxFragment) manager.findFragmentByTag(TAG);
    }

    public RxAvoidSimple simple(Intent intent) {
        check();
        return new RxAvoidSimple(fragment, intent);
    }

    public <A extends Activity> RxAvoidSimple simple(Class<A> aClass) {
        check();
        return new RxAvoidSimple(fragment, new Intent(fragment.getActivity(), aClass));
    }

    public RxAvoidResult result(Intent intent) {
        check();
        return new RxAvoidResult(fragment, intent);
    }

    public <A extends Activity> RxAvoidResult result(Class<A> aClass) {
        check();
        return new RxAvoidResult(fragment, new Intent(fragment.getActivity(), aClass));
    }

    private void check() {
        if (!isWith) {
            manager = ActivityManager.getInstance().getCurrActivity().getSupportFragmentManager();
            init();
        }
        isWith = false;
    }

}
