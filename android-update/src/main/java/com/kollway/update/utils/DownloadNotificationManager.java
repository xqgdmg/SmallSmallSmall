package com.kollway.update.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.kollway.update.R;
import com.kollway.update.api.CheckResult;
import com.kollway.update.model.AppVersion;
import com.kollway.update.model.UpdateConfig;

import java.io.File;

/**
 * Created by houweibin on 16/4/19.
 */
public class DownloadNotificationManager {

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private Context mContext;
    private final int notifityId = 10086;
    private String packageName;
    private String path;

    public DownloadNotificationManager(Context context, UpdateConfig updateConfig, AppVersion appVersion) {
        this.path = appVersion.downloadUrl;
        this.packageName = updateConfig.packageName;

        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mContext = context.getApplicationContext();
        mBuilder.setContentTitle(updateConfig.appName + "更新")
                .setContentText("努力下载更新中～")
                .setSmallIcon(updateConfig.logo)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), updateConfig.logo));
    }

    public void notifity(int progress) {
        if (progress == 100) {
            mBuilder.setContentText("下載完成,点击安装最新apk！")
                    .setDefaults( Notification.DEFAULT_VIBRATE)
                            //移除進度條
                    .setProgress(0, 0, false);
            setInstallPending();
            mNotifyManager.notify(notifityId, mBuilder.build());
        } else {
            mBuilder.setProgress(100, progress, false)
                    .setContentText("努力下载更新中～(" + progress + "%)");
            mNotifyManager.notify(notifityId, mBuilder.build());
        }
    }

    /**
     * 設置點擊安裝效果
     */
    private void setInstallPending() {
        File appFile = new File(FileUtil.getApkPath(packageName, path));
        Intent installIntent = AndroidUtil.getInstallIntent(appFile);
        if (installIntent != null) {
            PendingIntent updatePendingIntent = PendingIntent.getActivity(mContext, 0, installIntent, 0);
            mBuilder.setContentIntent(updatePendingIntent);
        }
    }


}
