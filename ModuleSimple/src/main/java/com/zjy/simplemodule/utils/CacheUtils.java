package com.zjy.simplemodule.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.zjy.simplemodule.base.BaseApplication;

public class CacheUtils {

    private static CacheUtils cacheUtils;
    private LruCache<String, Object> lruCache;

    public static void init(BaseApplication application) {
        application.setCacheUtils(getInstance());
    }

    private CacheUtils() {
        final int SIZE = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
        lruCache = new LruCache<String, Object>(SIZE) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Object value) {
                if (value instanceof Bitmap) {
                    Bitmap bitmap = (Bitmap) value;
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                }
                return super.sizeOf(key, value);
            }
        };
    }

    public static CacheUtils getInstance() {
        if (cacheUtils == null) {
            synchronized (CacheUtils.class) {
                if (cacheUtils == null) {
                    cacheUtils = new CacheUtils();
                }
            }
        }
        return cacheUtils;
    }

    public Object put(String key, Object value) {
        return lruCache.put(key, value);
    }

    public Object get(String key) {
        return lruCache.get(key);
    }

    public void resize(int maxSize) {
        lruCache.resize(maxSize);
    }

    public void trimToSize(int maxSize) {
        lruCache.trimToSize(maxSize);
    }

    public Object remove(String key) {
        return lruCache.remove(key);
    }

}
