package com.kollway.update.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.kollway.update.api.APIManager;
import com.kollway.update.api.CheckResult;
import com.kollway.update.callback.DownloadCallback;
import com.kollway.update.model.AppVersion;
import com.kollway.update.model.UpdateConfig;
import com.kollway.update.utils.AndroidUtil;
import com.kollway.update.utils.DownloadNotificationManager;
import com.kollway.update.utils.FileUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * Created by Chris on 2016/12/11.
 * description：
 *              下载的服务
 */
public class DownloadingService extends Service {

    private int progress = 0;
    private DownloadNotificationManager downloadNotificationManager;
    private boolean downloading = false;


    Handler notifityHandler = new Handler() {
        public void handleMessage(Message msg) {
            downloadNotificationManager.notifity(msg.what);
            super.handleMessage(msg);
        }

    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        AppVersion appVersion = (AppVersion) intent.getSerializableExtra(AppVersion.TAG);
        UpdateConfig updateConfig = (UpdateConfig) intent.getSerializableExtra(UpdateConfig.TAG);

        downloadNotificationManager = new DownloadNotificationManager(this, updateConfig, appVersion);

        startDownload(updateConfig, appVersion);

        return START_STICKY;
    }


    private void startDownload(final UpdateConfig updateConfig, final AppVersion appVersion) {
        if (downloading) {
            return;
        }
        downloading = true;
        APIManager.downloadApk(appVersion.downloadUrl, new DownloadCallback() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Log.e("下載進度", String.format("%d%% done\n", (100 * bytesRead) / contentLength));
                int mProgress = (int) ((100 * bytesRead) / contentLength);
                if (Math.abs(mProgress - progress) >= 20 || (mProgress != progress && mProgress == 100)) {
                    progress = mProgress;
                    notifityHandler.sendEmptyMessage(progress);
                    if (progress == 100) {
                        String file = FileUtil.getApkPath(updateConfig.packageName,appVersion.downloadUrl);
                        installApk(new File(file));
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("DownloadingService", "請求下載失敗");
                downloading = false;
                DownloadingService.this.stopSelf();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    notifityHandler.sendEmptyMessage(0);
                    FileUtil.saveApk(response, updateConfig.packageName, appVersion.downloadUrl);
                } else {
                    throw new IOException("Unexpected code " + response);
                }
                downloading = false;
            }

        });
    }


    /**
     * 安装apk
     */
    private void installApk(File apkfile) {
        Intent installIntent = AndroidUtil.getInstallIntent(apkfile);
        this.startActivity(installIntent);
        DownloadingService.this.stopSelf();// 停止服务
    }


}
