package com.external.lifoto.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qtfreet00 on 2017/2/5.
 */
public class NetUtil {
    private static final OkHttpClient client = new OkHttpClient();

    public static String GetEncHtml(String url) {

        Request.Builder builder = new Request.Builder().url(url);
        Request request = builder.build();
        Response execute;
        try {
            execute = client.newCall(request).execute();
            if (execute.isSuccessful()) {
                return execute.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
