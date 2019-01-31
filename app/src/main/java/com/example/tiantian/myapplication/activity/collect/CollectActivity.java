package com.example.tiantian.myapplication.activity.collect;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.databinding.ActivityCollectBinding;
import com.example.tiantian.myapplication.fragment.ArticleFragment;
import com.zjy.simplemodule.base.activity.BindingSimpleActivity;

public class CollectActivity extends BindingSimpleActivity<ActivityCollectBinding> {

    @Override
    protected int getContentId() {
        return R.id.collect_content;
    }

    @Override
    protected Fragment getFragment() {
        return ArticleFragment.newInstance(ArticleFragment.COLLECT);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    protected Toolbar getToolBar() {
        return (Toolbar) binding.collectToolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("收藏");
    }
}
