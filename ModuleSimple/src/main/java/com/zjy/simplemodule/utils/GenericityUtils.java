package com.zjy.simplemodule.utils;

import java.lang.reflect.ParameterizedType;

public class GenericityUtils {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericity(Class self, int i) {
        ParameterizedType type = (ParameterizedType) self.getGenericSuperclass();
        if (type != null) {
            try {
                return (Class<T>) type.getActualTypeArguments()[i];
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> Class<T> getGenericity(Class self) {
        return getGenericity(self, 0);
    }

}
