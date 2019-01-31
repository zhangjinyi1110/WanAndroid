package com.zjy.simplemodule.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zjy.simplemodule.R;
import com.zjy.simplemodule.adapter.viewholder.SimpleViewHolder;
import com.zjy.simplemodule.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<SimpleViewHolder> {

    private Context context;
    private List<T> list;
    private RecyclerView recyclerView;
    private OnItemClickListener<T> itemClickListener;
    private OnLoadMoreListener loadMoreListener;
    private boolean headerEnable;
    private boolean footerEnable;
    private boolean loadEnable;
    //    private boolean hasData = false;
    private boolean isLoad = false;
    private boolean footerFullSpan = true;
    private boolean headerFullSpan = true;
    private View headView;
    private View footerView;
    private View loadView;
    private int last = 0;
    private int curr = 0;
    public final int TYPE_HEADER = 0x00;
    public final int TYPE_CONTENT = 0x03;
    public final int TYPE_LOAD = 0x01;
    public final int TYPE_FOOTER = 0x02;
    protected final String TAG = getClass().getSimpleName();

    public BaseAdapter(Context context) {
        this(context, null);
    }

    public BaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        if (list == null) {
//            hasData = true;
            isLoad = true;
            this.list = new ArrayList<>();
        }
        setFooterEnable(true);
        setLoadEnable(true);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @LayoutRes
    protected abstract int getLayoutId(int type);

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case TYPE_FOOTER:
                return new SimpleViewHolder(footerView);
            case TYPE_HEADER:
                return new SimpleViewHolder(headView);
            case TYPE_LOAD:
                return new SimpleViewHolder(loadView);
        }
        if (isBinding()) {
            return new SimpleViewHolder(getBinding(viewGroup, i));
        }
        return getViewHolder(LayoutInflater.from(context).inflate(getLayoutId(i), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position, @NonNull List<Object> payloads) {
        int type = getItemViewType(position);
        if (type < TYPE_CONTENT) {
            if (type == TYPE_LOAD) {
                if (loadMoreListener != null && !isLoad) {
                    isLoad = true;
                    loadMoreListener.onLoadMore();
                }
            } else if (type == TYPE_FOOTER) {
                if (footerView.getTag() != null && footerView.getTag().equals("defaultFooterView")) {
                    String text = "-----我是底线-----";
                    if (list == null || list.isEmpty())
                        text = "-----暂无数据-----";
                    ((TextView) ((ViewGroup) footerView).getChildAt(0)).setText(text);
                }
            }
            return;
        }
        if (headerEnable) {
            position--;
        }
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads);
        else {
            if (!isBinding())
                convert(holder, list.get(position), position, payloads);
            else
                bindingConvert(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleViewHolder simpleViewHolder, int i) {
        final int position = i;
        simpleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last = curr;
                curr = position;
                if (!itemClick(simpleViewHolder.binding, list.get(position), position)) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(simpleViewHolder.itemView, list.get(position), position);
                    }
                }
            }
        });
        if (!isBinding())
            convert(simpleViewHolder, list.get(i), i);
        else
            bindingConvert(simpleViewHolder, i, null);
    }

    protected void bindingConvert(SimpleViewHolder viewHolder, int i, List<Object> payloads) {
    }

    protected void convert(SimpleViewHolder holder, T t, int position, List<Object> payloads) {
        convert(holder, t, position);
    }

    protected abstract void convert(SimpleViewHolder holder, T t, int position);

    protected SimpleViewHolder getViewHolder(View view) {
        return new SimpleViewHolder(view);
    }

    protected boolean itemClick(ViewDataBinding binding, T t, int position) {
        return false;
    }

    protected boolean isBinding() {
        return false;
    }

    protected ViewDataBinding getBinding(ViewGroup viewGroup, int type) {
        return null;
    }

    @Override
    public int getItemCount() {
        int listCount = list == null ? 0 : list.size();
        return listCount + ((footerEnable || loadEnable) ? 1 : 0) + (headerEnable ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && headerEnable) {
            return TYPE_HEADER;
        } else if (position == getItemCount() - 1 && (footerEnable || loadEnable)) {
            if (loadEnable/* && hasData*/) {
                return TYPE_LOAD;
            } else {
                return TYPE_FOOTER;
            }
        }
        return TYPE_CONTENT;
    }

    public void setList(List<T> list) {
//        hasData = true;
        this.list = list;
        notifyDataSetChanged();
        isLoad = false;
    }

    public void addList(List<T> list) {
        if (this.list == null) {
            setList(list);
            return;
        }
        this.list.addAll(list);
        notifyItemRangeChanged(getItemCount() - 1, list.size());
        isLoad = false;
    }

    public List<T> getList() {
        return list;
    }

    public void clear() {
        if (list != null) {
            int size = list.size();
            list.clear();
            notifyItemRangeRemoved(0, size);
//            notifyDataSetChanged();
        }
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setFooterEnable(boolean footerEnable) {
        this.footerEnable = footerEnable;
        if (footerEnable)
            initFooterView();
    }

    public void setLoadEnable(boolean loadEnable) {
        this.loadEnable = loadEnable;
        if (loadEnable)
            initLoadView();
    }

    public void setHeaderEnable(boolean headerEnable) {
        this.headerEnable = headerEnable;
        if (headerEnable)
            initHeaderView();
    }

    public boolean isFooterEnable() {
        return footerEnable;
    }

    public boolean isHeaderEnable() {
        return headerEnable;
    }

    public boolean isLoadEnable() {
        return loadEnable;
    }

    private void initFooterView() {
        if (footerView != null) {
            return;
        }
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = SizeUtils.dp2px(context, 10);
        linearLayout.setPadding(padding, padding, padding, padding);
        linearLayout.setGravity(Gravity.CENTER);
        TextView textView = new TextView(context);
        textView.setText("-----我是底线-----");
        textView.setTextSize(15);
        textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
        linearLayout.addView(textView);
        linearLayout.setTag("defaultFooterView");
        footerView = linearLayout;
    }

    private void initLoadView() {
        if (loadView != null) {
            return;
        }
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = SizeUtils.dp2px(context, 10);
        linearLayout.setPadding(padding, padding, padding, padding);
        linearLayout.setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(context);
        int size = SizeUtils.dp2px(context, 15);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        linearLayout.addView(progressBar);
        TextView textView = new TextView(context);
        textView.setText("玩命加载中...");
        textView.setTextSize(15);
        textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
        linearLayout.addView(textView);
        loadView = linearLayout;
    }

    private void initHeaderView() {
        if (headView != null) {
            return;
        }
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = SizeUtils.dp2px(context, 10);
        linearLayout.setPadding(padding, padding, padding, padding);
        linearLayout.setGravity(Gravity.CENTER);
        TextView textView = new TextView(context);
        textView.setText("-------我是顶部-------");
        textView.setTextSize(15);
        textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
        linearLayout.addView(textView);
        headView = linearLayout;
    }

    public void setLoadView(View loadView) {
        this.loadView = loadView;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public Context getContext() {
        return context;
    }

    public void setFooterFullSpan(boolean footerFullSpan) {
        this.footerFullSpan = footerFullSpan;
    }

    public void setHeaderFullSpan(boolean headerFullSpan) {
        this.headerFullSpan = headerFullSpan;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getLast() {
        return last;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull SimpleViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.getLayoutPosition() == getItemCount() - 1 && footerFullSpan) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams != null) {
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                }
            }
        }
        if (holder.getLayoutPosition() == 0 && headerFullSpan) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams != null) {
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                }
            }
        }
    }

    public void setItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T t, int position);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
