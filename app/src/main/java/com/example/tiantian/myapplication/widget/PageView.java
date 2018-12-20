package com.example.tiantian.myapplication.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tiantian.myapplication.R;

public class PageView extends FrameLayout {

    public enum State {
        LOAD,
        EMPTY,
        ERROR,
        CONTENT,
    }

    private Context context;
    private View errorView;
    private View loadView;
    private View emptyView;
    private View contentView;
    private OnPageStateChangeListener pageStateChangeListener;
    private State state;

    private PageView(Context context, View errorView, View loadView, View emptyView, View contentView, OnPageStateChangeListener pageStateChangeLinstener) {
        super(context);
        this.errorView = errorView;
        this.loadView = loadView;
        this.emptyView = emptyView;
        this.contentView = contentView;
        this.pageStateChangeListener = pageStateChangeLinstener;
        init();
    }

    private void init() {
        if (emptyView != null) {
            addView(emptyView);
        }
        if (errorView != null) {
            addView(errorView);
        }
        if (loadView != null) {
            addView(loadView);
        }
        addView(contentView);
        setState(loadView != null ? State.LOAD : State.CONTENT);
    }

    public void setState(State state) {
        this.state = state;
        View curr;
        switch (state) {
            case LOAD:
                if (loadView == null) {
                    curr = contentView;
                    this.state = State.CONTENT;
                } else {
                    curr = loadView;
                }
                break;
            case EMPTY:
                if (emptyView == null) {
                    curr = contentView;
                    this.state = State.CONTENT;
                } else {
                    curr = emptyView;
                }
                break;
            case ERROR:
                if (loadView == null) {
                    curr = contentView;
                    this.state = State.CONTENT;
                } else {
                    curr = errorView;
                }
                break;
            default:
                curr = contentView;
                this.state = State.CONTENT;
                break;
        }
        changeView(curr);
        if (pageStateChangeListener != null) {
            pageStateChangeListener.onPageStateChangeListener(curr, this.state);
        }
    }

    private void changeView(View curr) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view == curr) {
                view.setVisibility(VISIBLE);
            } else {
                view.setVisibility(GONE);
            }
        }
    }

    public State getState() {
        return state;
    }

    public static class Builder {

        private Context context;
        private View errorView;
        private View loadView;
        private View emptyView;
        private View contentView;
        private OnPageStateChangeListener pageStateChangeListener;
        private boolean isDefault = true;

        public Builder(Fragment context) {
            this.context = context.getContext();

        }

        public Builder setError(View error) {
            this.errorView = error;
            return this;
        }

        public Builder setLoad(View load) {
            this.loadView = load;
            return this;
        }

        public Builder setEmpty(View empty) {
            this.emptyView = empty;
            return this;
        }

        private void setContent(View content) {
            this.contentView = content;
//            return this;
        }

        public Builder setPageStateChangeLinstener(OnPageStateChangeListener pageStateChangeListener) {
            this.pageStateChangeListener = pageStateChangeListener;
            return this;
        }

        public Builder setDefault(boolean aDefault) {
            isDefault = aDefault;
            return this;
        }

        public PageView build(View content) {
            setContent(content);
            if (isDefault) {
                initDefault();
            }
            return new PageView(context, errorView, loadView, emptyView, contentView, pageStateChangeListener);
        }

        private void initDefault() {
            if (errorView == null) {
                errorView = new LinearLayout(context);
                errorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ((LinearLayout) errorView).setOrientation(LinearLayout.VERTICAL);
                ((LinearLayout) errorView).setGravity(Gravity.CENTER);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.ic_launcher_background);
                TextView textView = new TextView(context);
                textView.setText("加载失败");
                textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
                ((LinearLayout) errorView).addView(imageView);
                ((LinearLayout) errorView).addView(textView);
            }
            if (emptyView == null) {
                emptyView = new LinearLayout(context);
                emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ((LinearLayout) emptyView).setOrientation(LinearLayout.VERTICAL);
                ((LinearLayout) emptyView).setGravity(Gravity.CENTER);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.ic_launcher_background);
                TextView textView = new TextView(context);
                textView.setText("暂无数据");
                textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
                ((LinearLayout) emptyView).addView(imageView);
                ((LinearLayout) emptyView).addView(textView);
            }
            if (loadView == null) {
                loadView = new LinearLayout(context);
                loadView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ((LinearLayout) loadView).setOrientation(LinearLayout.VERTICAL);
                ((LinearLayout) loadView).setGravity(Gravity.CENTER);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.ic_launcher_background);
                TextView textView = new TextView(context);
                textView.setText("数据加载中...");
                textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
                ((LinearLayout) loadView).addView(imageView);
                ((LinearLayout) loadView).addView(textView);
            }
        }

    }

    public interface OnPageStateChangeListener {
        void onPageStateChangeListener(View view, State state);
    }

}
