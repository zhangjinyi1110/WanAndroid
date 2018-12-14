package com.example.tiantian.myapplication.adapter.itemdecoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.tiantian.myapplication.adapter.SimpleAdapter;
import com.example.tiantian.myapplication.utils.SizeUtils;

import java.util.Objects;

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private int left;
    private int right;
    private int top;
    private int bottom;

    public SimpleItemDecoration(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public SimpleItemDecoration(Context context) {
        this.context = context;
    }

    public SimpleItemDecoration(Context context, int left, int right, int top, int bottom) {
        this.context = context;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildLayoutPosition(view) == Objects.requireNonNull(parent.getAdapter()).getItemCount() - 1
                && ((SimpleAdapter) parent.getAdapter()).isShowFooter()) {
            return;
        }
        outRect.left = getSize(left);
        outRect.right = getSize(right);
        outRect.bottom = getSize(bottom);
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = getSize(top);
        }
    }

    private int getSize(int value) {
        if(context==null){
            return value;
        }
        return SizeUtils.dp2px(context, value);
    }
}
