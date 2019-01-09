package com.example.tiantian.myapplication.activity.tree;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.databinding.ActivityTreeBinding;
import com.example.tiantian.myapplication.fragment.tree.TreeFragment;
import com.zjy.simplemodule.base.activity.BindingSimpleActivity;

public class TreeActivity extends BindingSimpleActivity<ActivityTreeBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tree;
    }

    @Override
    protected int getContentId() {
        return R.id.tree_content;
    }

    @Override
    protected Fragment getFragment() {
        return TreeFragment.newInstance();
    }

    @Override
    protected Toolbar getToolBar() {
        return (Toolbar) binding.toolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("title"));
    }
}
