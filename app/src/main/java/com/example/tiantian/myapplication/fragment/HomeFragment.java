package com.example.tiantian.myapplication.fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.activity.webview.WebViewActivity;
import com.example.tiantian.myapplication.activity.wxarticle.ArticleListActivity;
import com.example.tiantian.myapplication.adapter.SimpleAdapter;
import com.example.tiantian.myapplication.adapter.itemdecoration.SimpleItemDecoration;
import com.example.tiantian.myapplication.base.BaseFragment;
import com.example.tiantian.myapplication.data.main.BannerData;
import com.example.tiantian.myapplication.data.main.CommonWeb;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.data.wxarticle.ArticleData;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.example.tiantian.myapplication.databinding.ArticleItemBinding;
import com.example.tiantian.myapplication.databinding.ChaptersItemBinding;
import com.example.tiantian.myapplication.databinding.FragmentHomeBinding;
import com.example.tiantian.myapplication.utils.GlideImageLoader;
import com.example.tiantian.myapplication.utils.SizeUtils;
import com.example.tiantian.myapplication.viewmodel.main.MainViewModel;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, MainViewModel> {

    private List<BannerData> bannerList;
    private SimpleAdapter<CommonWeb, ChaptersItemBinding> adapter;
    private SimpleAdapter<ArticleData, ArticleItemBinding> articleAdapter;
    private int page = 0;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding.mainBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        binding.mainBanner.setImageLoader(new GlideImageLoader());
        adapter = new SimpleAdapter<CommonWeb, ChaptersItemBinding>(getSelfActivity()) {
            @Override
            protected int getLayoutId(int i) {
                return R.layout.chapters_item;
            }

            @Override
            protected void convert(ChaptersItemBinding binding, CommonWeb commonWeb, int position) {
                Chapters chapters = new Chapters();
                chapters.setName(commonWeb.getName());
                binding.setChapter(chapters);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                binding.getRoot().setLayoutParams(layoutParams);
            }
        };
        binding.recyclerHome.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = SizeUtils.dp2px(getSelfActivity(), 5);
                outRect.top = SizeUtils.dp2px(getSelfActivity(), 5);
                outRect.right = SizeUtils.dp2px(getSelfActivity(), 5);
                outRect.bottom = SizeUtils.dp2px(getSelfActivity(), 5);
            }
        });
        binding.recyclerHome.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        binding.recyclerHome.setAdapter(adapter);
        articleAdapter = new SimpleAdapter<ArticleData, ArticleItemBinding>(getSelfActivity()) {
            @Override
            protected int getLayoutId(int i) {
                return R.layout.article_item;
            }

            @Override
            protected void convert(ArticleItemBinding binding, ArticleData articleData, int position) {
                binding.setArticle(articleData);
            }
        };
        binding.homeRecyclerArticle.addItemDecoration(new SimpleItemDecoration(getSelfActivity(), 13, 13, 8, 8));
        binding.homeRecyclerArticle.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
        binding.homeRecyclerArticle.setAdapter(articleAdapter);
    }

    @Override
    public void initEvent() {
        binding.mainBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(new Intent(getSelfActivity(), WebViewActivity.class)
                        .putExtra("url", bannerList.get(position).getUrl())
                .putExtra("title", bannerList.get(position).getTitle()));
            }
        });
        adapter.setItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                startActivity(new Intent(getSelfActivity(), WebViewActivity.class)
                        .putExtra("url", adapter.getItemData(position).getLink())
                .putExtra("title", adapter.getItemData(position).getName()));
            }
        });
        articleAdapter.setItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                startActivity(new Intent(getSelfActivity(), WebViewActivity.class)
                        .putExtra("url", articleAdapter.getItemData(position).getLink())
                        .putExtra("title", articleAdapter.getItemData(position).getTitle()));
            }
        });
        binding.homeArticleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getSelfActivity(), ArticleListActivity.class)
                        .putExtra("page", page++)
                        .putExtra("type", ArticleListActivity.TYPE_HOME)
                        .putExtra("title", "更多文章"));
            }
        });
    }

    @Override
    public void initData() {
        viewModel.getBannerList().observe(this, new Observer<List<BannerData>>() {
            @Override
            public void onChanged(@Nullable List<BannerData> bannerData) {
                Log.d(TAG, "onChanged: " + (bannerData == null));
                if (bannerData != null) {
                    if (bannerData.size() == 0) {
                        binding.mainBanner.setVisibility(View.GONE);
                        return;
                    }
                    bannerList = bannerData;
                    List<String> imagePath = new ArrayList<>();
                    List<String> titles = new ArrayList<>();
                    for (int i = 0; i < bannerData.size(); i++) {
                        titles.add(bannerData.get(i).getTitle());
                        imagePath.add(bannerData.get(i).getImagePath());
                    }
                    binding.mainBanner.setImages(imagePath);
                    binding.mainBanner.setBannerTitles(titles);
                    binding.mainBanner.start();
                }
            }
        });
        viewModel.getCommonWebList().observe(this, new Observer<List<CommonWeb>>() {
            @Override
            public void onChanged(@Nullable List<CommonWeb> commonWebs) {
                if (commonWebs != null) {
                    adapter.addList(commonWebs);
                    adapter.setShowFooter(false);
                }
            }
        });
        viewModel.getHomeArticle(page).observe(this, new Observer<Article>() {
            @Override
            public void onChanged(@Nullable Article article) {
                if(article!=null){
                    articleAdapter.setShowFooter(false);
                    articleAdapter.addList(article.getDatas());
                }
            }
        });
    }

}
