package com.example.tiantian.myapplication.activity.photo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.view.View;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.base.BaseActivity;
import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.databinding.ActivityPhotoBinding;
import com.example.tiantian.myapplication.fragment.photo.PhotoFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhotoActivity extends BaseActivity<ActivityPhotoBinding, BaseViewModel> {

    private List<String> path;
    private int curr;
    private List<PhotoFragment> fragments;

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView(final Bundle savedInstanceState) {
        path = getIntent().getStringArrayListExtra("path");
        curr = getIntent().getIntExtra("curr", 0);
        postponeEnterTransition();
        fragments = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            fragments.add(PhotoFragment.newInstance(path.get(i), i, i == curr));
        }
        binding.photoPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        binding.photoPager.setCurrentItem(curr, false);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void finishAfterTransition() {
        final int position = binding.photoPager.getCurrentItem();
        if(curr!=position){
            setResult(RESULT_OK, new Intent().putExtra("position", binding.photoPager.getCurrentItem()));
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    names.clear();
                    sharedElements.clear();
                    names.add(path.get(position));
                    sharedElements.put(path.get(position), fragments.get(position).getImageView());
                }
            });
        }
        super.finishAfterTransition();
    }
}
