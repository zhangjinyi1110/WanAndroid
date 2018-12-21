package com.example.tiantian.myapplication.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tiantian.myapplication.R;

public class PageView extends FrameLayout {

    public enum State {
        LOAD,
        EMPTY,
        ERROR,
        CONTENT,
    }

    private View content;
    private View error;
    private View empty;
    private View load;
    private State state;
    private OnPageStateChangeListener pageStateChangeListener;

    private PageView(@NonNull Context context) {
        super(context);
    }

    public void setState(State state) {
        this.state = state;
        changeState(state);
    }

    public void showContent() {
        setState(State.CONTENT);
    }

    public void showError() {
        setState(State.ERROR);
    }

    public void showEmpty() {
        setState(State.EMPTY);
    }

    public void showLoad() {
        setState(State.LOAD);
    }

    public State getState() {
        return state;
    }

    private void changeState(State state) {
        View curr;
        State currState;
        switch (state) {
            case LOAD:
                if (load != null) {
                    curr = load;
                    currState = state;
                } else {
                    curr = content;
                    currState = State.CONTENT;
                }
                break;
            case EMPTY:
                if (empty != null) {
                    curr = empty;
                    currState = state;
                } else {
                    curr = content;
                    currState = State.CONTENT;
                }
                break;
            case ERROR:
                if (error != null) {
                    curr = error;
                    currState = state;
                } else {
                    curr = content;
                    currState = State.CONTENT;
                }
                break;
            default:
                curr = content;
                currState = state;
                break;
        }
        changeView(curr);
        if (pageStateChangeListener != null) {
            pageStateChangeListener.onChangeListener(curr, currState);
        }
    }

    private void changeView(View curr) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (curr == view) {
                view.setVisibility(VISIBLE);
            } else {
                view.setVisibility(GONE);
            }
        }
    }

    public static class Builder {

        private Context context;
        private PageView pageView;
        private boolean isDefault = true;

        public Builder(Context context) {
            this.context = context;
            pageView = new PageView(this.context);
        }

        public Builder setEmpty(View empty) {
            pageView.empty = empty;
            return this;
        }

        public Builder setError(View error) {
            pageView.error = error;
            return this;
        }

        public Builder setLoad(View load) {
            pageView.load = load;
            return this;
        }

        public PageView create(Object object) {
            if (object instanceof Activity) {
                return create((Activity) object);
            } else if (object instanceof Fragment) {
                return create((Fragment) object);
            } else if (object instanceof View) {
                if (((View) object).getParent() instanceof ViewGroup)
                    return create((ViewGroup) ((View) object).getParent(), (View) object);
                else
                    throw new NullPointerException();
            } else {
                throw new NullPointerException("");
            }
        }

        private PageView create(ViewGroup viewGroup, View view) {
            int count = viewGroup.getChildCount();
            int index = 0;
            for (int i = 0; i < count; i++) {
                View v = viewGroup.getChildAt(i);
                if (v == view) {
                    index = i;
                }
            }
            setContent(viewGroup, view, index);
            return pageView;
        }

        private PageView create(Fragment fragment) {
            ViewGroup content = (ViewGroup) fragment.getView().getParent();
            create(content, fragment.getView());
            return pageView;
        }

        private PageView create(Activity activity) {
            ViewGroup content = activity.findViewById(android.R.id.content);
            View view = content.getChildAt(0);
            int index = 0;
            if (view instanceof ViewGroup) {
                content = (ViewGroup) view;
                view = content.getChildAt(1);
                index = 1;
            }
            setContent(content, view, index);
            return pageView;
        }

        private void setContent(ViewGroup content, @Nullable View view, int index) {
            if (view == null) {
                view = content;
            }
            pageView.content = view;
            pageView.removeAllViews();
            content.removeView(view);
            content.addView(pageView, index, view.getLayoutParams());
            initPage();
            pageView.setState(State.LOAD);
        }

        private void initPage() {
            if (isDefault) {
                if (pageView.empty == null) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(R.drawable.ic_launcher_background);
                    linearLayout.addView(imageView);
                    TextView textView = new TextView(context);
                    textView.setText("没有数据");
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    linearLayout.addView(textView);
                    setEmpty(linearLayout);
                }
                if (pageView.error == null) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(R.drawable.ic_launcher_background);
                    linearLayout.addView(imageView);
                    TextView textView = new TextView(context);
                    textView.setText("错误页面");
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    linearLayout.addView(textView);
                    setError(linearLayout);
                }
                if (pageView.load == null) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    ProgressBar progressBar = new ProgressBar(context);
                    linearLayout.addView(progressBar);
                    TextView textView = new TextView(context);
                    textView.setText("正在加载...");
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    linearLayout.addView(textView);
                    setLoad(linearLayout);
                }
            }
            pageView.addView(pageView.content);
            if (pageView.error != null) {
                pageView.addView(pageView.error);
            }
            if (pageView.empty != null) {
                pageView.addView(pageView.empty);
            }
            if (pageView.load != null) {
                pageView.addView(pageView.load);
            }
        }

        public Builder setPageStateChangeListener(OnPageStateChangeListener pageStateChangeListener) {
            pageView.pageStateChangeListener = pageStateChangeListener;
            return this;
        }

        public Builder setDefault(boolean aDefault) {
            isDefault = aDefault;
            return this;
        }
    }

    public interface OnPageStateChangeListener {
        void onChangeListener(View view, State state);
    }

}
