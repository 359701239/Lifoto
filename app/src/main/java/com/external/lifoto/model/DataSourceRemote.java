package com.external.lifoto.model;

import com.alibaba.fastjson.JSON;
import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.content.Api;
import com.external.lifoto.utils.NetUtil;

import java.util.ArrayList;

/**
 * Created by zuojie on 2018/1/18.
 */

public class DataSourceRemote implements DataSource {

    private static volatile DataSourceRemote instance;
    private int currentPage;
    private LoadDataTask loadDataTask;
    private LoadDataCallback callback;
    private boolean loadMore;

    public static DataSourceRemote getInstance() {
        if (instance == null) {
            synchronized (DataSourceRemote.class) {
                if (instance == null) {
                    instance = new DataSourceRemote();
                }
            }
        }
        return instance;
    }

    @Override
    public void getData(boolean loadMore, LoadDataCallback callback) {
        this.loadMore = loadMore;
        this.callback = callback;
        if (loadMore) {
            currentPage++;
        } else {
            currentPage = 1;
        }
        loadDataTask = new LoadDataTask();
        loadDataTask.execute(Api.getSortUrl(currentPage));
    }

    @Override
    public void release() {
        instance = null;
        callback = null;
        if (loadDataTask != null && !loadDataTask.isCancelled()) {
            loadDataTask.cancel(true);
        }
    }

    class LoadDataTask extends android.os.AsyncTask<String, Void, ArrayList<PhotoItem>> {

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
            if (callback != null) {
                callback.onDataLoaded(photoItems, loadMore);
            }
        }
    }
}
