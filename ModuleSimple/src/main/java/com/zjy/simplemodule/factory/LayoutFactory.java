package com.zjy.simplemodule.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.zjy.simplemodule.R;

public class LayoutFactory implements LayoutInflater.Factory2 {
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return onCreateView(name, context, attrs);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = createView(name, context, attrs);
        if (view == null)
            return null;
        setColorStyle(view, context, attrs);
        return view;
    }

    private void setColorStyle(View view, Context context, AttributeSet attrs) {
        view.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        Log.e(getClass().getSimpleName(), "createView: " + name);
        try {
            if (name.indexOf('.') == -1) {
                if (name.equals("View")) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getSimpleName(), "createView: " + e.toString());
        }
        return view;
    }
}
