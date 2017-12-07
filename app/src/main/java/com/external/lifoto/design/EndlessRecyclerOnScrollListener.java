package com.external.lifoto.design;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by zuojie on 17-12-6.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private boolean loading = false;
    private int[] lastVisibleItems;
    private int totalItemCount;

    private StaggeredGridLayoutManager layoutManager;

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = layoutManager.getItemCount();
        lastVisibleItems = layoutManager.findLastVisibleItemPositions(null);

        if (!loading && checkIsLast()) {
            loading = true;
            onLoadMore();
        }
    }

    public void setLoadFinish() {
        loading = false;
    }

    private boolean checkIsLast() {
        for (int lastItem : lastVisibleItems) {
            if (lastItem == totalItemCount - 1) {
                return true;
            }
        }
        return false;
    }

    public abstract void onLoadMore();
}
