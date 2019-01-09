package com.example.tiantian.myapplication.activity.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.databinding.ActivityMainBinding;
import com.example.tiantian.myapplication.fragment.main.HomeFragment;
import com.example.tiantian.myapplication.fragment.main.NavigationFragment;
import com.example.tiantian.myapplication.fragment.main.ProjectFragment;
import com.example.tiantian.myapplication.fragment.main.SystemFragment;
import com.example.tiantian.myapplication.viewmodel.main.MainViewModel;
import com.zjy.simplemodule.base.activity.SimpleBindingActivity;

public class MainActivity extends SimpleBindingActivity<MainViewModel, ActivityMainBinding> {

    private HomeFragment homeFragment;
    private SystemFragment systemFragment;
    private NavigationFragment navigationFragment;
    private ProjectFragment projectFragment;

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
    }
}
