package com.example.tiantian.myapplication.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;

public class BaseViewModel<R extends BaseRepository> extends AndroidViewModel {

    protected R repository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        repository = getRepository();
        if (repository != null)
            repository.with(application);
    }

    @SuppressWarnings("unchecked")
    private R getRepository() {
        try {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<R> rClass = (Class<R>) type.getActualTypeArguments()[0];
            return rClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <S> S getService(Class<S> sClass) {
        return repository.getService(sClass);
    }
}
