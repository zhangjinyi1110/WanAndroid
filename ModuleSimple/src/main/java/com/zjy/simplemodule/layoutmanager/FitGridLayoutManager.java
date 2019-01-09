package com.zjy.simplemodule.layoutmanager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

public class FitGridLayoutManager extends GridLayoutManager {

    public FitGridLayoutManager(Context context, int spanCount, final boolean footer, final boolean header) {
        super(context, spanCount);
        setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (footer && position == getItemCount() - 1) {
                    return getSpanCount();
                } else if (header && position == 0) {
                    return getSpanCount();
                }
                return 1;
            }
        });
    }

}
