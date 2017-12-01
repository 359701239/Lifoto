package com.external.lifoto.design;

import android.support.v7.widget.RecyclerView;

/**
 * Created by zuojie on 17-12-1.
 */

public abstract class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {

    public abstract void onScrollUp();

    public abstract void onScrollDown();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) {
            onScrollUp();
        } else {
            onScrollDown();
        }
    }
}
