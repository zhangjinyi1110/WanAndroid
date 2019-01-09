package com.example.tiantian.myapplication.fragment.photo;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.base.BaseFragment;
import com.example.tiantian.myapplication.base.BaseViewModel;
import com.example.tiantian.myapplication.databinding.FragmentPhotoBinding;

public class PhotoFragment extends BaseFragment<FragmentPhotoBinding, BaseViewModel> {

    private String path;
    private int position;
    private boolean start;

    public static PhotoFragment newInstance(String path, int position, boolean start) {
        Bundle args = new Bundle();
        args.putString("path", path);
        args.putInt("position", position);
        args.putBoolean("start", start);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            path = bundle.getString("path");
            position = bundle.getInt("position");
            start = bundle.getBoolean("start");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView(Bundle savedInstanceState) {
        binding.photo.setTransitionName(path);
        Glide.with(getSelfActivity()).load(path).into(new DrawableImageViewTarget(binding.photo) {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                super.onResourceReady(resource, transition);
                if (start)
                    startPostponedEnterTransition();
            }
        });
        binding.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelfActivity().finishAfterTransition();
            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void startPostponedEnterTransition() {
        getSelfActivity().startPostponedEnterTransition();
    }

    public ImageView getImageView() {
        return binding.photo;
    }
}
