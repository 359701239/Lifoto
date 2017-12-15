package com.external.lifoto.utils;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zuojie on 17-12-14.
 */

public class DownloadService extends IntentService {

    private static final String TAG = DownloadService.class.getName();
    public static final String PHOTO_LOAD_URL = "photo_load_url";
    public static final String PHOTO_ID = "photo_id";
    public static final String PHOTO_SET = "photo_set";
    private LoadPhotoEvent mLoadPhotoEvent;

    public DownloadService() {
        super(TAG);
//        EventBus.getDefault().register(this);
        mLoadPhotoEvent = new LoadPhotoEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        boolean setPaper = intent.getBooleanExtra(PHOTO_SET, false);
        String photoLoadUrl = intent.getStringExtra(PHOTO_LOAD_URL);
        String photoId = intent.getStringExtra(PHOTO_ID);
        Bitmap paper = null;

        mLoadPhotoEvent.setPhotoId(photoId);
        File file = new File(Environment.getExternalStorageDirectory() + "/Lifoto/" + photoId + ".jpg");
        if (file.exists()) {
            mLoadPhotoEvent.setDone(true);
            EventBus.getDefault().post(mLoadPhotoEvent);//发送事件,图片文件已存在
            if (!setPaper) {
                return;
            }
        }
        if (!file.exists()) {
            HttpURLConnection connec = null;
            try {
                URL url = new URL(photoLoadUrl);
                connec = (HttpURLConnection) url.openConnection();
                connec.setDoInput(true);
                connec.setConnectTimeout(20 * 1000);
                int code = connec.getResponseCode();
                if (code == 200) {
                    InputStream is = connec.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int length;
                    int progress = 0;
                    int contentLength = connec.getContentLength();
                    byte[] buffer = new byte[1024];
                    while ((length = is.read(buffer)) != -1) {
                        progress += length;
                        mLoadPhotoEvent.setProgress(progress * 100 / contentLength);
                        EventBus.getDefault().postSticky(mLoadPhotoEvent);//发送事件,更新UI
                        bos.write(buffer, 0, length);
                    }
                    mLoadPhotoEvent.setDone(true);
                    EventBus.getDefault().post(mLoadPhotoEvent);//发送事件,更新UI
                    bos.flush();
                    bos.close();
                    is.close();
                    paper = savePhoto(photoId, bos);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connec != null) {
                    connec.disconnect();
                }
            }
        } else {
            paper = BitmapFactory.decodeFile(file.getPath());
        }
        if (setPaper && paper != null) {
            Intent set = new Intent(Intent.ACTION_ATTACH_DATA);
            set.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            set.putExtra("mimeType", "image/*");
            Uri uri = Uri.parse(MediaStore.Images.Media
                    .insertImage(getContentResolver(), paper, null, null));
            set.setData(uri);
            startActivity(set);
        }
    }

    private Bitmap savePhoto(String photoId, ByteArrayOutputStream bos) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.toByteArray().length);
        File photoDir = new File(Environment.getExternalStorageDirectory(), "Lifoto");
        if (!photoDir.exists()) {
            photoDir.mkdir();
        }
        File file = new File(photoDir, photoId + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoDir.getAbsolutePath())));
        return bitmap;
    }
}
