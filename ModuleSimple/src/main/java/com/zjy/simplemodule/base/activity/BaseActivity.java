package com.zjy.simplemodule.base.activity;

import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.zjy.simplemodule.R;
import com.zjy.simplemodule.factory.LayoutFactory;
import com.zjy.simplemodule.receiver.NetWorkReceiver;
import com.zjy.simplemodule.utils.ActivityManager;

import java.lang.reflect.Field;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();
    private View notNetView;
    private WindowManager manager;
    private WindowManager.LayoutParams layoutParams;
    private boolean isAddView = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        styleColor();
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
        initNotNetView();
        NetWorkReceiver receiver = new NetWorkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        filter.addAction("android.net.ethernet.STATE_CHANGE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(receiver, filter);
    }

    private void styleColor() {
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(getLayoutInflater(), false);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        getLayoutInflater().setFactory2(new LayoutFactory());
    }

    public void hideNotNetView() {
        if (isAddView)
            manager.removeView(notNetView);
        isAddView = false;
    }

    public void showNotNetView() {
        if (!isAddView)
            manager.addView(notNetView, layoutParams);
        isAddView = true;
    }

    private void initNotNetView() {
        notNetView = getLayoutInflater().inflate(R.layout.not_net_layout, null);
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION
                , WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                , PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 0;
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

    public Resources getSkinResources(AssetManager assetManager) {
        return new Resources(assetManager, getResources().getDisplayMetrics(), getResources().getConfiguration());
    }
}
