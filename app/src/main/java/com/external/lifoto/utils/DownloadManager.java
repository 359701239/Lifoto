package com.external.lifoto.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zuojie on 17-12-16.
 */

public class DownloadManager {

    private static DownloadManager instance = null;
    private static Context mContext = null;
    private final static int MAX_THREAD = 3;
    private ThreadPoolExecutor threadPool;
    private OkHttpClient okHttpClient;

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
        okHttpClient = new OkHttpClient();
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

        private String url, file, tag;

        public DownloadTask(String url, String file, String tag) {
            this.url = url;
            this.file = file;
            this.tag = tag;
        }

        @Override
        public void run() {
            Request request = new Request.Builder().url(url).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.show(mContext, "下载失败");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File item = new File(file);
                        fos = new FileOutputStream(item);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            EventBus.getDefault().postSticky(new DownloadEvent(false, progress, tag));//发送事件,更新UI
                        }
                        fos.flush();
                        EventBus.getDefault().post(new DownloadEvent(true, 100, tag));//发送事件,更新UI
                        // 通知图库更新
                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + item.getAbsolutePath())));

                    } catch (Exception e) {
                        Toast.show(mContext, "下载失败");
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                }
            });
        }
    }
}
