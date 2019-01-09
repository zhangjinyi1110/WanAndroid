package com.example.tiantian.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

    private final String TAG = this.getClass().getSimpleName();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = getPaddingStart() + getPaddingEnd();
        int height = getPaddingTop() + getPaddingBottom();
        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
            int w = view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int h = view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            maxHeight = maxHeight > h ? maxHeight : h;
            if (measureWidth < w + width) {
                if (w > measureWidth) {
                    w = measureWidth;
                }
                width = getPaddingStart() + getPaddingEnd();
                height += maxHeight;
            }
            width += w;
            maxWidth = maxWidth > width ? maxWidth : width;
            if (i == getChildCount() - 1) {
                height += maxHeight;
            }
        }
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(measureWidth, measureHeight);
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(measureWidth, height);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(maxWidth, measureHeight);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(maxWidth, height);
        } else {
            setMeasuredDimension(measureWidth, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getPaddingStart();
        int height = getPaddingTop();
        int maxHeight = 0;
        int maxWidth = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
            int w = view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int h = view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            maxHeight = maxHeight > h ? maxHeight : h;
            if (maxWidth < w + width) {
                if (w > maxWidth) {
                    w = maxWidth;
                }
                width = getPaddingStart();
                height += maxHeight;
                maxHeight = 0;
            }
            width += w;
            int viewL = width - w + layoutParams.leftMargin;
            int viewR = width - layoutParams.rightMargin;
            int viewT = height + layoutParams.topMargin;
            int viewB = ((height + h) > getMeasuredHeight() ? getMeasuredHeight() : (h + height)) - layoutParams.bottomMargin;
            view.layout(viewL, viewT, viewR, viewB);
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}