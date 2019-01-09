package com.zjy.simplemodule.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjy.simplemodule.adapter.viewholder.SimpleViewHolder;

import java.util.List;

public abstract class BindingAdapter<T, B extends ViewDataBinding> extends BaseAdapter<T> {

    protected B binding;
    private OnItemClickListener<T, B> itemClickListener;

    public BindingAdapter(Context context) {
        super(context);
    }

    public BindingAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    protected boolean isBinding() {
        return true;
    }

    @Override
    protected B getBinding(ViewGroup viewGroup, int type) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutId(type), viewGroup, false);
        return binding;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void bindingConvert(SimpleViewHolder viewHolder, int i, List<Object> payloads) {
        if (payloads==null || payloads.isEmpty())
            convert((B) viewHolder.binding, getList().get(i), i);
        else
            convert((B) viewHolder.binding, getList().get(i), i, payloads);
    }

    protected void convert(B binding, T t, int position, List<Object> payloads) {
        convert(binding, t, position);
    }

    protected abstract void convert(B binding, T t, int position);

    @Override
    protected void convert(SimpleViewHolder holder, T t, int position) {
    }

    @Override
    protected SimpleViewHolder getViewHolder(View view) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean itemClick(ViewDataBinding binding, T t, int position) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick((B) binding, t, position);
            return true;
        } else {
            return false;
        }
    }

    public void setItemClickListener(OnItemClickListener<T, B> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener<T, B extends ViewDataBinding> {
        void onItemClick(B binding, T t, int position);
    }
}
