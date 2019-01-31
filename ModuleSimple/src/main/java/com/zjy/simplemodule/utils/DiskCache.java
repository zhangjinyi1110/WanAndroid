package com.zjy.simplemodule.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;
import com.zjy.simplemodule.base.Contracts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DiskCache {

    private static DiskLruCache diskLruCache;
    private String cachePath;
    private String uniqueName;
//    private Context context;

    private DiskCache(Context context) {
//        this.context = context;
        init(context);
    }

    public static DiskCache with(Context context) {
        return new DiskCache(context);
    }

    private void init(Context context) {
        uniqueName = "";
        cachePath = getDiskCacheDir(context);
    }

    private String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    private DiskCache cachePath(String path) {
        uniqueName = path;
        return this;
    }

    private <T> T save(String key, T value) {
        open();
        DiskLruCache.Editor editor = null;
        try {
            editor = diskLruCache.edit(hashKeyForDisk(key));
            OutputStream stream = editor.newOutputStream(0);
            ObjectOutputStream outputStream = new ObjectOutputStream(stream);
            outputStream.writeObject(value);
            outputStream.flush();
            editor.commit();
        } catch (IOException e) {
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return value;
    }

    public <T> T saveAndClose(String key, T value) {
        open();
        T t = save(key, value);
        close();
        return t;
    }

    @SuppressWarnings("unchecked")
    private <T> T get(String key) {
        open();
        T t = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(hashKeyForDisk(key));
            if (snapshot != null) {
                InputStream stream = snapshot.getInputStream(0);
                ObjectInputStream inputStream = new ObjectInputStream(stream);
                try {
                    t = (T) inputStream.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public <T> T getAndClose(String key) {
        T t = get(key);
        close();
        return t;
    }

    public DiskCache remove(String key) {
        open();
        try {
            diskLruCache.remove(hashKeyForDisk(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void open() {
//        if (diskLruCache != null && !diskLruCache.isClosed()) {
//            return;
//        }
        File file = new File(cachePath + "/" + uniqueName);
        Log.e(getClass().getSimpleName(), "open: " + file.getAbsolutePath());
        try {
            if (!file.exists()) {
                file.exists();
            }
            diskLruCache = DiskLruCache.open(file, Contracts.DISK_VERSION, 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public long size() {
        open();
        long size = diskLruCache.size();
        close();
        return size;
    }

    public void clear() {
        open();
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    public String getCachePath() {
        return cachePath + "/" + uniqueName;
    }

    public static void close() {
        try {
            if (diskLruCache != null && !diskLruCache.isClosed()) {
                diskLruCache.close();
                diskLruCache = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
