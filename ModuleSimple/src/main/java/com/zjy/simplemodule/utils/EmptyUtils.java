package com.zjy.simplemodule.utils;

public class EmptyUtils {

    public static <T> boolean tIsEmpty(T t) {
        return t == null;
    }

    public static <T> boolean tUnEmpty(T t) {
        return t != null;
    }

}
