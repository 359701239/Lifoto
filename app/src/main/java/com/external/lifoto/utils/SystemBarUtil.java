package com.external.lifoto.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by zuojie on 17-12-4.
 */

public class SystemBarUtil {

    private static int navigationBarHeight = 0;

    public static int getNavigationBarHeight(Context context) {
        if (navigationBarHeight == 0) {
            try {
                Resources resources = context.getResources();
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                navigationBarHeight = resources.getDimensionPixelSize(resourceId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return navigationBarHeight;
    }
}
