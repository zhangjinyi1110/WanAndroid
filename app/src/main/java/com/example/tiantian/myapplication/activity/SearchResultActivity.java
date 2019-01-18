package com.example.tiantian.myapplication.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.databinding.ActivitySearchResultBinding;
import com.example.tiantian.myapplication.fragment.ArticleFragment;
import com.example.tiantian.myapplication.viewmodel.search.SearchViewModel;
import com.zjy.simplemodule.base.activity.BindingSimpleActivity;
import com.zjy.simplemodule.base.activity.SimpleBindingActivity;

public class SearchResultActivity extends BindingSimpleActivity<ActivitySearchResultBinding> {

    @Override
    protected int getContentId() {
        return R.id.search_result_content;
    }

    @Override
    protected Fragment getFragment() {
        return ArticleFragment.newInstance(ArticleFragment.SEARCH, getIntent().getStringExtra("k"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected Toolbar getToolBar() {
        return (Toolbar) binding.searchResultToolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setTitle(getIntent().getStringExtra("title"));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
