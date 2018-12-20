package com.example.tiantian.myapplication.activity.webview;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.base.BaseActivity;
import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.databinding.ActivityWebViewBinding;

public class WebViewActivity extends BaseActivity<ActivityWebViewBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) binding.toolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("title"));
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("url");
        binding.webView.loadUrl(url);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack())
            binding.webView.goBack();
        else
            super.onBackPressed();
    }
}
