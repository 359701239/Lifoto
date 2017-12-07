package com.external.lifoto.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by zuojie on 17-12-4.
 */

public class SystemBarUtil {

    public static int getNavigationBarHeight(Context context) {
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            return resources.getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
