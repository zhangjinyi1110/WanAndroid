package com.zjy.simplemodule.retrofit;

import android.arch.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

public class AutoDisposeUtils {

    public static <T> AutoDisposeConverter<T> bind(LifecycleOwner owner){
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner));
    }

}
