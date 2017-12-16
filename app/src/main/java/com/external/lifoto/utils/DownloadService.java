package com.external.lifoto.utils;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by zuojie on 17-12-14.
 */

public class DownloadService extends Service {

    public static final String PHOTO_LOAD_URL = "photo_load_url";
    public static final String PHOTO_ID = "photo_id";

    @Override
    public void onCreate() {
        super.onCreate();
        DownloadManager.init(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String photoLoadUrl = intent.getStringExtra(PHOTO_LOAD_URL);
        String photoId = intent.getStringExtra(PHOTO_ID);

        File file = new File(Environment.getExternalStorageDirectory() + "/Lifoto/" + photoId + ".jpg");
        if (file.exists()) {
            EventBus.getDefault().post(new DownloadEvent(true, 100, photoId));//发送事件,图片文件已存在
        } else {
            Toast.show(this, "开始下载");
            DownloadManager.get().addTask(photoLoadUrl, file.getPath(), photoId);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
