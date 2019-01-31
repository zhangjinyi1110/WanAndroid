package com.example.tiantian.myapplication.activity.main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.activity.about.AboutActivity;
import com.example.tiantian.myapplication.activity.collect.CollectActivity;
import com.example.tiantian.myapplication.activity.login.LoginActivity;
import com.example.tiantian.myapplication.activity.search.SearchActivity;
import com.example.tiantian.myapplication.activity.settings.SettingsActivity;
import com.example.tiantian.myapplication.base.Contracts;
import com.example.tiantian.myapplication.databinding.ActivityMainBinding;
import com.example.tiantian.myapplication.fragment.main.HomeFragment;
import com.example.tiantian.myapplication.fragment.main.NavigationFragment;
import com.example.tiantian.myapplication.fragment.main.ProjectFragment;
import com.example.tiantian.myapplication.fragment.main.SystemFragment;
import com.example.tiantian.myapplication.viewmodel.main.MainViewModel;
import com.zjy.simplemodule.base.activity.SimpleBindingActivity;
import com.zjy.simplemodule.rxcallback.Avoid;
import com.zjy.simplemodule.rxcallback.RxAvoid;
import com.zjy.simplemodule.rxcallback.RxCallback;
import com.zjy.simplemodule.utils.AssetUtils;
import com.zjy.simplemodule.utils.Callback;
import com.zjy.simplemodule.utils.EnvironmentUtils;
import com.zjy.simplemodule.utils.SharedPreferencesUtils;
import com.zjy.simplemodule.utils.ToastUtils;

public class MainActivity extends SimpleBindingActivity<MainViewModel, ActivityMainBinding> {

    private HomeFragment homeFragment;
    private SystemFragment systemFragment;
    private NavigationFragment navigationFragment;
    private ProjectFragment projectFragment;
    private ImageView headerImage;
    private TextView headerName;

    @Override
    protected int getContentId() {
        return R.id.main_content;
    }

    @Override
    protected Toolbar getToolBar() {
        return binding.mainToolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        super.initToolbar(actionBar);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.app_name));
    }

    @Override
    protected Fragment getFragment() {
        if (homeFragment == null)
            homeFragment = HomeFragment.newInstance();
        return homeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        systemFragment = SystemFragment.newInstance();
        navigationFragment = NavigationFragment.newInstance();
        projectFragment = ProjectFragment.newInstance();
        headerImage = binding.mainNavigation.getHeaderView(0).findViewById(R.id.header_image);
        headerName = binding.mainNavigation.getHeaderView(0).findViewById(R.id.header_name);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.mainBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        changeFragment(homeFragment);
                        break;
                    case R.id.bottom_system:
                        changeFragment(systemFragment);
                        break;
                    case R.id.bottom_navigation:
                        changeFragment(navigationFragment);
                        break;
                    case R.id.bottom_project:
                        changeFragment(projectFragment);
                        break;
                }
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.mainDrawer
                , binding.mainToolbar, R.string.app_name, R.string.app_name);
        toggle.syncState();
        binding.mainDrawer.addDrawerListener(toggle);
        binding.mainNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_about_me:
                        startActivity(new Intent(getSelf(), AboutActivity.class));
                        break;
                    case R.id.navigation_collect:
                        startActivity(new Intent(getSelf(), CollectActivity.class));
                        break;
                    case R.id.navigation_light:
                        String path = EnvironmentUtils.getFileDir(getSelf(), Environment.DIRECTORY_DOCUMENTS).getPath();
                        final String filepath = path + "/skin.apk";
                        AssetUtils.copyFile(getSelf(), filepath, "skinColor.apk", new Callback<Boolean>() {
                            @Override
                            public void onCall(Boolean aBoolean) {
                                if (aBoolean) {
                                    Resources resources = getSkinResources(AssetUtils.addAssetPath(filepath));
                                    PackageInfo info = getPackageManager().getPackageArchiveInfo(filepath, PackageManager.GET_ACTIVITIES);
                                    int id = resources.getIdentifier("colorA", "color"
                                            , info.packageName);
                                    headerName.setTextColor(resources.getColor(id));
                                }
                            }
                        });
                        break;
                    case R.id.navigation_settings:
                        RxAvoid.getInstance()
                                .with(getSelf())
                                .result(SettingsActivity.class)
                                .callback(new RxCallback<Avoid>() {
                                    @Override
                                    public void onResult(Avoid avoid) {
                                        if (avoid.isOk()) {
                                            headerName.setText(avoid.getIntent().getStringExtra(Contracts.USER_NAME));
                                        }
                                    }
                                });
                        break;
                }
                binding.mainDrawer.closeDrawers();
                return true;
            }
        });
        headerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (headerName.getText().toString().equals(getString(R.string.text_to_login))) {
//                    startActivity(new Intent(getSelf(), LoginActivity.class));
                    RxAvoid.getInstance()
                            .with(getSelf())
                            .result(LoginActivity.class)
                            .callback(new RxCallback<Avoid>() {
                                @Override
                                public void onResult(Avoid avoid) {
                                    if (avoid.isOk()) {
                                        Intent intent = avoid.getIntent();
                                        if (intent != null) {
                                            String userName = intent.getStringExtra(Contracts.USER_NAME);
                                            if (userName != null)
                                                headerName.setText(userName);
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
//        String userName = DiskCache.with(getSelf()).cachePath(Contracts.USER_PATH).getAndClose(Contracts.USER_NAME);
        String userName = SharedPreferencesUtils.with(this).getString(Contracts.USER_NAME, getString(R.string.text_to_login));
        if (userName != null) {
            headerName.setText(userName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_option_search:
                startActivity(new Intent(getSelf(), SearchActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
