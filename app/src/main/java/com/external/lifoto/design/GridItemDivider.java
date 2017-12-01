package com.external.lifoto.design;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zuojie on 17-3-7.
 */

public class GridItemDivider extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;

    public GridItemDivider(Context context, RecyclerView.LayoutManager layoutManager, float dp) {
        this.spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        this.spacing = dpToPx(context, dp);
    }

    private static int dpToPx(Context context, float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
        outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

        if (position < spanCount) { // top edge
            outRect.top = spacing;
        }
        outRect.bottom = spacing; // item bottom
    }
}