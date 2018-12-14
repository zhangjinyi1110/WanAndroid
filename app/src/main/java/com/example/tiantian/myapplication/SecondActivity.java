package com.example.tiantian.myapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tiantian.myapplication.adapter.SimpleAdapter;
import com.example.tiantian.myapplication.adapter.itemdecoration.SimpleItemDecoration;
import com.example.tiantian.myapplication.api.WXArticleService;
import com.example.tiantian.myapplication.data.wxarticle.Article;
import com.example.tiantian.myapplication.data.wxarticle.ArticleData;
import com.example.tiantian.myapplication.databinding.ActivitySecondBinding;
import com.example.tiantian.myapplication.databinding.ArticleItemBinding;
import com.example.tiantian.myapplication.flowable.HttpResultTransformer;
import com.example.tiantian.myapplication.utils.HttpSubscriber;
import com.example.tiantian.myapplication.utils.RetrofitHelper;

public class SecondActivity extends AppCompatActivity {

    private SimpleAdapter<ArticleData, ArticleItemBinding> adapter;
    private int id;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySecondBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_second);
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
        adapter.setLoadMoreListener(new SimpleAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getData();
            }
        });
        adapter.setItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                startActivity(new Intent(getApplicationContext(), ThirdActivity.class).putExtra("url", adapter.getItemData(position).getLink()));
            }
        });
        binding.recyclerArticle.setAdapter(adapter);
        id = getIntent().getIntExtra("id", 0);
        getData();
    }

    private void getData(){
        RetrofitHelper.getInstance()
                .createService(WXArticleService.class)
                .getArticle(id, page)
                .compose(new HttpResultTransformer<Article>())
                .subscribe(new HttpSubscriber<Article>() {
                    @Override
                    public void success(Article article) {
                        page++;
                        adapter.addList(article.getDatas());
                        if(article.getDatas().size() + article.getOffset() == article.getTotal()) {
                            adapter.setShowFooter(false);
                        }
                    }
                });
    }
}
