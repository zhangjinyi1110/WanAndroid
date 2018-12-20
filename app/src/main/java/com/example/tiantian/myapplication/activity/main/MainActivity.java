package com.example.tiantian.myapplication.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.activity.wxarticle.ChaptersActivity;
import com.example.tiantian.myapplication.base.BaseActivity;
import com.example.tiantian.myapplication.databinding.ActivityMainBinding;
import com.example.tiantian.myapplication.fragment.HomeFragment;
import com.example.tiantian.myapplication.viewmodel.main.MainViewModel;
import com.example.tiantian.myapplication.widget.PageView;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setSupportActionBar(binding.mainToolbar);
        binding.mainNavigation.setItemTextColor(getResources().getColorStateList(R.color.navigation_text_color));
        ImageView header = new ImageView(this);
        header.setImageResource(R.drawable.ic_navigation_bg);
        header.setAdjustViewBounds(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(layoutParams);
        binding.mainNavigation.addHeaderView(header);
        setFragment(R.id.main_content, HomeFragment.newInstance());
    }

    @Override
    public void initEvent() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.mainDrawer, binding.mainToolbar, R.string.app_name, R.string.app_name);
        binding.mainDrawer.setDrawerListener(toggle);
        toggle.syncState();
        binding.mainNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_chapters:
                        startActivity(new Intent(getApplicationContext(), ChaptersActivity.class));
                        break;
                }
                binding.mainDrawer.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void initData() {

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
                break;
        }
        return true;
    }
}
