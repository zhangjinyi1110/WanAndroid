package com.zjy.simplemodule.base.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zjy.simplemodule.utils.ActivityManager;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isBinding()) {
            setContentView(getLayoutId());
        } else {
            bindingView();
        }
        if (isViewModel()) {
            initViewModel();
        }
        ActivityManager.getInstance().addActivity(this);
        if (getToolBar() != null) {
            setSupportActionBar(getToolBar());
            initToolbar(getSupportActionBar());
        }
        initView(savedInstanceState);
        initEvent();
        initData();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();

    protected void bindingView() {
    }

    protected void initViewModel() {

    }

    protected boolean isViewModel() {
        return false;
    }

    protected Toolbar getToolBar() {
        return null;
    }

    protected void initToolbar(ActionBar actionBar) {
    }

    protected boolean isBinding() {
        return false;
    }

    protected BaseActivity getSelf() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return onHomeClick();
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean onHomeClick() {
        finish();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isHideKeyboard()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View view = getWindow().getCurrentFocus();
                if (view != null) {
                    int[] lt = new int[2];
                    view.getLocationInWindow(lt);
                    int left = lt[0];
                    int top = lt[1];
                    int bottom = top + view.getHeight();
                    int right = left + view.getWidth();
                    float x = event.getX();
                    float y = event.getY();
                    boolean flag = x >= left && x <= right && y >= top && y <= bottom;
                    if (!flag) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if (inputMethodManager != null) {
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            view.clearFocus();
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    protected boolean isHideKeyboard() {
        return false;
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }
}
