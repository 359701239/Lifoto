package com.external.lifoto.bean;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-12-4.
 */

public class PhotoItem implements Parcelable {

    private String id;
    private int width;
    private int height;
    private String color;
    private String description;
    private String url_raw;
    private String url_full;
    private String url_regular;
    private String url_small;
    private String url_thumb;
    private int likes;

    private PhotoItem(String id, int width, int height, String color, String description,
                      String url_raw, String url_full, String url_regular, String url_small, String url_thumb,
                      int likes) {
        this.id = id;
        this.width = width;
        this.height = height;
        int colorTemp = Color.parseColor(color);
        if (Color.red(colorTemp) > 200 && Color.green(colorTemp) > 200 && Color.blue(colorTemp) > 200) {
            this.color = "#C0C0C0";
        } else {
            this.color = color;
        }
        this.description = description;
        this.url_raw = url_raw;
        this.url_full = url_full;
        this.url_regular = url_regular;
        this.url_small = url_small;
        this.url_thumb = url_thumb;
        this.likes = likes;
    }

    protected PhotoItem(Parcel in) {
        id = in.readString();
        width = in.readInt();
        height = in.readInt();
        color = in.readString();
        description = in.readString();
        url_raw = in.readString();
        url_full = in.readString();
        url_regular = in.readString();
        url_small = in.readString();
        url_thumb = in.readString();
        likes = in.readInt();
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        @Override
        public PhotoItem createFromParcel(Parcel in) {
            return new PhotoItem(in);
        }

        @Override
        public PhotoItem[] newArray(int size) {
            return new PhotoItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl_raw() {
        return url_raw;
    }

    public String getUrl_full() {
        return url_full;
    }

    public String getUrl_regular() {
        return url_regular;
    }

    public String getUrl_small() {
        return url_small;
    }

    public String getUrl_thumb() {
        return url_thumb;
    }

    public int getLikes() {
        return likes;
    }

    public static ArrayList<PhotoItem> parseItems(JSONArray jsonArray) throws JSONException {
        ArrayList<PhotoItem> items = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String id = item.getString("id");
            int width = item.getIntValue("width");
            int height = item.getIntValue("height");
            String color = item.getString("color");
            String description = item.getString("description");
            JSONObject urlArray = item.getJSONObject("urls");
            String url_raw = urlArray.getString("raw");
            String url_full = urlArray.getString("full");
            String url_regular = urlArray.getString("regular");
            String url_small = urlArray.getString("small");
            String url_thumb = urlArray.getString("thumb");
            int likes = item.getIntValue("likes");

            items.add(new PhotoItem(id, width, height, color, description,
                    url_raw, url_full, url_regular, url_small, url_thumb,
                    likes));
        }
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(color);
        dest.writeString(description);
        dest.writeString(url_raw);
        dest.writeString(url_full);
        dest.writeString(url_regular);
        dest.writeString(url_small);
        dest.writeString(url_thumb);
        dest.writeInt(likes);
    }
}
