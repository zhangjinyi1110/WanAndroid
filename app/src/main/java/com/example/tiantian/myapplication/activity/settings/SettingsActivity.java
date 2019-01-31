package com.example.tiantian.myapplication.activity.settings;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.databinding.ActivitySettingsBinding;
import com.example.tiantian.myapplication.fragment.settings.SettingsFragment;
import com.zjy.simplemodule.base.activity.BindingSimpleActivity;

public class SettingsActivity extends BindingSimpleActivity<ActivitySettingsBinding> {

    @Override
    protected int getContentId() {
        return R.id.settings_content;
    }

    @Override
    protected Fragment getFragment() {
        return SettingsFragment.newInstance();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected Toolbar getToolBar() {
        return (Toolbar) binding.settingsToolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setTitle("设置");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
