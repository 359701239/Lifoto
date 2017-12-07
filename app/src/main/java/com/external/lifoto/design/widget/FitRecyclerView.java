package com.external.lifoto.design.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.external.lifoto.utils.SystemBarUtil;

/**
 * Created by zuojie on 17-12-4.
 */

public class FitRecyclerView extends RecyclerView {
    public FitRecyclerView(Context context) {
        super(context);
        initBottomPadding(context);
    }

    public FitRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBottomPadding(context);
    }

    public FitRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBottomPadding(context);
    }

    private void initBottomPadding(Context context) {
        setPadding(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getPaddingBottom() + SystemBarUtil.getNavigationBarHeight(context));
    }
}
