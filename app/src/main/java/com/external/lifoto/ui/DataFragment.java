package com.external.lifoto.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.external.lifoto.R;
import com.external.lifoto.adapter.MainListAdapter;
import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.design.EndlessRecyclerOnScrollListener;
import com.external.lifoto.design.GridItemDivider;
import com.external.lifoto.fragment.BaseFragment;
import com.external.lifoto.presenter.DataContract;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-11-30.
 */

public class DataFragment extends BaseFragment implements DataContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = DataFragment.class.getName();
    private RecyclerView recyclerView;
    private MainListAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private TextView noNet;

    private EndlessRecyclerOnScrollListener scrollListener;

    private DataContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sort, container, false);

        recyclerView = root.findViewById(R.id.list);
        refreshLayout = root.findViewById(R.id.refresh);
        noNet = root.findViewById(R.id.noNet);

        noNet.setOnClickListener(this);

        initList();
        if (presenter != null) {
            presenter.start();
        }
        return root;
    }

    private void initList() {
        refreshLayout.setOnRefreshListener(this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(25);
        recyclerView.addItemDecoration(new GridItemDivider(getResources().getDimensionPixelSize(R.dimen.staggeredDivider)));
        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                System.out.println("@@@onLoadMore");
                if (presenter != null) {
                    System.out.println("@@@loadData");
                    presenter.loadData(false, true);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void goToTop() {
        if (layoutManager != null) {
            layoutManager.smoothScrollToPosition(recyclerView, null, 0);
        }
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
        presenter.loadData(true, false);
    }

    @Override
    public void setPresenter(DataContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setIsRefreshing(boolean isRefreshing) {
        refreshLayout.setRefreshing(true);
        noNet.setVisibility(View.GONE);
    }

    @Override
    public void setData(ArrayList<PhotoItem> items, boolean append) {
        noNet.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
        if (adapter == null) {
            adapter = new MainListAdapter(getActivity());
            recyclerView.setAdapter(adapter);
        }
        adapter.setData(items, append);
        if (append) {
            scrollListener.setLoadFinish();
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showError() {
        noNet.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(false);
    }
}
