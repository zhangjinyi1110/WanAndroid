package com.example.tiantian.myapplication.fragment.main;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.example.tiantian.myapplication.databinding.FragmentProjectBinding;
import com.example.tiantian.myapplication.fragment.tree.TreeFragment;
import com.example.tiantian.myapplication.viewmodel.main.MainViewModel;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;

import java.util.ArrayList;
import java.util.List;

public class ProjectFragment extends AbsBindingFragment<MainViewModel, FragmentProjectBinding> {

    private TreeFragment treeFragment;

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
        viewModel.getProjectTree();
    }

    @Override
    protected void observe() {
        viewModel.getProjectList().observe(this, new Observer<List<Chapters>>() {
            @Override
            public void onChanged(@Nullable List<Chapters> chapters) {
                if (chapters != null) {
                    getSelfActivity().getIntent()
                            .putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) chapters);
                    if (treeFragment == null) {
                        treeFragment = TreeFragment.newInstance();
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.project_content, treeFragment)
                                .commitNow();
                    }
                }
            }
        });
    }

    @Override
    public boolean isLazy() {
        return false;
    }
}
