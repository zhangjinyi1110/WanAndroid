package com.example.tiantian.myapplication.fragment.tree;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.example.tiantian.myapplication.databinding.FragmentTreeBinding;
import com.example.tiantian.myapplication.fragment.ArticleFragment;
import com.example.tiantian.myapplication.viewmodel.tree.TreeViewModel;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;

import java.util.ArrayList;

public class TreeFragment extends AbsBindingFragment<TreeViewModel, FragmentTreeBinding> {

    private ArrayList<ArticleFragment> fragments;

    public static TreeFragment newInstance() {
        return new TreeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tree;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ArrayList<Chapters> chapters = getSelfActivity().getIntent().getParcelableArrayListExtra("data");
        fragments = new ArrayList<>();
        final ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < chapters.size(); i++) {
            titles.add(chapters.get(i).getName());
            fragments.add(ArticleFragment.newInstance(chapters.get(i).getId()));
            binding.treeTab.addTab(binding.treeTab.newTab());
        }
        binding.treePager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        binding.treeTab.setupWithViewPager(binding.treePager);
        binding.treePager.setOffscreenPageLimit(chapters.size());
        binding.treePager.setCurrentItem(getSelfActivity().getIntent().getIntExtra("position", 0), false);
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
    }
}
