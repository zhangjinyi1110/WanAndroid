package com.zjy.simplemodule.rxcallback;

public class Permission {

    private boolean granted;
    private String permission;

    public Permission(boolean granted, String permission) {
        this.granted = granted;
        this.permission = permission;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
