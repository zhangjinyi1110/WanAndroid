package com.example.tiantian.myapplication.fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.activity.webview.WebViewActivity;
import com.example.tiantian.myapplication.adapter.itemdecoration.RailItemDecoration;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.data.wxarticle.ArticleData;
import com.example.tiantian.myapplication.databinding.FragmentArticleBinding;
import com.example.tiantian.myapplication.databinding.ItemHomeArticleBinding;
import com.example.tiantian.myapplication.viewmodel.article.ArticleViewModel;
import com.zjy.simplemodule.adapter.BaseAdapter;
import com.zjy.simplemodule.adapter.BindingAdapter;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;

public class ArticleFragment extends AbsBindingFragment<ArticleViewModel, FragmentArticleBinding> {

    private BindingAdapter<ArticleData, ItemHomeArticleBinding> adapter;
    private int page = 0;
    private int cid;
    public static final int NOT_CID = -1;

    public static ArticleFragment newInstance(int cid) {
        Bundle args = new Bundle();
        args.putInt("cid", cid);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            cid = bundle.getInt("cid");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_article;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        adapter = new BindingAdapter<ArticleData, ItemHomeArticleBinding>(getSelfActivity()) {
            @Override
            protected void convert(ItemHomeArticleBinding binding, ArticleData articleData, int position) {
                if (TextUtils.isEmpty(articleData.getEnvelopePic())) {
                    binding.itemArticleImage.setVisibility(View.GONE);
                    binding.itemArticleMessage.setVisibility(View.GONE);
                } else {
                    binding.itemArticleMessage.setVisibility(View.VISIBLE);
                    binding.itemArticleMessage.setText(articleData.getDesc());
                    binding.itemArticleImage.setVisibility(View.VISIBLE);
                    Glide.with(getSelfActivity()).load(articleData.getEnvelopePic()).into(binding.itemArticleImage);
                }
                if (articleData.getNiceDate().contains("小时前")) {
                    binding.itemNew.setVisibility(View.VISIBLE);
                } else {
                    binding.itemNew.setVisibility(View.GONE);
                }
                binding.itemArticleTitle.setText(articleData.getTitle());
                binding.itemArticleAuthor.setText(articleData.getAuthor());
                binding.itemArticleTime.setText(articleData.getNiceDate());
                binding.itemArticleTag.setText(articleData.getChapterName());
            }

            @Override
            protected int getLayoutId(int type) {
                return R.layout.item_home_article;
            }
        };
        binding.recyclerArticle.addItemDecoration(new RailItemDecoration(getSelfActivity()));
        binding.recyclerArticle.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
        binding.recyclerArticle.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        adapter.setLoadMoreListener(new BaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "onLoadMore: ");
                viewModel.getArticle(page, cid);
            }
        });
        adapter.setItemClickListener(new BindingAdapter.OnItemClickListener<ArticleData, ItemHomeArticleBinding>() {
            @Override
            public void onItemClick(ItemHomeArticleBinding binding, ArticleData articleData, int position) {
                startActivity(new Intent(getSelfActivity(), WebViewActivity.class)
                        .putExtra("url", articleData.getLink())
                        .putExtra("title", articleData.getTitle()));
            }
        });
    }

    @Override
    protected void initData() {
        Log.d(TAG, "initData: " + cid);
        viewModel.getArticleData().observe(this, new Observer<Article>() {
            @Override
            public void onChanged(@Nullable Article article) {
                if (article != null) {
                    Log.e(TAG, "onChanged: " + cid);
                    if (article.getCurPage() == article.getPageCount()) {
                        adapter.setLoadEnable(false);
                    }
                    page++;
                    adapter.addList(article.getDatas());
                }
            }
        });
        viewModel.getArticle(page, cid);
    }

    @Override
    public boolean isLazy() {
        return false;
    }
}
