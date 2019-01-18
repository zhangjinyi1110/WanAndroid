package com.example.tiantian.myapplication.fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
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
    private int page;
    private int cid;
    private String k;
    public static final int NOT_CID = -1;
    public static final int SEARCH = -2;

    public static ArticleFragment newInstance(int cid, String k) {
        Bundle args = new Bundle();
        args.putInt("cid", cid);
        args.putString("k", k);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
            if (cid == NOT_CID)
                setLazy(false);
            else if (cid == SEARCH) {
                setLazy(false);
                k = bundle.getString("k");
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_article;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        binding.refreshArticle.setEnabled(cid != NOT_CID);
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
                if (articleData.getNiceDate().contains("小时前")
                        || articleData.getNiceDate().contains("分钟前")
                        || articleData.getNiceDate().contains("秒前")) {
                    binding.itemNew.setVisibility(View.VISIBLE);
                } else {
                    binding.itemNew.setVisibility(View.GONE);
                }
                binding.itemArticleTitle.setText(Html.fromHtml(articleData.getTitle()));
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
//        ((SimpleItemAnimator) binding.recyclerArticle.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void initEvent() {
        adapter.setLoadMoreListener(new BaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
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
        binding.refreshArticle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!binding.refreshArticle.isRefreshing())
                    binding.refreshArticle.setRefreshing(true);
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        page = 0;
        adapter.setLoad(true);
        adapter.clear();
        adapter.setLoadEnable(true);
        if (k == null)
            viewModel.getArticle(page, cid);
        else
            viewModel.search(page, k);
    }

    @Override
    protected void observe() {
        Observer<Article> observer = new Observer<Article>() {
            @Override
            public void onChanged(@Nullable Article article) {
                if (article != null) {
                    if (article.isOver()) {
                        adapter.setLoadEnable(false);
                    }
                    if (binding.refreshArticle.isRefreshing())
                        binding.refreshArticle.setRefreshing(false);
                    page++;
                    adapter.addList(article.getDatas());
                }
            }
        };
        viewModel.getArticleData().observe(this, observer);
        viewModel.getSearchResult().observe(this, observer);
    }

}
