package com.example.tiantian.myapplication.fragment.main;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.activity.webview.WebViewActivity;
import com.example.tiantian.myapplication.data.main.NaviData;
import com.example.tiantian.myapplication.data.wxarticle.ArticleData;
import com.example.tiantian.myapplication.databinding.FragmentNavigationBinding;
import com.example.tiantian.myapplication.databinding.ItemNaviItemBinding;
import com.example.tiantian.myapplication.databinding.ItemNaviTitleBinding;
import com.example.tiantian.myapplication.viewmodel.main.MainViewModel;
import com.zjy.simplemodule.adapter.BindingAdapter;
import com.zjy.simplemodule.adapter.viewholder.SimpleViewHolder;
import com.zjy.simplemodule.base.BaseViewModel;
import com.zjy.simplemodule.base.fragment.AbsBindingFragment;
import com.zjy.simplemodule.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class NavigationFragment extends AbsBindingFragment<MainViewModel, FragmentNavigationBinding> {

    private BindingAdapter<NaviData, ItemNaviTitleBinding> titleAdapter;
    private BindingAdapter<NaviData, ItemNaviItemBinding> itemAdapter;
    private TypedArray typedArray;

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        typedArray = getResources().obtainTypedArray(R.array.textcolor);
        titleAdapter = new BindingAdapter<NaviData, ItemNaviTitleBinding>(getSelfActivity()) {
            @Override
            protected void convert(ItemNaviTitleBinding binding, NaviData naviData, int position) {
                binding.itemNaviTag.setText(naviData.getName());
                if (position == titleAdapter.getCurr())
                    binding.itemNaviTag.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                else
                    binding.itemNaviTag.setBackgroundColor(getResources().getColor(R.color.colorThinGray));
            }

            @Override
            protected void convert(ItemNaviTitleBinding binding, NaviData naviData, int position, List<Object> payloads) {
                switch ((String) payloads.get(0)) {
                    case "last":
                        binding.itemNaviTag.setBackgroundColor(getResources().getColor(R.color.colorThinGray));
                        break;
                    case "curr":
                        binding.itemNaviTag.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        break;
                }
            }

            @Override
            protected int getLayoutId(int type) {
                return R.layout.item_navi_title;
            }
        };
        titleAdapter.setLoadEnable(false);
        titleAdapter.setFooterEnable(false);
        binding.recyclerNavigationTitle.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
        binding.recyclerNavigationTitle.setAdapter(titleAdapter);

        itemAdapter = new BindingAdapter<NaviData, ItemNaviItemBinding>(getSelfActivity()) {
            @Override
            protected void convert(ItemNaviItemBinding binding, NaviData naviData, int position) {
                binding.itemNaviTitle.setText(naviData.getName());
                final List<ArticleData> arrayList = naviData.getArticles();
                int topSize = (int) getSelfActivity().getResources().getDimension(R.dimen.dp_5);
                int rightSize = (int) getSelfActivity().getResources().getDimension(R.dimen.dp_5);
                for (int i = 0; i < arrayList.size(); i++) {
                    ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, topSize, rightSize, 0);
                    TextView textView = new TextView(getSelfActivity());
                    textView.setText(arrayList.get(i).getTitle());
                    textView.setLayoutParams(layoutParams);
                    textView.setBackground(getResources().getDrawable(R.drawable.text_gray_bg));
                    textView.setTextColor(typedArray.getColor(i, 0));
                    final int finalI = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getSelfActivity(), WebViewActivity.class)
                                    .putExtra("title", arrayList.get(finalI).getTitle())
                                    .putExtra("url", arrayList.get(finalI).getLink()));
                        }
                    });
                    binding.itemNaviSub.addView(textView);
                }
            }

            @Override
            protected int getLayoutId(int type) {
                return R.layout.item_navi_item;
            }
        };
        itemAdapter.setFooterEnable(false);
        itemAdapter.setLoadEnable(false);
        binding.recyclerNavigationItem.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
        binding.recyclerNavigationItem.setAdapter(itemAdapter);
    }

    @Override
    protected void initEvent() {
        titleAdapter.setItemClickListener(new BindingAdapter.OnItemClickListener<NaviData, ItemNaviTitleBinding>() {
            @Override
            public void onItemClick(ItemNaviTitleBinding binding, NaviData naviData, int position) {
                NavigationFragment.this.binding.recyclerNavigationItem.smoothScrollToPosition(position);
                titleAdapter.notifyItemRangeChanged(titleAdapter.getLast(), 1, "last");
                titleAdapter.notifyItemRangeChanged(position, 1, "curr");
            }
        });
    }

    @Override
    protected void initData() {
        viewModel.getNaviList().observe(this, new Observer<List<NaviData>>() {
            @Override
            public void onChanged(@Nullable List<NaviData> naviData) {
                if (naviData != null) {
                    titleAdapter.addList(naviData);
                    itemAdapter.addList(naviData);
                }
            }
        });
        viewModel.getHomeNavi();
    }

}
