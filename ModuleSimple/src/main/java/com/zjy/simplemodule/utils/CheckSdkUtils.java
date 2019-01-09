package com.zjy.simplemodule.utils;

import android.os.Build;

public class CheckSdkUtils {

    //判断是否小于等于参数sdkVersion
    public static boolean lessThen(int sdkVersion) {
        return Build.VERSION.SDK_INT <= sdkVersion;
    }

    //判断是否大于等于参数sdkVersion
    public static boolean greaterThan(int sdkVersion) {
        return Build.VERSION.SDK_INT >= sdkVersion;
    }

    //判断是否等于参数sdkVersion
    public static boolean equalTo(int sdkVersion) {
        return Build.VERSION.SDK_INT == sdkVersion;
    }

}
