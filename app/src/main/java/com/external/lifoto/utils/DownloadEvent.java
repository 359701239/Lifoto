package com.external.lifoto.utils;

/**
 * Created by dingmouren on 2017/5/22.
 */

public class DownloadEvent {
    private boolean done = false;
    private int progress;
    private String id;

    public DownloadEvent(boolean done, int progress, String id) {
        this.done = done;
        this.progress = progress;
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public String getPhotoId() {
        return id;
    }

    public int getProgress() {
        return progress;
    }
}
