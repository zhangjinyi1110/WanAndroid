package com.zjy.simplemodule.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class EnvironmentUtils {

    public static File getFileDir(Context context, String type) {
        File dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            dir = context.getExternalFilesDir(type);
        } else {
            dir = context.getFilesDir();
        }
        return dir;
    }

    public static File getCacheDir(Context context) {
        File dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            dir = context.getExternalCacheDir();
        } else {
            dir = context.getCacheDir();
        }
        return dir;
    }

}
