package com.external.lifoto.model;

import android.support.annotation.NonNull;

import com.external.lifoto.bean.PhotoItem;

import java.util.ArrayList;

/**
 * Created by zuojie on 2018/1/18.
 */

public class DataRepository implements DataSource {
    private static DataRepository INSTANCE = null;

    private boolean needRefresh = false;
    private ArrayList<PhotoItem> cachedData;
    private final DataSource remoteDataSource, localDataSource;

    public static DataRepository getInstance(DataSource tasksRemoteDataSource,
                                             DataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }

    private DataRepository(@NonNull DataSource remoteDataSource, @NonNull DataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public void getData(final boolean loadMore, @NonNull final LoadDataCallback callback) {
        if (cachedData != null && cachedData.size() > 0 && !needRefresh && !loadMore) {
            callback.onDataLoaded(cachedData, false);
            System.out.println("@@@loadCacheData");
            return;
        }

        if (needRefresh) {
            getTasksFromRemoteDataSource(loadMore, callback);
        } else {
            System.out.println("@@@LocalData");
            localDataSource.getData(loadMore, new LoadDataCallback() {
                @Override
                public void onDataLoaded(ArrayList<PhotoItem> items, boolean append) {
                    setCacheData(items);
                    callback.onDataLoaded(items, append);
                }

                @Override
                public void onDataLoadedError() {
                    getTasksFromRemoteDataSource(loadMore, callback);
                }
            });
        }
    }

    public void refreshData() {
        needRefresh = true;
    }

    @Override
    public void release() {
        remoteDataSource.release();
        localDataSource.release();
    }

    private void setCacheData(ArrayList<PhotoItem> items) {
        if (cachedData == null) {
            cachedData = new ArrayList<>();
        }
        cachedData.addAll(items);
        needRefresh = false;
    }

    private void getTasksFromRemoteDataSource(boolean loadMore, @NonNull final LoadDataCallback callback) {
        System.out.println("@@@RemoteData");
        remoteDataSource.getData(loadMore, new LoadDataCallback() {
            @Override
            public void onDataLoaded(ArrayList<PhotoItem> items, boolean append) {
                setCacheData(items);
                callback.onDataLoaded(items, append);
            }

            @Override
            public void onDataLoadedError() {
                callback.onDataLoadedError();
            }
        });
    }
}
