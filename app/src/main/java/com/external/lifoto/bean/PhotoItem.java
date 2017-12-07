package com.external.lifoto.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-12-4.
 */

public class PhotoItem {

    private String id;
    private int width;
    private int height;
    private String color;
    private String description;
    private ItemUrls urls;

    private PhotoItem(String id, int width, int height, String color, String description, ItemUrls urls) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.color = color;
        this.description = description;
        this.urls = urls;
    }

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

    public ItemUrls getUrls() {
        return urls;
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
            ItemUrls urls = new ItemUrls(url_raw, url_full, url_regular, url_small, url_thumb);

            items.add(new PhotoItem(id, width, height, color, description, urls));
        }
        return items;
    }

    public static class ItemUrls {
        public String raw;
        public String full;
        public String regular;
        public String small;
        public String thumb;

        public ItemUrls(String raw, String full, String regular, String small, String thumb) {
            this.raw = raw;
            this.full = full;
            this.regular = regular;
            this.small = small;
            this.thumb = thumb;
        }
    }
}
