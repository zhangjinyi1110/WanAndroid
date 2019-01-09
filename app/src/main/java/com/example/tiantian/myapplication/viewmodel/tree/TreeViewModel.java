package com.example.tiantian.myapplication.viewmodel.tree;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.tiantian.myapplication.repository.tree.TreeRepository;
import com.zjy.simplemodule.base.BaseViewModel;

public class TreeViewModel extends BaseViewModel<TreeRepository> {

    public TreeViewModel(@NonNull Application application) {
        super(application);
    }
}
