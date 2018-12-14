package com.example.tiantian.myapplication;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.tiantian.myapplication.databinding.ActivityThirdBinding;
import com.example.tiantian.myapplication.widget.ProgressWebView;

public class ThirdActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityThirdBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_third);
        WebSettings settings = binding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        String url = getIntent().getStringExtra("url");
        binding.webView.loadUrl(url);
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
}
