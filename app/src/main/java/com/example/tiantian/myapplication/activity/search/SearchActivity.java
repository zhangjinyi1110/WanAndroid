package com.example.tiantian.myapplication.activity.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.activity.photo.PhotoActivity;
import com.example.tiantian.myapplication.adapter.SimpleAdapter;
import com.example.tiantian.myapplication.api.MainService;
import com.example.tiantian.myapplication.base.BaseActivity;
import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.data.main.BannerData;
import com.example.tiantian.myapplication.databinding.ActivitySearchBinding;
import com.example.tiantian.myapplication.databinding.SearchItemBinding;
import com.example.tiantian.myapplication.flowable.HttpListResultTransformer;
import com.example.tiantian.myapplication.flowable.HttpResultTransformer;
import com.example.tiantian.myapplication.utils.HttpSubscriber;
import com.example.tiantian.myapplication.utils.RetrofitHelper;
import com.example.tiantian.myapplication.utils.RxLifecyclerUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends BaseActivity<ActivitySearchBinding, BaseViewModel> {

    private SimpleAdapter<BannerData, SearchItemBinding> adapter;
    private int curr = -1;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView(Bundle savedInstanceState) {
        if (ActivityCompat.checkSelfPermission(this, permissions[0])
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, permissions[1])
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, permissions, 0x01);
        }
        adapter = new SimpleAdapter<BannerData, SearchItemBinding>(this) {
            @Override
            protected int getLayoutId(int i) {
                return R.layout.search_item;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void convert(SearchItemBinding binding, BannerData bannerData, int position) {
                binding.searchItemImage.setTransitionName(bannerData.getImagePath());
                binding.searchItemImage.setTag(null);
                Glide.with(SearchActivity.this).load(Integer.valueOf(bannerData.getImagePath())).into(binding.searchItemImage);
                binding.searchItemImage.setTag(bannerData.getImagePath());
            }
        };
        binding.searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.searchRecycler.setAdapter(adapter);
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(final List<String> names, final Map<String, View> sharedElements) {
                if (SearchActivity.this.curr != -1) {
                    names.clear();
                    sharedElements.clear();
                    names.add(adapter.getItemData(curr).getImagePath());
                    sharedElements.put(adapter.getItemData(curr).getImagePath()
                            , binding.searchRecycler.findViewWithTag(adapter.getItemData(curr).getImagePath()));
                    for (String key : sharedElements.keySet()) {
                        Log.d(TAG, "onMapSharedElements: " + key + ":" + sharedElements.get(key));
                    }
                    SearchActivity.this.curr = -1;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == RESULT_OK) {
            curr = data.getIntExtra("position", 0);
            Log.d(TAG, "onActivityReenter: " + curr);
            binding.searchRecycler.scrollToPosition(curr);
            postponeEnterTransition();
            binding.searchRecycler.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    binding.searchRecycler.getViewTreeObserver().removeOnPreDrawListener(this);
                    binding.searchRecycler.requestLayout();
                    startPostponedEnterTransition();
                    return true;
                }
            });
        }
    }

    @Override
    public void initEvent() {
        adapter.setItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                BitmapDrawable drawable = (BitmapDrawable) adapter.getItemBinding(binding)
                        .searchItemImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                String path = Environment.getExternalStorageDirectory().getPath() + "/pic/" + System.currentTimeMillis() + ".jpg";
                try {
                    Log.d(TAG, "onItemClick: " + path);
                    File file = new File(path);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                    return;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onItemClick: " + e.toString());
                    if (e != null)
                        return;
                }
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < adapter.getList().size(); i++) {
                    list.add(adapter.getItemData(i).getImagePath());
                }
                Intent intent = new Intent(SearchActivity.this, PhotoActivity.class)
                        .putStringArrayListExtra("path", list)
                        .putExtra("curr", position);
                ActivityOptionsCompat compat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(SearchActivity.this
                                , adapter.getItemBinding(binding).searchItemImage
                                , adapter.getItemData(position).getImagePath());
                startActivityForResult(intent, 0x00, compat.toBundle());
            }
        });
    }

    @Override
    public void initData() {
//        RetrofitHelper.getInstance()
//                .createService(MainService.class)
//                .getBanner()
//                .compose(new HttpResultTransformer<List<BannerData>>(this))
//                .as(RxLifecyclerUtils.<List<BannerData>>bind(this))
//                .subscribe(new HttpSubscriber<List<BannerData>>() {
//                    @Override
//                    public void success(List<BannerData> bannerData) {
//                        adapter.setShowLoadEnable(false);
//                        if (bannerData != null)
//                            adapter.addList(bannerData);
//                    }
//                });
        final List<BannerData> list = new ArrayList<>();
        int[] banner = new int[]{R.drawable.ic_banner1, R.drawable.ic_banner2, R.drawable.ic_banner3
                , R.drawable.ic_banner4, R.drawable.ic_banner5, R.drawable.ic_banner6
                , R.drawable.ic_banner7, R.drawable.ic_banner8, R.drawable.ic_banner9};
        for (int i = 0; i < 9; i++) {
            BannerData bannerData = new BannerData();
            bannerData.setImagePath(banner[i] + "");
            list.add(bannerData);
        }
        adapter.addList(list);
    }
}
