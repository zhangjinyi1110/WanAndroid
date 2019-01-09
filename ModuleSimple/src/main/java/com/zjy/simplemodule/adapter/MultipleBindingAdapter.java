package com.zjy.simplemodule.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.zjy.simplemodule.adapter.model.MultipleModel;

import java.util.List;

public abstract class MultipleBindingAdapter extends BindingAdapter<MultipleModel, ViewDataBinding> {

    public MultipleBindingAdapter(Context context) {
        super(context);
    }

    public MultipleBindingAdapter(Context context, List<MultipleModel> list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if (type < TYPE_CONTENT)
            return type;
        if (isHeaderEnable())
            position--;
        return getItemType(position);
    }

    protected int getItemType(int position) {
        return getList().get(position).getType();
    }

    @SuppressWarnings("unchecked")
    public  <T extends MultipleModel> T getItemData(MultipleModel multipleModel) {
        return (T) multipleModel;
    }

    @SuppressWarnings("unchecked")
    public <B extends ViewDataBinding> B getItemBinding(ViewDataBinding binding) {
        return (B) binding;
    }
}
