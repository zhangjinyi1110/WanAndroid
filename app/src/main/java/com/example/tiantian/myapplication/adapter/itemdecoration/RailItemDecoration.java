package com.example.tiantian.myapplication.adapter.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.tiantian.myapplication.R;

public class RailItemDecoration extends RecyclerView.ItemDecoration {

    private float railHeight;
    private float space;
    private Paint paint;

    public RailItemDecoration(Context context) {
        railHeight = context.getResources().getDimension(R.dimen.dp_1);
        space = context.getResources().getDimension(R.dimen.dp_8);
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorGray));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildLayoutPosition(view) == 0) {
            return;
        }
        outRect.top = (int) railHeight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int bottom = view.getTop();
            int top = (int) (bottom - railHeight);
            int end = view.getWidth();
            c.drawRect(space, top, end - space, bottom, paint);
        }
    }
}
