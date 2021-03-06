package com.external.lifoto.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.external.lifoto.DownloadActivity;
import com.external.lifoto.R;
import com.external.lifoto.SettingActivity;
import com.external.lifoto.design.widget.FitToolbar;
import com.external.lifoto.model.DataRepository;
import com.external.lifoto.model.DataSourceLocal;
import com.external.lifoto.model.DataSourceRemote;
import com.external.lifoto.presenter.DataPresenter;
import com.external.lifoto.utils.Dialog;
import com.external.lifoto.utils.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by zuojie on 17-11-30.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FitToolbar toolbar;
    private TextView title;

    private DataFragment dataFragment;
    private DataPresenter dataPresenter;

    private Handler handler;
    private static final int MSG_EXIT = 0;
    private static final int MSG_DOWNLOAD = 1;
    private static final int MSG_SETTING = 2;
    private static final int MSG_ABOUT = 3;
    private static final int MSG_DELAY = 250;

    private long exitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(this);

        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);

        init();
    }

    private void init() {
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ordinary.ttf"));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.setting:
                                handler.sendEmptyMessageDelayed(MSG_SETTING, MSG_DELAY);
                                break;
                            case R.id.about:
                                handler.sendEmptyMessageDelayed(MSG_ABOUT, MSG_DELAY);
                                break;
                            case R.id.exit:
                                handler.sendEmptyMessage(MSG_EXIT);
                                return true;
                        }
                        drawer.closeDrawers();
                        return true;
                    }
                });
        dataFragment = (DataFragment) getFragmentManager().findFragmentById(R.id.contentFrame);
        if (dataFragment == null) {
            dataFragment = new DataFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, dataFragment).commit();
        }
        dataPresenter = new DataPresenter(
                DataRepository.getInstance(
                        DataSourceRemote.getInstance(), DataSourceLocal.getInstance()), dataFragment);
    }

    @Override
    protected void onDestroy() {
        dataPresenter.stop();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action:
                dataFragment.goToTop();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.show(getApplicationContext(), getString(R.string.tip_exit));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                Toast.cancelToast();
            }
        }
    }

    private static class Handler extends android.os.Handler {
        private WeakReference<Activity> mActivity;

        private Handler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_EXIT:
                    mActivity.get().finish();
                    break;
                case MSG_DOWNLOAD:
                    mActivity.get().startActivity(new Intent(mActivity.get(), DownloadActivity.class));
                    break;
                case MSG_SETTING:
                    mActivity.get().startActivity(new Intent(mActivity.get(), SettingActivity.class));
                    break;
                case MSG_ABOUT:
                    Dialog.showSimpleDialog(mActivity.get(), R.string.activity_about, R.string.about_content);
                    break;
            }
        }
    }
}
