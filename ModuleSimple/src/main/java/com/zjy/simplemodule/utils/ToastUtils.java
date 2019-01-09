package com.zjy.simplemodule.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showToastShort(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String message, int time) {
        Toast.makeText(context, message, time).show();
    }

}
