package com.example.tiantian.myapplication.fragment.main;

import android.os.Bundle;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.databinding.FragmentProjectBinding;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;

public class ProjectFragment extends AbsBindingFragment<BaseViewModel, FragmentProjectBinding> {

    public static ProjectFragment newInstance() {
        return new ProjectFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }
}
