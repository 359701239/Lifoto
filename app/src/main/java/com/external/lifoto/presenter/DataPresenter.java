package com.external.lifoto.presenter;

import android.support.annotation.NonNull;

import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.model.DataRepository;
import com.external.lifoto.model.DataSource;

import java.util.ArrayList;

/**
 * Created by zuojie on 2018/1/18.
 */

public class DataPresenter implements DataContract.Presenter {

    private final DataRepository dataRepository;
    private final DataContract.View view;

    public DataPresenter(@NonNull DataRepository dataRepository, @NonNull DataContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        loadData(false, false);
    }

    @Override
    public void stop() {
        if (dataRepository != null) {
            dataRepository.release();
        }
    }

    @Override
    public void loadData(boolean update, boolean loadMore) {
        view.setIsRefreshing(true);
        if (update) {
            dataRepository.refreshData();
        }

        dataRepository.getData(loadMore, new DataSource.LoadDataCallback() {

            @Override
            public void onDataLoaded(ArrayList<PhotoItem> items, boolean append) {
                if (view.isActive()) {
                    view.setData(items, append);
                }
            }

            @Override
            public void onDataLoadedError() {
                if (view.isActive()) {
                    view.showError();
                }
            }
        });
    }
}
