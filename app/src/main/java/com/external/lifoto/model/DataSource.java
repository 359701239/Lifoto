package com.external.lifoto.model;

import com.external.lifoto.bean.PhotoItem;

import java.util.ArrayList;

/**
 * Created by zuojie on 2018/1/18.
 */

public interface DataSource {

    interface LoadDataCallback {

        void onDataLoaded(ArrayList<PhotoItem> items, boolean append);

        void onDataLoadedError();
    }

    void getData(boolean loadMore, LoadDataCallback callback);

    void release();
}
