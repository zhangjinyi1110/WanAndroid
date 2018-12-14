package com.example.tiantian.myapplication.utils;

import android.content.Context;

public class SizeUtils {

    public static int dp2px (Context context, float value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * value);
    }

    public static int sp2px (Context context, float value) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scale * value);
    }

}
