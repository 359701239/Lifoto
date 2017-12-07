package com.external.lifoto.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import com.external.lifoto.R;

/**
 * Created by zuojie on 17-12-7.
 */

public class Dialog {

    public static void showSimpleDialog(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public static void showSimpleDialog(Context context, @StringRes int title, @StringRes int msg) {
        showSimpleDialog(context, context.getText(title).toString(), context.getText(msg).toString());
    }
}
