package com.example.tiantian.myapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.tiantian.myapplication.adapter.SimpleAdapter;
import com.example.tiantian.myapplication.adapter.itemdecoration.SimpleItemDecoration;
import com.example.tiantian.myapplication.api.WXArticleService;
import com.example.tiantian.myapplication.data.wxarticle.Chapters;
import com.example.tiantian.myapplication.databinding.ActivityMainBinding;
import com.example.tiantian.myapplication.databinding.ChaptersItemBinding;
import com.example.tiantian.myapplication.flowable.HttpListResultTransformer;
import com.example.tiantian.myapplication.flowable.HttpResultTransformer;
import com.example.tiantian.myapplication.utils.HttpSubscriber;
import com.example.tiantian.myapplication.utils.RetrofitHelper;
import com.example.tiantian.myapplication.utils.RxLifecyclerUtils;
import com.example.tiantian.myapplication.utils.SizeUtils;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recyclerChapters.addItemDecoration(new SimpleItemDecoration(this, 13, 13, 8, 8));
        final SimpleAdapter<Chapters, ChaptersItemBinding> adapter = new SimpleAdapter<Chapters, ChaptersItemBinding>(this) {
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
        adapter.setItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                startActivity(new Intent(getApplicationContext(), SecondActivity.class).putExtra("id", adapter.getItemData(position).getId()));
            }
        });
        binding.recyclerChapters.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerChapters.setAdapter(adapter);
//        RetrofitHelper.getInstance()
//                .createService(WXArticleService.class)
//                .getChapters()
//                .compose(new HttpListResultTransformer<Chapters>())
//                .as(RxLifecyclerUtils.<List<Chapters>>bind(this))
//                .subscribe(new HttpSubscriber<List<Chapters>>() {
//                    @Override
//                    public void success(List<Chapters> chapters) {
//                        adapter.addList(chapters);
//                    }
//                });
        RetrofitHelper.getInstance()
                .createService(WXArticleService.class)
                .getChapters()
                .compose(new HttpResultTransformer<List<Chapters>>())
                .as(RxLifecyclerUtils.<List<Chapters>>bind(this))
                .subscribe(new HttpSubscriber<List<Chapters>>() {
                    @Override
                    public void success(List<Chapters> chapters) {
                        adapter.addList(chapters);
                    }
                });
    }
}
