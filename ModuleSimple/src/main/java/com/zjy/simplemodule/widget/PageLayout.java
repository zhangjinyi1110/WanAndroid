package com.zjy.simplemodule.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zjy.simplemodule.R;

public class PageLayout extends FrameLayout {

    public enum State {
        LOAD,
        ERROR,
        EMPTY,
        CONTENT
    }

    private View errorView;
    private View emptyView;
    private View loadView;
    private View contentView;
    private OnChangeStateListener changeStateListener;

    private PageLayout(@NonNull Context context) {
        super(context);
    }

    private void setErrorView(View errorView) {
        this.errorView = errorView;
        addView(this.errorView);
    }

    private void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        addView(this.emptyView);
    }

    private void setLoadView(View loadView) {
        this.loadView = loadView;
        addView(this.loadView);
    }

    private void setContentView(View contentView) {
        this.contentView = contentView;
        addView(this.contentView);
    }

    private void setChangeStateListener(OnChangeStateListener onChangeStateListener) {
        this.changeStateListener = onChangeStateListener;
    }

    public void showLoad() {
        changeState(State.LOAD);
    }

    public void showEmpty() {
        changeState(State.EMPTY);
    }

    public void showError() {
        changeState(State.ERROR);
    }

    public void showContent() {
        changeState(State.CONTENT);
    }

    private void changeState(State state) {
        View view;
        switch (state) {
            case LOAD:
                if (loadView != null)
                    view = loadView;
                else {
                    view = contentView;
                    state = State.CONTENT;
                }
                break;
            case EMPTY:
                if (emptyView != null)
                    view = emptyView;
                else {
                    view = contentView;
                    state = State.CONTENT;
                }
                break;
            case ERROR:
                if (errorView != null)
                    view = errorView;
                else {
                    view = contentView;
                    state = State.CONTENT;
                }
                break;
            default:
                view = contentView;
                break;
        }
        changeView(view);
        if (changeStateListener != null) {
            changeStateListener.onChangeState(view, state);
        }
    }

    private void changeView(View view) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v == view) {
                v.setVisibility(VISIBLE);
            } else {
                v.setVisibility(GONE);
            }
        }
    }

    public static class Builder {

        private boolean toolbar = false;//是否使用自定义标题栏
        private PageLayout pageLayout;
        private Context context;
        private OnErrorClickListener errorClickListener;
        private OnEmptyClickListener emptyClickListener;
        private boolean isDefault = false;

        public Builder(Context context) {
            this.context = context;
            pageLayout = new PageLayout(context);
        }

        public Builder setLoadView(View loadView) {
            pageLayout.setLoadView(loadView);
            return this;
        }

        public Builder setEmptyView(View emptyView) {
            pageLayout.setEmptyView(emptyView);
            return this;
        }

        public Builder setErrorView(View errorView) {
            pageLayout.setErrorView(errorView);
            return this;
        }

        //自定义标题栏时调用
        public Builder hasToolbar() {
            this.toolbar = true;
            return this;
        }

        //设置默认样式
        public Builder setDefault() {
            isDefault = true;
            return this;
        }

        public Builder setChangeStateListener(OnChangeStateListener changeStateListener) {
            pageLayout.setChangeStateListener(changeStateListener);
            return this;
        }

        public Builder setEmptyClickListener(OnEmptyClickListener emptyClickListener) {
            this.emptyClickListener = emptyClickListener;
            return this;
        }

        public Builder setErrorClickListener(OnErrorClickListener errorClickListener) {
            this.errorClickListener = errorClickListener;
            return this;
        }

        public PageLayout create(Object content) {
            if (content instanceof Activity) {
                ViewGroup viewGroup = ((Activity) content).findViewById(android.R.id.content);
                return create(viewGroup, viewGroup.getChildAt(toolbar ? 1 : 0));
            } else if (content instanceof Fragment) {
                View view = ((Fragment) content).getView();
                if (view != null) {
                    ViewGroup viewGroup = (ViewGroup) view.getParent();
                    return create(viewGroup, view);
                } else {
                    throw new NullPointerException("Content view cannot be empty.");
                }
            } else if (content instanceof View) {
                View view = (View) content;
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                return create(viewGroup, view);
//                if (viewGroup != null) {
//                    return create(viewGroup, view);
//                } else {
//                    throw new ClassCastException("Content view should have a parent control.");
//                }
            } else {
                throw new ClassCastException("Content should be an activity or fragment or view.");
            }
        }

        private PageLayout create(ViewGroup parent, View content) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) == content) {
                    parent.removeView(content);
                    pageLayout.setContentView(content);
                    parent.addView(pageLayout, i);
                    break;
                }
            }
            init();
            pageLayout.showLoad();
            return pageLayout;
        }

        private void init() {
            if (!isDefault) {
                if (pageLayout.errorView != null) {
                    pageLayout.errorView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (errorClickListener != null) {
                                if (!errorClickListener.onErrorClick()) {
                                    pageLayout.showLoad();
                                }
                            }
                        }
                    });
                }
                if (pageLayout.emptyView != null) {
                    pageLayout.emptyView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (emptyClickListener != null) {
                                if (!emptyClickListener.onEmptyClick()) {
                                    pageLayout.showLoad();
                                }
                            }
                        }
                    });
                }
                return;
            }
            if (pageLayout.loadView == null) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                linearLayout.setGravity(Gravity.CENTER);
                ProgressBar progressBar = new ProgressBar(context);
                linearLayout.addView(progressBar);
                TextView textView = new TextView(context);
                textView.setGravity(Gravity.CENTER);
                textView.setText("玩命加载中...");
                linearLayout.addView(textView);
                setLoadView(linearLayout);
            }
            if (pageLayout.errorView == null) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                linearLayout.setGravity(Gravity.CENTER);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.ic_launcher_background);
                linearLayout.addView(imageView);
                TextView textView = new TextView(context);
                textView.setGravity(Gravity.CENTER);
                textView.setText("暂时没有相关信息");
                linearLayout.addView(textView);
                TextView tip = new TextView(context);
                tip.setGravity(Gravity.CENTER);
                textView.setText("点击空白处刷新");
                linearLayout.addView(tip);
                setErrorView(linearLayout);
            }
            if (pageLayout.emptyView == null) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                linearLayout.setGravity(Gravity.CENTER);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.ic_launcher_background);
                linearLayout.addView(imageView);
                TextView textView = new TextView(context);
                textView.setGravity(Gravity.CENTER);
                textView.setText("页面加载出错啦");
                linearLayout.addView(textView);
                TextView tip = new TextView(context);
                tip.setGravity(Gravity.CENTER);
                textView.setText("点击空白处刷新");
                linearLayout.addView(tip);
                setEmptyView(linearLayout);
            }
            pageLayout.emptyView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emptyClickListener != null) {
                        if (!emptyClickListener.onEmptyClick()) {
                            pageLayout.showLoad();
                        }
                    }
                }
            });
            pageLayout.errorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (errorClickListener != null) {
                        if (!errorClickListener.onErrorClick()) {
                            pageLayout.showLoad();
                        }
                    }
                }
            });
        }
    }

    //状态切换接口
    public interface OnChangeStateListener {
        void onChangeState(View view, State state);
    }

    public interface OnErrorClickListener {
        boolean onErrorClick();//返回false则切换load状态；否则不切换状态，可自主切换
    }

    public interface OnEmptyClickListener {
        boolean onEmptyClick();//返回false则切换load状态；否则不切换状态，可自主切换
    }
}
