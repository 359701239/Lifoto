package com.external.lifoto.utils;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by zuojie on 17-12-14.
 */

public class DownloadService extends IntentService {

    private static final String TAG = DownloadService.class.getName();
    public static final String PHOTO_LOAD_URL = "photo_load_url";
    public static final String PHOTO_ID = "photo_id";
    private DownloadEvent mLoadPhotoEvent;

    public DownloadService() {
        super(TAG);
        mLoadPhotoEvent = new DownloadEvent();
        DownloadManager.init(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String photoLoadUrl = intent.getStringExtra(PHOTO_LOAD_URL);
        String photoId = intent.getStringExtra(PHOTO_ID);

        mLoadPhotoEvent.setFileId(photoId);
        File file = new File(Environment.getExternalStorageDirectory() + "/Lifoto/" + photoId + ".jpg");
        if (file.exists()) {
            mLoadPhotoEvent.setDone();
            EventBus.getDefault().post(mLoadPhotoEvent);//发送事件,图片文件已存在
        } else {
            Toast.show(this, "开始下载");
            DownloadManager.get().addTask(photoLoadUrl, file.getPath(), photoId);
        }
    }
}
