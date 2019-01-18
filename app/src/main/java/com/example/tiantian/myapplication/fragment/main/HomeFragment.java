package com.example.tiantian.myapplication.fragment.main;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.activity.webview.WebViewActivity;
import com.example.tiantian.myapplication.data.main.BannerData;
import com.example.tiantian.myapplication.databinding.FragmentHomeBinding;
import com.example.tiantian.myapplication.fragment.ArticleFragment;
import com.example.tiantian.myapplication.utils.GlideImageLoader;
import com.example.tiantian.myapplication.viewmodel.main.MainViewModel;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.tiantian.myapplication.fragment.ArticleFragment.NOT_CID;

public class HomeFragment extends AbsBindingFragment<MainViewModel, FragmentHomeBinding> {

    private List<BannerData> bannerList;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        binding.homeBanner.setImageLoader(new GlideImageLoader());
        binding.homeBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        binding.homeBanner.setIndicatorGravity(BannerConfig.RIGHT);
        FragmentManager manager = getChildFragmentManager();
        if (savedInstanceState != null) {
            for (Fragment f : manager.getFragments()) {
                manager.beginTransaction().remove(f).commit();
            }
            manager.getFragments().clear();
        }
        manager.beginTransaction()
                .replace(R.id.home_content, ArticleFragment.newInstance(NOT_CID))
                .commitNow();
    }

    @Override
    protected void initEvent() {
        binding.homeBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(new Intent(getSelfActivity(), WebViewActivity.class)
                        .putExtra("url", bannerList.get(position).getUrl())
                        .putExtra("title", bannerList.get(position).getTitle()));
            }
        });
    }

    @Override
    protected void initData() {
        viewModel.getHomeBannerList();
    }

    @Override
    protected void observe() {
        viewModel.getBannerList().observe(this, new Observer<List<BannerData>>() {
            @Override
            public void onChanged(@Nullable List<BannerData> bannerData) {
                if (bannerData != null) {
                    bannerList = bannerData;
                    List<String> imageList = new ArrayList<>();
                    List<String> titleList = new ArrayList<>();
                    for (BannerData banner : bannerData) {
                        imageList.add(banner.getImagePath());
                        titleList.add(banner.getTitle());
                    }
                    binding.homeBanner.setBannerTitles(titleList);
                    binding.homeBanner.setImages(imageList);
                    binding.homeBanner.start();
                }
            }
        });
    }

    @Override
    public boolean isLazy() {
        return false;
    }
}
