package com.zjy.simplemodule.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesUtils {

    private static String FILE_NAME = "share_file";
    private SharedPreferences sharedPreferences;

    private SharedPreferencesUtils(Context context) {
        this(context, FILE_NAME);
    }

    private SharedPreferencesUtils(Context context, String fileName) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtils with(Context context) {
        return new SharedPreferencesUtils(context);
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public Set<String> getStringSet(String key) {
        return getStringSet(key, new HashSet<String>());
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return sharedPreferences.getStringSet(key, defValue);
    }

    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    public SharedPreferencesUtils put(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
        return this;
    }

    public SharedPreferencesUtils put(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
        return this;
    }

    public SharedPreferencesUtils put(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
        return this;
    }

    public SharedPreferencesUtils put(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
        return this;
    }

    public SharedPreferencesUtils put(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
        return this;
    }

    public SharedPreferencesUtils put(String key, Set<String> value) {
        sharedPreferences.edit().putStringSet(key, value).apply();
        return this;
    }

}
