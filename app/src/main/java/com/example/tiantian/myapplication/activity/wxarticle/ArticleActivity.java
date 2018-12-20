package com.example.tiantian.myapplication.activity.wxarticle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.base.BaseActivity;
import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.databinding.ActivityArticleBinding;

public class ArticleActivity extends BaseActivity<ActivityArticleBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_article;
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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(Bundle savedInstanceState) {
        WebSettings settings = binding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        String url = getIntent().getStringExtra("url");
        binding.webView.loadUrl(url);
    }

    @Override
    public void initEvent() {
        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        binding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d(getClass().getSimpleName(), "onProgressChanged: " + newProgress);
                if(newProgress==100){
                    binding.webView.getProgressBar().setVisibility(View.GONE);
                    return;
                }
                if(binding.webView.getProgressBar().getVisibility()==View.GONE)
                    binding.webView.getProgressBar().setVisibility(View.VISIBLE);
                binding.webView.getProgressBar().setProgress(newProgress);
            }
        });
    }

    @Override
    public void initData() {

    }
}
