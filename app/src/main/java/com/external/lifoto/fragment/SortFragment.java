package com.external.lifoto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.external.lifoto.R;
import com.external.lifoto.adapter.MainListAdapter;
import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.content.Api;
import com.external.lifoto.design.EndlessRecyclerOnScrollListener;
import com.external.lifoto.design.GridItemDivider;
import com.external.lifoto.design.RecyclerViewScrollDetector;
import com.external.lifoto.utils.NetUtil;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-11-30.
 */

public class SortFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private MainListAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;

    private RefreshAsyncTask refreshTask;
    private LoadMoreAsyncTask loadMoreTask;
    private EndlessRecyclerOnScrollListener scrollListener;
    private int currentPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sort, container, false);

        recyclerView = root.findViewById(R.id.list);
        refreshLayout = root.findViewById(R.id.refresh);

        initList();
        onRefresh();
        return root;
    }

    private void initList() {
        refreshLayout.setOnRefreshListener(this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(25);
//        recyclerView.addOnScrollListener(detector);
        recyclerView.addItemDecoration(new GridItemDivider(getResources().getDimensionPixelSize(R.dimen.staggeredDivider)));
        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                loadMoreTask = new LoadMoreAsyncTask();
                loadMoreTask.execute(Api.getSortUrl(getTitle(), ++currentPage));
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        refreshLayout.setRefreshing(true);
    }

    public void goToTop() {
        if (layoutManager != null)
            layoutManager.smoothScrollToPosition(recyclerView, null, 0);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        refreshTask = new RefreshAsyncTask();
        refreshTask.execute(Api.getSortUrl(getTitle(), 1));
    }

    class RefreshAsyncTask extends android.os.AsyncTask<String, Void, ArrayList<PhotoItem>> {

        @Override
        protected ArrayList<PhotoItem> doInBackground(String... strings) {
            String path = strings[0];
            try {
                return PhotoItem.parseItems(JSON.parseArray(NetUtil.GetEncHtml(path)));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<PhotoItem> photoItems) {
            if (adapter == null) {
                adapter = new MainListAdapter(SortFragment.this, photoItems);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setData(photoItems);
            }
            refreshLayout.setRefreshing(false);
        }
    }

    class LoadMoreAsyncTask extends android.os.AsyncTask<String, Void, ArrayList<PhotoItem>> {

        @Override
        protected void onPreExecute() {
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected ArrayList<PhotoItem> doInBackground(String... strings) {
            String path = strings[0];
            try {
                return PhotoItem.parseItems(JSON.parseArray(NetUtil.GetEncHtml(path)));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<PhotoItem> photoItems) {
            if (adapter != null) {
                adapter.insertData(photoItems);
            }
            scrollListener.setLoadFinish();
            refreshLayout.setRefreshing(false);
        }
    }

    private RecyclerViewScrollDetector detector = new RecyclerViewScrollDetector() {
        @Override
        public void onScrollUp() {
            hideSystemUI();
        }

        @Override
        public void onScrollDown() {
            showSystemUI();
        }
    };
}
