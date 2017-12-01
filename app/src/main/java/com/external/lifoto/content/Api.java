package com.external.lifoto.content;

import com.external.lifoto.Lifoto;

import java.util.Locale;

/**
 * Created by zuojie on 17-12-1.
 */

public class Api {

    private static String SOURCE_NEW = String.format("https://api.unsplash.com/photos?client_id=%s&page=1&per_page=15&order_by=latest", Lifoto.API_KEY);
    private static String SOURCE_GOOD = String.format("https://api.unsplash.com/photos/curated?client_id=%s&page=1&per_page=15&order_by=latest", Lifoto.API_KEY);


    public static String getSortUrl(String category, int page) {
        switch (category) {
            case "建筑":
                return getCategoryUrl(2, page);
            case "饮食":
                return getCategoryUrl(3, page);
            case "自然":
                return getCategoryUrl(4, page);
            case "物品":
                return getCategoryUrl(8, page);
            case "人物":
                return getCategoryUrl(6, page);
            case "科技":
                return getCategoryUrl(7, page);
            case "最新作品":
                return SOURCE_NEW;
            case "精选":
                return SOURCE_GOOD;
            default:
                return null;
        }
    }

    public static String getInfoUrl(String id) {
        return String.format("https://api.unsplash.com/photos/%s?client_id=%s", id, Lifoto.API_KEY);
    }

    private static String getCategoryUrl(int which, int page) {
        return String.format(Locale.ENGLISH, "https://api.unsplash.com/categories/%d/photos?client_id=%s&page=%d&per_page=15", which, Lifoto.API_KEY, page);
    }
}
