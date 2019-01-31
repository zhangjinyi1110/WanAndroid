package com.example.tiantian.myapplication.activity.webview;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.api.ArticleService;
import com.example.tiantian.myapplication.api.CollectService;
import com.example.tiantian.myapplication.base.BaseActivity;
import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.base.Contracts;
import com.example.tiantian.myapplication.databinding.ActivityWebViewBinding;
import com.zjy.simplemodule.base.activity.BindingActivity;
import com.zjy.simplemodule.retrofit.AutoDisposeUtils;
import com.zjy.simplemodule.retrofit.BaseSubscriber;
import com.zjy.simplemodule.retrofit.HttpResultException;
import com.zjy.simplemodule.retrofit.HttpResultTransformer;
import com.zjy.simplemodule.retrofit.RetrofitUtils;

public class WebViewActivity extends BindingActivity<ActivityWebViewBinding> {

    private String url;
    private int id;
    private boolean collect = false;
    private MenuItem item;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected Toolbar getToolBar() {
        return (Toolbar) binding.toolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra(Contracts.ARTICLE_TITLE));
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(Bundle savedInstanceState) {
        WebSettings settings = binding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        url = getIntent().getStringExtra(Contracts.ARTICLE_URL);
        id = getIntent().getIntExtra(Contracts.ARTICLE_ID, 0);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        item = menu.findItem(R.id.web_collect);
        if (item != null) {
            if (getIntent().getBooleanExtra(Contracts.ARTICLE_BANNER, false)) {
                item.setVisible(false);
            } else {
                if (getIntent().getBooleanExtra(Contracts.ARTICLE_IS_COLLECT, false)) {
                    item.setTitle("取消收藏");
                    collect = true;
                } else {
                    collect = false;
                    item.setTitle("收藏");
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web_copy:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", url);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.web_collect:
                if (collect)
                    uncollect();
                else
                    collect();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uncollect() {
        RetrofitUtils.getInstance()
                .createService(CollectService.class)
                .uncollect(id)
                .compose(new HttpResultTransformer<>())
                .as(AutoDisposeUtils.bind(this))
                .subscribe(new BaseSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFailure(HttpResultException exception) {
                        Toast.makeText(getSelf(), ("取消收藏失败: ") + exception.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onEmpty() {
                        sendBroadcast(new Intent(Contracts.ARTICLE_COLLECT)
                                .putExtra(Contracts.ARTICLE_COLLECT_ACTION, Contracts.ARTICLE_COLLECT_CANCEL)
                                .putExtra(Contracts.ARTICLE_COLLECT_ID, id));
                        collect = false;
                        item.setTitle("收藏");
                        Toast.makeText(getSelf(), "取消收藏成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void collect() {
        RetrofitUtils.getInstance()
                .createService(CollectService.class)
                .collect(id)
                .compose(new HttpResultTransformer<>())
                .as(AutoDisposeUtils.bind(this))
                .subscribe(new BaseSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFailure(HttpResultException exception) {
                        Toast.makeText(getSelf(), ("收藏失败: ") + exception.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEmpty() {
                        sendBroadcast(new Intent(Contracts.ARTICLE_COLLECT)
                                .putExtra(Contracts.ARTICLE_COLLECT_ACTION, Contracts.ARTICLE_COLLECT_YES)
                                .putExtra(Contracts.ARTICLE_COLLECT_ID, id));
                        collect = true;
                        item.setTitle("取消收藏");
                        Toast.makeText(getSelf(), "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
