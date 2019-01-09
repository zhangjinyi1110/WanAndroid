package com.example.tiantian.myapplication.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.tiantian.myapplication.R;

@SuppressLint("AppCompatCustomView")
public class SquareImageView extends ImageView {

    private Context context;
    private int squareType = TYPE_NOT;
    public static final int TYPE_NOT = 0x00;
    public static final int TYPE_WIDTH = 0x01;
    public static final int TYPE_HEIGHT = 0x02;

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView, defStyleAttr, 0);
        squareType = array.getInt(R.styleable.SquareImageView_square_type, TYPE_NOT);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;
        switch (squareType) {
            case TYPE_HEIGHT:
                width = heightMeasureSpec;
                break;
            case TYPE_WIDTH:
                height = widthMeasureSpec;
                break;
        }
        super.onMeasure(width, height);
    }

    public void setSquareType(int squareType) {
        this.squareType = squareType;
        requestLayout();
        invalidate();
    }

    public int getSquareType() {
        return squareType;
    }
}
