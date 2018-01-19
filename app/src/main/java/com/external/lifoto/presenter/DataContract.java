package com.external.lifoto.presenter;

import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.ui.BaseView;

import java.util.ArrayList;

/**
 * Created by zuojie on 2018/1/18.
 */

public interface DataContract {
    interface View extends BaseView<Presenter> {

        void setIsRefreshing(boolean isRefreshing);

        void showError();

        void setData(ArrayList<PhotoItem> items, boolean append);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadData(boolean refresh, boolean loadMore);
    }
}
