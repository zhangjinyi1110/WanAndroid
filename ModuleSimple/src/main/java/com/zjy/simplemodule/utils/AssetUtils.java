package com.zjy.simplemodule.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AssetUtils {

    private static final String TAG = "assetUtils";

    @SuppressLint("CheckResult")
    public static void copyFile(final Context context, String filepath, final String filename, final Callback<Boolean> callback) {
        final File file = new File(filepath);
        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                Observable.just(context.getAssets().open(filename))
                        .subscribeOn(Schedulers.io())
                        .map(new Function<InputStream, byte[]>() {
                            @Override
                            public byte[] apply(InputStream inputStream) throws Exception {
                                byte[] bytes = new byte[inputStream.available()];
                                inputStream.read(bytes);
                                inputStream.close();
                                return bytes;
                            }
                        })
                        .map(new Function<byte[], Boolean>() {
                            @Override
                            public Boolean apply(byte[] bytes) throws Exception {
                                FileOutputStream outputStream = new FileOutputStream(file);
                                outputStream.write(bytes);
                                outputStream.close();
                                return true;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                                       @Override
                                       public void accept(Boolean aBoolean) throws Exception {
                                           callback.onCall(true);
                                       }
                                   }
                                , new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e(TAG, "accept: " + throwable.toString());
                                        callback.onCall(false);
                                    }
                                });
//                InputStream inputStream = context.getAssets().open(filename);
//                byte[] bytes = new byte[inputStream.available()];
//                inputStream.read(bytes);
//                context.getAssets().close();
//                FileOutputStream outputStream = new FileOutputStream(file);
//                outputStream.write(bytes);
//                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "copyFile: " + e.toString());
                callback.onCall(false);
            }
        } else {
            callback.onCall(true);
        }
    }

    public static AssetManager addAssetPath(String path) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getMethod("addAssetPath", String.class);
            method.invoke(assetManager, path);
            return assetManager;
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            Log.e(TAG, "getAssetManager: " + e.toString());
            return null;
        }
    }

}
