package com.external.lifoto.model;

/**
 * Created by zuojie on 2018/1/18.
 */

public class DataSourceLocal implements DataSource {

    private static volatile DataSourceLocal instance;

    public static DataSourceLocal getInstance() {
        if (instance == null) {
            synchronized (DataSourceLocal.class) {
                if (instance == null) {
                    instance = new DataSourceLocal();
                }
            }
        }
        return instance;
    }

    @Override
    public void getData(boolean loadMore, LoadDataCallback callback) {
        callback.onDataLoadedError();//直接使用remoteData
    }

    @Override
    public void release() {
        instance = null;
    }
}
