package com.example.tiantian.myapplication.activity.wxarticle;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.adapter.SimpleAdapter;
import com.example.tiantian.myapplication.adapter.itemdecoration.SimpleItemDecoration;
import com.example.tiantian.myapplication.base.BaseActivity;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.example.tiantian.myapplication.databinding.ActivityChaptersBinding;
import com.example.tiantian.myapplication.databinding.ChaptersItemBinding;
import com.example.tiantian.myapplication.viewmodel.wxarticle.ChapterViewModel;
import com.example.tiantian.myapplication.widget.PageView;

import java.util.List;

public class ChaptersActivity extends BaseActivity<ActivityChaptersBinding, ChapterViewModel> {

    private final String TAG = ChaptersActivity.class.getSimpleName();
    private SimpleAdapter<Chapters, ChaptersItemBinding> adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_chapters;
    }

    @Override
    public void initData() {
        viewModel.getChapterList().observe(this, new Observer<List<Chapters>>() {
            @Override
            public void onChanged(@Nullable List<Chapters> chapters) {
                if (chapters != null) {
                    adapter.addList(chapters);
                    pageView.setState(PageView.State.CONTENT);
                }
            }
        });
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) binding.toolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("公众号列表");
    }

    @Override
    protected PageView getPageView() {
        return new PageView.Builder(this).create(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding.recyclerChapters.addItemDecoration(new SimpleItemDecoration(this, 13, 13, 8, 8));
        adapter = new SimpleAdapter<Chapters, ChaptersItemBinding>(this) {
            @Override
            protected int getLayoutId(int i) {
                return R.layout.chapters_item;
            }

            @Override
            protected void convert(ChaptersItemBinding binding, Chapters chapters, int position) {
                binding.setChapter(chapters);
            }
        };
        adapter.setShowFooter(false);
        binding.recyclerChapters.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerChapters.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
        adapter.setItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                startActivity(new Intent(getApplicationContext(), ArticleListActivity.class)
                        .putExtra("id", adapter.getItemData(position).getId())
                        .putExtra("title", adapter.getItemData(position).getName()));
            }
        });
    }

}
