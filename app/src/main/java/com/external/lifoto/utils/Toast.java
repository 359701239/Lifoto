package com.external.lifoto.utils;

import android.content.Context;

/**
 * Created by zuojie on 17-12-7.
 */

public class Toast {
    private static android.widget.Toast mToast;

    public static void show(Context context, String text) {
        if (mToast != null) {
            mToast.setText(text);
            mToast.setDuration(android.widget.Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
