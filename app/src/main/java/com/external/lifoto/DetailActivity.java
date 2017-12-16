package com.external.lifoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.external.lifoto.bean.PhotoDetail;
import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.content.Api;
import com.external.lifoto.design.widget.ScaleImageView;
import com.external.lifoto.utils.DownloadService;
import com.external.lifoto.utils.DownloadEvent;
import com.external.lifoto.utils.NetUtil;
import com.external.lifoto.utils.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zuojie on 17-12-8.
 */

public class DetailActivity extends Activity implements View.OnClickListener {

    private ScaleImageView thumb;
    private PhotoItem item;

    private TextView mTvPhotoSize;
    private TextView mTvPhotoExposure;
    private TextView mTvPhotoAperture;
    private TextView mTvPhotoFocal;
    private TextView mTvPhotoIso;
    private TextView mTvPhotoModel;

    private ProgressBar progressBar, loadattr;
    private ImageView download;
    private CardView container;
    private ImageView back;
    private LinearLayout attrs;

    private boolean downloading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");

        thumb = findViewById(R.id.thumb);

        mTvPhotoSize = findViewById(R.id.tv_attr_size);
        mTvPhotoExposure = findViewById(R.id.tv_attr_exposure);
        mTvPhotoAperture = findViewById(R.id.tv_attr_aperture);
        mTvPhotoFocal = findViewById(R.id.tv_attr_focal);
        mTvPhotoIso = findViewById(R.id.tv_attr_iso);
        mTvPhotoModel = findViewById(R.id.tv_attr_model);

        progressBar = findViewById(R.id.progressbar);
        download = findViewById(R.id.download);
        container = findViewById(R.id.container);
        back = findViewById(R.id.back);
        loadattr = findViewById(R.id.loadattr);
        attrs = findViewById(R.id.attrs);

        initView();
        initData();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        container.setCardBackgroundColor(Color.parseColor(item.getColor()));
        thumb.setInitSize(item.getWidth(), item.getHeight());
        Glide.with(this).load(Uri.parse(item.getUrl_small()))
                .asBitmap()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(thumb);
        download.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void initData() {
        Observable.create(new Observable.OnSubscribe<PhotoDetail>() {
            @Override
            public void call(Subscriber<? super PhotoDetail> subscriber) {
                String path = Api.getInfoUrl(item.getId());
                PhotoDetail detail = PhotoDetail.objectFromData(NetUtil.GetEncHtml(path));
                subscriber.onNext(detail);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PhotoDetail>() {
                    @Override
                    public void onNext(PhotoDetail detail) {
                        showPhotoAttr(detail);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private void showPhotoAttr(PhotoDetail detail) {
        mTvPhotoSize.setText(String.format(Locale.ENGLISH, "分辨率 : %d * %d", detail.getWidth(), detail.getHeight()));
        mTvPhotoExposure.setText(String.format(Locale.ENGLISH, "快门 : %s", detail.getExif().getExposure_time() == null ? "未知" : detail.getExif().getExposure_time()));
        mTvPhotoAperture.setText(String.format(Locale.ENGLISH, "光圈 : %s", detail.getExif().getAperture() == null ? "未知" : detail.getExif().getAperture()));
        mTvPhotoFocal.setText("焦距 : " + (detail.getExif().getFocal_length() == null ? "未知" : detail.getExif().getFocal_length()));
        mTvPhotoIso.setText("ISO : " + (detail.getExif().getIso() == 0 ? "未知" : detail.getExif().getIso()));
        mTvPhotoModel.setText("器材 : " + (detail.getExif().getModel() == null ? "未知" : detail.getExif().getModel()));
        loadattr.setVisibility(View.GONE);
        attrs.setVisibility(View.VISIBLE);
    }

    private void download() {
        if (downloading) {
            Toast.show(this, "正在下载");
            return;
        }
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.PHOTO_LOAD_URL, item.getUrl_regular());
        intent.putExtra(DownloadService.PHOTO_ID, item.getId());
        startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateLoadProgress(DownloadEvent event) {
        if (event == null || !event.getPhotoId().equals(item.getId())) {
            return;
        }

        progressBar.setProgress(event.getProgress());
        if (event.getProgress() < 100 && !event.isDone()) {
            downloading = true;
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLoad(DownloadEvent event) {
        if (event == null || !event.getPhotoId().equals(item.getId())) {
            return;
        }
        if (event.isDone()) {
            downloading = false;
            progressBar.setVisibility(View.INVISIBLE);
            com.external.lifoto.utils.Toast.show(this, "已下载");
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                download();
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        attrs.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }
}
