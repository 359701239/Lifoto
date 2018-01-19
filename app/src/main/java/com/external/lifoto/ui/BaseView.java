package com.external.lifoto.ui;

import com.external.lifoto.presenter.BasePresenter;

/**
 * Created by zuojie on 2018/1/18.
 */

public interface BaseView<T> {

    void setPresenter(T presenter);
}