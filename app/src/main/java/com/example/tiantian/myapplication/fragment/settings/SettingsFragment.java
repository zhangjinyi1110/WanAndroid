package com.example.tiantian.myapplication.fragment.settings;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.activity.login.LoginActivity;
import com.example.tiantian.myapplication.base.Contracts;
import com.example.tiantian.myapplication.databinding.FragmentSettingsBinding;
import com.example.tiantian.myapplication.utils.TextUtils;
import com.example.tiantian.myapplication.viewmodel.settings.SettingsViewModel;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;
import com.zjy.simplemodule.rxcallback.Avoid;
import com.zjy.simplemodule.rxcallback.RxAvoid;
import com.zjy.simplemodule.rxcallback.RxCallback;
import com.zjy.simplemodule.utils.EmptyUtils;
import com.zjy.simplemodule.utils.SharedPreferencesUtils;

public class SettingsFragment extends AbsBindingFragment<SettingsViewModel, FragmentSettingsBinding> {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    protected void observe() {
        viewModel.getLogoutFlag().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
//                    DiskCache.with(getSelfActivity())
//                            .cachePath(Contracts.USER_PATH)
//                            .remove(Contracts.USER_NAME);
//                    DiskCache.close();
                    binding.settingsLogout.setText("登录");
                    SharedPreferencesUtils.with(getSelfActivity()).put(Contracts.USER_NAME, getString(R.string.text_to_login));
                }
            }
        });
        viewModel.getCacheSize().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String aLong) {
                if (EmptyUtils.tUnEmpty(aLong)) {
                    binding.settingsCacheSize.setText(aLong);
                }
            }
        });
        viewModel.getLoginFlag().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (EmptyUtils.tUnEmpty(aBoolean)) {
                    binding.settingsLogout.setText(aBoolean ? "退出登录" : "登录");
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        binding.settingsLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.viewGetText(binding.settingsLogout).equals("退出登录")) {
                    viewModel.logout();
                } else
                    RxAvoid.getInstance()
                            .result(LoginActivity.class)
                            .callback(new RxCallback<Avoid>() {
                                @Override
                                public void onResult(Avoid avoid) {
                                    if (avoid.isOk()) {
                                        binding.settingsLogout.setText("退出登录");
                                        getSelfActivity().setResult(Activity.RESULT_OK, new Intent().putExtra(Contracts.USER_NAME, avoid.getIntent().getStringExtra(Contracts.USER_NAME)));
                                    }
                                }
                            });
            }
        });
        binding.settingsClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.clearCache();
            }
        });
    }

    @Override
    protected void initData() {
        viewModel.getSize();
        viewModel.isLogin();
    }
}
