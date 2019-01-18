package com.zjy.simplemodule.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.zjy.simplemodule.utils.GenericityUtils;

import io.reactivex.disposables.Disposable;

public class BaseViewModel<R extends BaseRepository> extends AndroidViewModel {

    protected R repository;
    protected final String TAG = this.getClass().getSimpleName();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        repository = getRepository();
    }

    @SuppressWarnings("unchecked")
    private R getRepository() {
        Class<R> rClass = GenericityUtils.getGenericity(getClass());
        try {
            if (rClass != null) {
                R r = rClass.newInstance();
                r.with(getApplication());
                return r;
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
//        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
//        try {
//            Class<R> rClass;
//            if (type != null) {
//                rClass = (Class<R>) type.getActualTypeArguments()[0];
//                R r = rClass.newInstance();
//                r.with(getApplication());
//                return r;
//            }
//        } catch (IllegalAccessException | InstantiationException | NullPointerException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public void onCleared() {
        super.onCleared();
        repository.close();
    }
}
