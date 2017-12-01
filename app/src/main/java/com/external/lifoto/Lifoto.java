package com.external.lifoto;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by zuojie on 17-11-30.
 */

public class Lifoto extends Application {

    public static String API_KEY = null;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            API_KEY = appInfo.metaData.getString("apiKey");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
