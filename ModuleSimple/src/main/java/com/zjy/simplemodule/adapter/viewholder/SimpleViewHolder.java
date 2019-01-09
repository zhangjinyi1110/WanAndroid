package com.zjy.simplemodule.adapter.viewholder;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public B binding;

    public SimpleViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public SimpleViewHolder(B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public <T extends View> T findViewById(int viewId) {
        return itemView.findViewById(viewId);
    }

    public TextView findTextViewById(int viewId) {
        return findViewById(viewId);
    }

    public EditText findEditViewById(int viewId) {
        return findViewById(viewId);
    }

    public ImageView findImageViewById(int viewId) {
        return findViewById(viewId);
    }

}
