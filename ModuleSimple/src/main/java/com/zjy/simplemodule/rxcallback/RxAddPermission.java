package com.zjy.simplemodule.rxcallback;

import java.util.ArrayList;

public class RxAddPermission {

    private RxFragment fragment;
    private boolean filter;
    private ArrayList<String> permissions;

    RxAddPermission(RxFragment fragment, boolean filter) {
        this.fragment = fragment;
        this.filter = filter;
        permissions = new ArrayList<>();
    }

    public RxAddPermission addPermission(String permission) {
        permissions.add(permission);
        return this;
    }

    public RxPermissionSimple simple() {
        return new RxPermissionSimple(fragment, permissions.toArray(new String[0]), filter);
    }

    public RxPermissionResult result() {
        return new RxPermissionResult(fragment, permissions.toArray(new String[0]), filter);
    }

}
