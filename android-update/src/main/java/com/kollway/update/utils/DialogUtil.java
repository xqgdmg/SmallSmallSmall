package com.kollway.update.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.kollway.update.api.CheckResult;

/**
 * Created by houweibin on 16/4/15.
 */
public class DialogUtil {

    public static void showSimpleDialog(Context context, CheckResult result, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("更新")
                .setMessage(result.data.changeLog)
                .setPositiveButton("下载安装", listener)
                .setCancelable(false);
        if (result.data.updateType.equals("normal")) {
            builder.setNegativeButton("取消", null);
        }
        builder.create().show();
    }

    public static void showSimpleDialog(Context context, String title, String message, String ensure, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        builder.setPositiveButton(ensure, listener)
                .setNegativeButton("取消", null)
                .setCancelable(false);
        builder.create().show();
    }


}
