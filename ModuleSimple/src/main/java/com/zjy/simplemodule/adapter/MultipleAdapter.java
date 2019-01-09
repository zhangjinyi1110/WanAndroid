package com.zjy.simplemodule.adapter;

import android.content.Context;

import com.zjy.simplemodule.adapter.model.MultipleModel;

import java.util.List;

public abstract class MultipleAdapter extends BaseAdapter<MultipleModel> {

    public MultipleAdapter(Context context) {
        this(context, null);
    }

    public MultipleAdapter(Context context, List<MultipleModel> list) {
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

}
