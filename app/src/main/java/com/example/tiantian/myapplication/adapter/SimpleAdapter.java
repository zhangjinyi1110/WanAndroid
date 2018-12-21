package com.example.tiantian.myapplication.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.tiantian.myapplication.R;
import com.example.tiantian.myapplication.utils.SizeUtils;

import java.util.List;

public abstract class SimpleAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    private List<T> list;
    private Context context;
    private View footerView;
    private boolean showFooter;
    public final int TYPE_FOOTER = 0x02;
    public final int TYPE_CONTENT = 0x01;
    private OnItemClickListener itemClickListener;
    private OnLoadMoreListener loadMoreListener;

    public SimpleAdapter(Context context) {
        this(null, context);
    }

    public SimpleAdapter(List<T> list, Context context) {
        this.list = list;
        this.context = context;
        setShowFooter(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_FOOTER)
            return new ViewHolder(footerView);
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(i), viewGroup, false));
    }

    protected abstract int getLayoutId(int i);

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull SimpleAdapter.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() != 0)
            convert((B) holder.binding, list.get(position), position, payloads);
        else
            super.onBindViewHolder(holder, position, payloads);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull final SimpleAdapter.ViewHolder viewHolder, int i) {
        if (showFooter && getItemViewType(i) == TYPE_FOOTER) {
            if (loadMoreListener != null) {
                Log.d("SimpleAdapter", "onBindViewHolder: footer");
                loadMoreListener.onLoadMore();
            }
            return;
        }
        viewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(viewHolder.binding, viewHolder.getAdapterPosition());
                }
            }
        });
        convert((B) viewHolder.binding, list.get(i), i);
    }

    protected abstract void convert(B binding, T t, int position);

    protected void convert(B binding, T t, int position, List<Object> payloads) {
        if (showFooter && getItemViewType(position) == TYPE_FOOTER) {
            return;
        }
        convert(binding, t, position);
    }

    @Override
    public int getItemCount() {
        int count = list == null ? 0 : list.size();
        count += showFooter ? 1 : 0;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return showFooter && position == getItemCount() - 1 ? TYPE_FOOTER : TYPE_CONTENT;
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        if (this.list == null) {
            setList(list);
            return;
        }
        this.list.addAll(list);
        notifyItemRangeInserted(getItemCount() - 1, list.size());
    }

    public List<T> getList() {
        return list;
    }

    public void setShowFooter(boolean showFooter) {
        this.showFooter = showFooter;
        if (showFooter && footerView == null) {
            initLoadFooterView();
        }
    }

    public boolean isShowFooter() {
        return showFooter;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        Log.d("SimpleAdapter", "setLoadMoreListener: ");
        this.loadMoreListener = loadMoreListener;
    }

    private void initLoadFooterView() {
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        int paddingSize = SizeUtils.dp2px(context, 10);
        linearLayout.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        linearLayout.setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(context);
        int size = SizeUtils.dp2px(context, 18);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        progressBar.setLayoutParams(params);
        linearLayout.addView(progressBar);
        TextView textView = new TextView(context);
        textView.setText("正在加载数据...");
        textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
        textView.setTextSize(15);
        linearLayout.addView(textView);
        footerView = linearLayout;
    }

    public T getItemData(int position) {
        if (position >= list.size()) {
            throw new ArrayIndexOutOfBoundsException("this list count is " + list.size() + ", the position is " + position + ".");
        }
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public ViewHolder(@NonNull ViewDataBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewDataBinding binding, int position);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
