package com.example.tiantian.myapplication.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Size;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.tiantian.myapplication.utils.SizeUtils;

public class ProgressWebView extends WebView {

    private ProgressBar progressBar;
    private Context context;

    public ProgressWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(context, 6));
        progressBar.setLayoutParams(layoutParams);
        addView(progressBar);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if(newProgress==100){
                    progressBar.setVisibility(GONE);
                }else{
                    progressBar.setVisibility(VISIBLE);
                }
            }
        });
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
