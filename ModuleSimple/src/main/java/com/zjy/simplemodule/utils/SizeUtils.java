package com.zjy.simplemodule.utils;

import android.content.Context;

public class SizeUtils {

    public static int dp2px(Context context, int value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * value + 0.5f);
    }

    public static int sp2px(Context context, int value) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scale * value + 0.5f);
    }

    public static int px2dp(Context context, int value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale / value + 0.5f);
    }

    public static int px2sp(Context context, int value) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scale / value + 0.5f);
    }

}
