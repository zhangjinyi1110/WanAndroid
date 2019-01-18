package com.example.tiantian.myapplication.activity.search;

import android.arch.lifecycle.Observer;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.data.search.SearchHot;
import com.example.tiantian.myapplication.databinding.ActivitySearchBinding;
import com.example.tiantian.myapplication.databinding.ItemSearchBinding;
import com.example.tiantian.myapplication.viewmodel.search.SearchViewModel;
import com.zjy.simplemodule.adapter.BindingAdapter;
import com.zjy.simplemodule.base.activity.AbsBindingActivity;
import com.zjy.simplemodule.utils.DiskCache;
import com.zjy.simplemodule.utils.KeyBroadUtils;

import java.util.List;

public class SearchActivity extends AbsBindingActivity<SearchViewModel, ActivitySearchBinding> {

    private TypedArray colors;
    private BindingAdapter<String, ItemSearchBinding> adapter;
    public static final int TYPE_CHANGE = 0x00;
    public static final int TYPE_DELETE = 0x01;
    public static final int TYPE_CLEAR = 0x02;

    @Override
    protected Toolbar getToolBar() {
        return binding.searchToolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        adapter = new BindingAdapter<String, ItemSearchBinding>(getSelf()) {
            @Override
            protected void convert(ItemSearchBinding binding, final String s, int position) {
                binding.itemHistoryDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.updateHistory(s, TYPE_DELETE);
                    }
                });
                binding.itemHistoryText.setText(s);
            }

            @Override
            protected int getLayoutId(int type) {
                return R.layout.item_search;
            }
        };
        adapter.setLoadEnable(false);
        adapter.setFooterEnable(false);
        binding.recyclerSearchHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerSearchHistory.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        adapter.setItemClickListener(new BindingAdapter.OnItemClickListener<String, ItemSearchBinding>() {
            @Override
            public void onItemClick(ItemSearchBinding binding, String s, int position) {
                viewModel.search(s);
            }
        });
        binding.searchHistoryClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.updateHistory(null, TYPE_CLEAR);
            }
        });
        binding.editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                viewModel.search(v.getText().toString());
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        colors = getResources().obtainTypedArray(R.array.textcolor);
        viewModel.getHotList().observe(this, new Observer<List<SearchHot>>() {
            @Override
            public void onChanged(@Nullable List<SearchHot> searchHots) {
                if (searchHots != null) {
                    for (int i = 0; i < searchHots.size(); i++) {
                        final SearchHot hot = searchHots.get(i);
                        TextView textView = new TextView(getSelf());
                        textView.setText(hot.getName());
                        textView.setBackground(getResources().getDrawable(R.drawable.text_primary_bg));
                        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        int size = (int) getResources().getDimension(R.dimen.dp_5);
                        layoutParams.setMargins(size, size, size, size);
                        textView.setLayoutParams(layoutParams);
                        textView.setPadding(size, size, size, size);
                        textView.setTextColor(colors.getColor(i, 0));
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewModel.search(hot.getName());
                            }
                        });
                        binding.searchHot.addView(textView);
                    }
                }
            }
        });
        viewModel.getHistoryList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                if (strings != null) {
                    adapter.setList(strings);
                }
            }
        });
        viewModel.getHistory();
        viewModel.getSearchHot();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_option_search:
                binding.editSearch.requestFocus();
                viewModel.search(binding.editSearch.getText().toString());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.editSearch.post(new Runnable() {
            @Override
            public void run() {
                KeyBroadUtils.show(getApplicationContext(), binding.editSearch);
            }
        });
    }

    @Override
    protected boolean isHideKeyboard() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DiskCache.close();
    }
}
