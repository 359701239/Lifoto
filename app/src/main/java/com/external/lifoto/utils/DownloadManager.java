package com.external.lifoto.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zuojie on 17-12-16.
 */

public class DownloadManager {

    private static DownloadManager instance = null;
    private static Context mContext = null;
    private final static int MAX_THREAD = 3;
    private ThreadPoolExecutor threadPool;

    public static void init(Context context) {
        mContext = context;
    }

    public static DownloadManager get() {
        if (mContext == null) {
            throw new RuntimeException("get method must call after init.");
        }
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }

    public DownloadManager() {
        threadPool = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 60, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
            private AtomicInteger mInteger = new AtomicInteger(1);

            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                return new Thread(runnable, "download thread #" + mInteger.getAndIncrement());
            }
        });
    }

    public void addTask(String url, String file, String tag) {
        threadPool.execute(new DownloadTask(url, file, tag));
    }

    private class DownloadTask implements Runnable {

        private DownloadTask(String url, String file, String tag) {
            DownloadEvent mLoadPhotoEvent = new DownloadEvent();
            mLoadPhotoEvent.setFileId(tag);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(20 * 1000);
                int code = connection.getResponseCode();
                if (code == 200) {
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int length;
                    int progress = 0;
                    int contentLength = connection.getContentLength();
                    byte[] buffer = new byte[1024];
                    while ((length = is.read(buffer)) != -1) {
                        progress += length;
                        mLoadPhotoEvent.setProgress(progress * 100 / contentLength);
                        EventBus.getDefault().postSticky(mLoadPhotoEvent);//发送事件,更新UI
                        bos.write(buffer, 0, length);
                    }
                    mLoadPhotoEvent.setDone();
                    EventBus.getDefault().post(mLoadPhotoEvent);//发送事件,更新UI
                    bos.flush();
                    bos.close();
                    is.close();
                    savePhoto(file, bos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        public void run() {

        }
    }


    private void savePhoto(String file, ByteArrayOutputStream bos) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.toByteArray().length);
        File pic = new File(file);
        if (!pic.getParentFile().exists()) {
            if (!pic.getParentFile().mkdir()) {
                return;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(pic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                    pic.getAbsolutePath(), pic.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + pic.getAbsolutePath())));
    }
}
