package com.example.tiantian.myapplication.utils;

import android.widget.TextView;

public class TextUtils {

    public static String isEmpty(String value, String def) {
        return value == null || value.length() == 0 ? def : value;
    }

    public static String isEmpty(String value) {
        return isEmpty(value, "");
    }

    public static String viewGetText(TextView textView) {
        return textView.getText().toString();
    }

}
