package com.example.tiantian.myapplication.fragment.main;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.activity.tree.TreeActivity;
import com.example.tiantian.myapplication.adapter.itemdecoration.RailItemDecoration;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.example.tiantian.myapplication.databinding.FragmentSystemBinding;
import com.example.tiantian.myapplication.databinding.ItemSystemBinding;
import com.example.tiantian.myapplication.viewmodel.main.MainViewModel;
import com.zjy.simplemodule.adapter.BindingAdapter;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;

import java.util.ArrayList;
import java.util.List;

public class SystemFragment extends AbsBindingFragment<MainViewModel, FragmentSystemBinding> {

    private BindingAdapter<Chapters, ItemSystemBinding> adapter;

    public static SystemFragment newInstance() {
        return new SystemFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_system;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        final int rightSize = (int) getSelfActivity().getResources().getDimension(R.dimen.dp_8);
        final int topSize = (int) getSelfActivity().getResources().getDimension(R.dimen.dp_5);
        binding.refreshSystem.setRefreshing(true);
        adapter = new BindingAdapter<Chapters, ItemSystemBinding>(getSelfActivity()) {
            @Override
            protected void convert(ItemSystemBinding binding, final Chapters chapters, int position) {
                binding.itemSystemTitle.setText(chapters.getName());
                binding.itemSystemLabelGroup.removeAllViews();
                for (int i = 0; i < chapters.getChildren().size(); i++) {
                    Chapters c = chapters.getChildren().get(i);
                    TextView textView = new TextView(getSelfActivity());
                    ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, topSize, rightSize, 0);
                    textView.setLayoutParams(layoutParams);
                    SpannableString spannableString = new SpannableString(c.getName());
                    spannableString.setSpan(new UnderlineSpan(), 0, c.getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    textView.setText(spannableString);
                    final int finalI = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getSelfActivity(), TreeActivity.class)
                                    .putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) chapters.getChildren())
                                    .putExtra("position", finalI)
                                    .putExtra("title", chapters.getName()));
                        }
                    });
                    binding.itemSystemLabelGroup.addView(textView);
                }
            }

            @Override
            protected int getLayoutId(int type) {
                return R.layout.item_system;
            }
        };
        binding.recyclerSystem.addItemDecoration(new RailItemDecoration(getSelfActivity()));
        binding.recyclerSystem.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
        binding.recyclerSystem.setAdapter(adapter);
        adapter.setLoadEnable(false);
        adapter.setFooterEnable(false);
    }

    @Override
    protected void initEvent() {
        adapter.setItemClickListener(new BindingAdapter.OnItemClickListener<Chapters, ItemSystemBinding>() {
            @Override
            public void onItemClick(ItemSystemBinding binding, Chapters chapters, int position) {
                startActivity(new Intent(getSelfActivity(), TreeActivity.class)
                        .putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) chapters.getChildren())
                        .putExtra("title", chapters.getName())
                        .putExtra("position", 0));
            }
        });
        binding.refreshSystem.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.refreshSystem.setRefreshing(true);
                adapter.setLoad(true);
                viewModel.getHomeChapter();
            }
        });
    }

    @Override
    protected void initData() {
        viewModel.getHomeChapter();
    }

    @Override
    protected void observe() {
        viewModel.getChapterList().observe(this, new Observer<List<Chapters>>() {
            @Override
            public void onChanged(@Nullable List<Chapters> chapters) {
                if (chapters != null) {
                    if (binding.refreshSystem.isRefreshing())
                        binding.refreshSystem.setRefreshing(false);
                    adapter.setList(chapters);
                }
            }
        });
    }

    @Override
    public boolean isLazy() {
        return false;
    }
}
