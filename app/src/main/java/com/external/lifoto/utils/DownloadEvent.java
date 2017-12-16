package com.external.lifoto.utils;

/**
 * Created by dingmouren on 2017/5/22.
 */

public class DownloadEvent {
    private boolean done = false;
    private int progress;
    private String id;

    public void setDone() {
        this.done = true;
    }

    public boolean isDone() {
        return done;
    }

    public String getPhotoId() {
        return id;
    }

    public void setFileId(String photoId) {
        this.id = photoId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
