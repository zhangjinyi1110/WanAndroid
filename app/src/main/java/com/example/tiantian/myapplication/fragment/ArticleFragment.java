package com.example.tiantian.myapplication.fragment;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.tiantian.myapplication.base.Contracts;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.data.wxarticle.ArticleData;
import com.example.tiantian.myapplication.databinding.FragmentArticleBinding;
import com.example.tiantian.myapplication.databinding.ItemHomeArticleBinding;
import com.example.tiantian.myapplication.viewmodel.article.ArticleViewModel;
import com.zjy.simplemodule.adapter.BaseAdapter;
import com.zjy.simplemodule.adapter.BindingAdapter;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;
import com.zjy.simplemodule.utils.Callback;
import com.zjy.simplemodule.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends AbsBindingFragment<ArticleViewModel, FragmentArticleBinding> {

    private BindingAdapter<ArticleData, ItemHomeArticleBinding> adapter;
    private int page;
    private int cid;
    private String k;
    public static final int NOT_CID = -1;
    public static final int SEARCH = -2;
    public static final int COLLECT = -3;
    private UpdateBroadcastReceiver receiver;

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
            } else if (cid == COLLECT) {
                setLazy(false);
            }
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Contracts.ARTICLE_COLLECT);
        filter.addAction(Contracts.LOGIN);
        filter.addAction(Contracts.LOGOUT);
        receiver = new UpdateBroadcastReceiver();
        getSelfActivity().registerReceiver(receiver, filter);
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
            protected void convert(final ItemHomeArticleBinding binding, final ArticleData articleData, final int position) {
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
                if (articleData.isCollect() || cid == COLLECT) {
                    articleData.setCollect(true);
                    binding.itemArticleCollect.setImageResource(R.drawable.ic_favorite_gray_18dp);
                } else {
                    binding.itemArticleCollect.setImageResource(R.drawable.ic_favorite_border_gray_18dp);
                }
                binding.itemArticleCollect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!articleData.isCollect())
                            viewModel.collect(articleData, new Callback<Boolean>() {
                                @Override
                                public void onCall(Boolean aBoolean) {
                                    if (aBoolean) {
                                        articleData.setCollect(true);
                                        binding.itemArticleCollect.setImageResource(R.drawable.ic_favorite_gray_18dp);
                                    } else {
                                        articleData.setCollect(false);
                                        binding.itemArticleCollect.setImageResource(R.drawable.ic_favorite_border_gray_18dp);
                                    }
                                }
                            });
                        else
                            viewModel.uncollect((cid == COLLECT) ? articleData.getOriginId() : articleData.getId(), new Callback<Boolean>() {
                                @Override
                                public void onCall(Boolean aBoolean) {
                                    if (!aBoolean) {
                                        articleData.setCollect(true);
                                        binding.itemArticleCollect.setImageResource(R.drawable.ic_favorite_gray_18dp);
                                    } else {
                                        if (cid == COLLECT) {
                                            adapter.getList().remove(articleData);
                                            notifyDataSetChanged();
                                            return;
                                        }
                                        articleData.setCollect(false);
                                        binding.itemArticleCollect.setImageResource(R.drawable.ic_favorite_border_gray_18dp);
                                    }
                                }
                            });
                    }
                });
            }

            @Override
            protected void convert(ItemHomeArticleBinding binding, ArticleData data, int position, List<Object> payloads) {
                switch ((String) payloads.get(0)) {
                    case Contracts.ARTICLE_COLLECT_CANCEL:
                        binding.itemArticleCollect.setImageResource(R.drawable.ic_favorite_border_gray_18dp);
                        break;
                    case Contracts.ARTICLE_COLLECT_YES:
                        binding.itemArticleCollect.setImageResource(R.drawable.ic_favorite_gray_18dp);
                        break;
                }
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
                adapter.setLoadEnable(true);
                if (cid == SEARCH)
                    viewModel.search(page, k);
                else if (cid == COLLECT)
                    viewModel.getCollectList(page);
                else if (k == null)
                    viewModel.getArticle(page, cid);
            }
        });
        adapter.setItemClickListener(new BindingAdapter.OnItemClickListener<ArticleData, ItemHomeArticleBinding>() {
            @Override
            public void onItemClick(ItemHomeArticleBinding binding, ArticleData articleData, int position) {
                startActivity(new Intent(getSelfActivity(), WebViewActivity.class)
                        .putExtra(Contracts.ARTICLE_URL, articleData.getLink())
                        .putExtra(Contracts.ARTICLE_TITLE, articleData.getTitle())
                        .putExtra(Contracts.ARTICLE_IS_COLLECT, articleData.isCollect())
                        .putExtra(Contracts.ARTICLE_ID, (cid == COLLECT) ? articleData.getOriginId() : articleData.getId()));
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
        if (cid == SEARCH)
            viewModel.search(page, k);
        else if (cid == COLLECT)
            viewModel.getCollectList(page);
        else if (k == null)
            viewModel.getArticle(page, cid);
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
                } else {
                    adapter.setLoadEnable(false);
                    adapter.notifyItemRangeChanged(adapter.getItemCount() - 1, 1);
                }
            }
        };
        viewModel.getArticleData().observe(this, observer);
        viewModel.getSearchResult().observe(this, observer);
        viewModel.getCollectList().observe(this, observer);
        viewModel.getUpdateList().observe(this, new Observer<Article>() {
            @Override
            public void onChanged(@Nullable Article article) {
                if (article != null) {
                    if (article.isOver()) {
                        adapter.setLoadEnable(false);
                    }
                    if (binding.refreshArticle.isRefreshing())
                        binding.refreshArticle.setRefreshing(false);
                    adapter.setList(article.getDatas());
                } else {
                    adapter.setLoadEnable(false);
                    adapter.notifyItemRangeChanged(adapter.getItemCount() - 1, 1);
                }
            }
        });
    }

    private class UpdateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (EmptyUtils.tUnEmpty(intent) && EmptyUtils.tUnEmpty(intent.getAction())) {
                switch (intent.getAction()) {
                    case Contracts.ARTICLE_COLLECT:
                        String data = intent.getStringExtra(Contracts.ARTICLE_COLLECT_ACTION);
                        if (EmptyUtils.tUnEmpty(data)) {
                            int id = intent.getIntExtra(Contracts.ARTICLE_COLLECT_ID, -1);
                            ArrayList<ArticleData> arrayList = new ArrayList<>(adapter.getList());
                            switch (data) {
                                case Contracts.ARTICLE_COLLECT_YES:
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        if (arrayList.get(i).getId() == id) {
                                            arrayList.get(i).setCollect(true);
                                            adapter.notifyItemRangeChanged(i, 1, Contracts.ARTICLE_COLLECT_YES);
                                            break;
                                        }
                                    }
                                    break;
                                case Contracts.ARTICLE_COLLECT_CANCEL:
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        if (arrayList.get(i).getId() == id) {
                                            arrayList.get(i).setCollect(false);
                                            adapter.notifyItemRangeChanged(i, 1, Contracts.ARTICLE_COLLECT_CANCEL);
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        break;
                    case Contracts.LOGIN:
                        adapter.setLoad(true);
                        viewModel.updateArticle(page - 1, 0, new ArrayList<ArticleData>());
                        break;
                    case Contracts.LOGOUT:
                        adapter.setLoad(true);
                        viewModel.updateArticle(page - 1, 0, new ArrayList<ArticleData>());
                        break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EmptyUtils.tUnEmpty(receiver)) {
            getSelfActivity().unregisterReceiver(receiver);
        }
    }
}
