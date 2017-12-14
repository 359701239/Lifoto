package com.external.lifoto.utils;

/**
 * Created by dingmouren on 2017/5/22.
 */

public class LoadPhotoEvent {
    private boolean done = false;
    private int progress;
    private String photoId;

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
