package com.external.lifoto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.external.lifoto.R;
import com.external.lifoto.adapter.MainListAdapter;
import com.external.lifoto.design.GridItemDivider;
import com.external.lifoto.design.RecyclerViewScrollDetector;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-11-30.
 */

public class SortFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private MainListAdapter adapter;
    private View mDecorView;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sort, container, false);

        mDecorView = activity.getWindow().getDecorView();
        recyclerView = root.findViewById(R.id.list);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            strings.add("item" + i);
        }
        adapter = new MainListAdapter(strings);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(detector);
        recyclerView.addItemDecoration(new GridItemDivider(activity, layoutManager, 12));

        return root;
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

    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
