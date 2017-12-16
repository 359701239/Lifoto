package com.external.lifoto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.external.lifoto.R;
import com.external.lifoto.adapter.MainListAdapter;
import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.content.Api;
import com.external.lifoto.design.EndlessRecyclerOnScrollListener;
import com.external.lifoto.design.GridItemDivider;
import com.external.lifoto.utils.NetUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zuojie on 17-11-30.
 */

public class SortFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = SortFragment.class.getName();
    private RecyclerView recyclerView;
    private MainListAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private TextView noNet;

    private LoadDataTask loadDataTask;
    private EndlessRecyclerOnScrollListener scrollListener;
    private int currentPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sort, container, false);

        recyclerView = root.findViewById(R.id.list);
        refreshLayout = root.findViewById(R.id.refresh);
        noNet = root.findViewById(R.id.noNet);

        noNet.setOnClickListener(this);

        initList();
        onRefresh();
        return root;
    }

    private void initList() {
        refreshLayout.setOnRefreshListener(this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(25);
        recyclerView.addItemDecoration(new GridItemDivider(getResources().getDimensionPixelSize(R.dimen.staggeredDivider)));
        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                Log.i(TAG, "onLoadMore");
                loadDataTask = new LoadDataTask(SortFragment.this, true);
                loadDataTask.execute(Api.getSortUrl(getTitle(), ++currentPage));
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void goToTop() {
        if (layoutManager != null)
            layoutManager.smoothScrollToPosition(recyclerView, null, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noNet:
                onRefresh();
                break;
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        refreshLayout.setRefreshing(true);
        noNet.setVisibility(View.GONE);
        loadDataTask = new LoadDataTask(this, false);
        loadDataTask.execute(Api.getSortUrl(getTitle(), currentPage));
    }

    static class LoadDataTask extends android.os.AsyncTask<String, Void, ArrayList<PhotoItem>> {
        private WeakReference<SortFragment> fragmentWeakReference;
        private boolean loadMore = false;

        private LoadDataTask(SortFragment fragment, boolean isLoadMore) {
            fragmentWeakReference = new WeakReference<>(fragment);
            loadMore = isLoadMore;
        }

        @Override
        protected void onPreExecute() {
            if (loadMore) {
                fragmentWeakReference.get().refreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected ArrayList<PhotoItem> doInBackground(String... strings) {
            String path = strings[0];
            try {
                return PhotoItem.parseItems(JSON.parseArray(NetUtil.GetEncHtml(path)));
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<PhotoItem> photoItems) {
            if (photoItems == null) {
                fragmentWeakReference.get().noNet.setVisibility(View.VISIBLE);
            } else if (fragmentWeakReference.get().adapter == null) {
                fragmentWeakReference.get().adapter = new MainListAdapter(fragmentWeakReference.get().activity, photoItems);
                fragmentWeakReference.get().recyclerView.setAdapter(fragmentWeakReference.get().adapter);
            } else {
                if (loadMore) {
                    fragmentWeakReference.get().adapter.insertData(photoItems);
                    fragmentWeakReference.get().scrollListener.setLoadFinish();
                } else {
                    fragmentWeakReference.get().adapter.setData(photoItems);
                }
            }
            fragmentWeakReference.get().refreshLayout.setRefreshing(false);
        }
    }
}
