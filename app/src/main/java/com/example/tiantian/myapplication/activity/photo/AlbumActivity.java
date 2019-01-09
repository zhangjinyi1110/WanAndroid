package com.example.tiantian.myapplication.activity.photo;

import android.arch.lifecycle.Observer;
import android.databinding.ViewDataBinding;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.adapter.SimpleAdapter;
import com.example.tiantian.myapplication.base.BaseActivity;
import com.example.tiantian.myapplication.data.photo.Photo;
import com.example.tiantian.myapplication.databinding.ActivityAlbumBinding;
import com.example.tiantian.myapplication.databinding.ItemPhotoBinding;
import com.example.tiantian.myapplication.utils.SizeUtils;
import com.example.tiantian.myapplication.viewmodel.photo.AlbumViewModel;

import java.util.List;

public class AlbumActivity extends BaseActivity<ActivityAlbumBinding, AlbumViewModel> {

    private SimpleAdapter<Photo, ItemPhotoBinding> adapter;
    private int page = 1;
    private final int SIZE = 30;

    @Override
    public int getLayoutId() {
        return R.layout.activity_album;
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) binding.toolbar;
    }

    @Override
    protected void initToolbar(ActionBar actionBar) {
        super.initToolbar(actionBar);
        actionBar.setTitle("相册");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        adapter = new SimpleAdapter<Photo, ItemPhotoBinding>(this) {
            @Override
            protected int getLayoutId(int i) {
                return R.layout.item_photo;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void convert(final ItemPhotoBinding binding, Photo photo, int position) {
                binding.itemPhotoImage.setTransitionName(photo.getPath());
                binding.itemPhotoImage.setTag(null);
                Glide.with(getApplicationContext()).load(photo.getPath())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_banner2)
                                .error(R.drawable.ic_banner3))
                        .into(binding.itemPhotoImage);
                binding.itemPhotoImage.setTag(photo.getPath());
                binding.itemPhotoCheck.setText(String.valueOf(photo.getSize()));
            }
        };
        binding.albumRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int size = SizeUtils.dp2px(getApplicationContext(), 1);
                if (parent.getChildLayoutPosition(view) % 3 == 1) {
                    outRect.bottom = size;
                    outRect.left = size;
                    outRect.right = size;
                } else if (parent.getChildLayoutPosition(view) % 3 == 0) {
                    outRect.bottom = size;
                } else {
                    outRect.bottom = size;
                }
            }
        });
        binding.albumRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        binding.albumRecycler.setAdapter(adapter);
//        adapter.setShowLoadEnable(false);
    }

    @Override
    public void initEvent() {
        adapter.setLoadMoreListener(new SimpleAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                viewModel.getPhotoList(page, SIZE);
            }
        });
        adapter.setItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                Toast.makeText(getApplicationContext(), adapter.getItemData(position).getTime(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void initData() {
        viewModel.getPhotoList().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                if (photos != null) {
//                    if (photos.size() < size) {
//                        adapter.setShowLoadEnable(false);
//                    } else {
//                        adapter.setShowLoadEnable(true);
//                    }
                    adapter.addList(photos);
                    AlbumActivity.this.page++;
                    if(viewModel.getCursorCount() == adapter.getList().size()) {
                        adapter.setShowLoadEnable(false);
                    }
                    Log.d(TAG, "onChanged: " + adapter.getList().size());
                }
            }
        });
        viewModel.getPhotoList(page, SIZE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.cursorClose();
    }
}
