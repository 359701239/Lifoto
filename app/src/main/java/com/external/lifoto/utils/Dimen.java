package com.external.lifoto.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by zuojie on 17-3-8.
 */

public class Dimen {

    public static int dpToPx(Context context, float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()) + 0.5f);
    }
}
