package com.example.tiantian.myapplication.activity.wxarticle;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.adapter.SimpleAdapter;
import com.example.tiantian.myapplication.adapter.itemdecoration.SimpleItemDecoration;
import com.example.tiantian.myapplication.base.BaseActivity;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.data.wxarticle.ArticleData;
import com.example.tiantian.myapplication.databinding.ActivityArticleListBinding;
import com.example.tiantian.myapplication.databinding.ArticleItemBinding;
import com.example.tiantian.myapplication.viewmodel.wxarticle.ArticleListViewModel;

public class ArticleListActivity extends BaseActivity<ActivityArticleListBinding, ArticleListViewModel> {

    private SimpleAdapter<ArticleData, ArticleItemBinding> adapter;
    private int id;
    private int page;
    private int type;
    public static final int TYPE_HOME = 0x01;
    public static final int TYPE_DEFAULT = 0x00;

    @Override
    public int getLayoutId() {
        return R.layout.activity_article_list;
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) binding.toolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("title"));
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        page = getIntent().getIntExtra("page", 1);
        type = getIntent().getIntExtra("type", TYPE_DEFAULT);
        binding.recyclerArticle.addItemDecoration(new SimpleItemDecoration(this, 13, 13, 8, 8));
        binding.recyclerArticle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<ArticleData, ArticleItemBinding>(this) {
            @Override
            protected int getLayoutId(int i) {
                return R.layout.article_item;
            }

            @Override
            protected void convert(ArticleItemBinding binding, ArticleData articleData, int position) {
                binding.setArticle(articleData);
            }
        };
        binding.recyclerArticle.setAdapter(adapter);
        adapter.setShowFooter(false);
    }

    @Override
    public void initEvent() {
        adapter.setItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                startActivity(new Intent(getApplicationContext(), ArticleActivity.class)
                        .putExtra("url", adapter.getItemData(position).getLink())
                        .putExtra("title", adapter.getItemData(position).getTitle()));
            }
        });
        adapter.setLoadMoreListener(new SimpleAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (type == TYPE_DEFAULT)
                    viewModel.getArticle(String.valueOf(id), page);
                else if (type == TYPE_HOME)
                    viewModel.getHomeArticle(page);
            }
        });
    }

    @Override
    public void initData() {
        id = getIntent().getIntExtra("id", 0);
        if (type == TYPE_DEFAULT)
            viewModel.getArticle(String.valueOf(id), page).observe(this, observer);
        else if (type == TYPE_HOME)
            viewModel.getHomeArticle(page).observe(this, observer);
    }

    private Observer observer = new Observer<Article>() {
        @Override
        public void onChanged(@Nullable Article article) {
            if (article != null) {
                Log.d("page", "onChanged: " + article.getCurPage() + "/" + article.getPageCount());
                if (article.getPageCount() == article.getCurPage()) {
                    adapter.setShowFooter(false);
                } else {
                    adapter.setShowFooter(true);
                }
                page++;
                adapter.addList(article.getDatas());
            }
        }
    };

}
