package com.kollway.update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kollway.update.api.APIManager;
import com.kollway.update.api.CheckResult;
import com.kollway.update.model.AppVersion;
import com.kollway.update.model.UpdateConfig;
import com.kollway.update.service.DownloadingService;
import com.kollway.update.utils.AndroidUtil;
import com.kollway.update.utils.DialogUtil;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UpdateManager {
    private UpdateConfig updateConfig;
    private Context mContext;


    private UpdateManager(UpdateConfig updateConfig, Context context) {
        this.updateConfig = updateConfig;
        mContext = context;
    }

    public void update() {
        requestCheckUpdateApi();
    }

    private void showDownloadDialog(final CheckResult result) {
        DialogUtil.showSimpleDialog(mContext, result, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AndroidUtil.isWifiConnect(mContext)) {
                    startDownloadService(result);
                    return;
                }
                DialogUtil.showSimpleDialog(mContext, "警告", "当前非WiFi网络环境，下载更新将会消耗流量，确定更新吗？", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDownloadService(result);
                    }
                });
            }
        });
    }

    private void startDownloadService(CheckResult result) {
        Intent intent = new Intent(mContext, DownloadingService.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(AppVersion.TAG, result.data);
        bundle.putSerializable(UpdateConfig.TAG, updateConfig);
        intent.putExtras(bundle);

        mContext.startService(intent);
    }

    private void requestCheckUpdateApi() {
        HashMap<String, String> body = new HashMap<>();
        body.put("channel", "official");
        body.put("type", "android");
        body.put("name", getVersion());

        APIManager.buildUpdateApi(mContext, updateConfig.baseUrl).getLatestVersion(body, new Callback<CheckResult>() {
            @Override
            public void success(CheckResult checkResult, Response response) {
                AppVersion appVersion = checkResult.data;
                if (appVersion != null
                        && !appVersion.name.equals(getVersion())
                        && checkResult.data.status.equals("publish")) {
                    showDownloadDialog(checkResult);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    //获取当前应用的版本号
    private String getVersion() {
        String version = "";
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    //UpdateManager 构造类
    public static class Builder {
        private final UpdateConfig updateConfig;
        private Context mContext;

        public Builder(Context context, String appName, int logoResource, String baseUrl) {
            mContext = context;
            updateConfig = new UpdateConfig();
            updateConfig.versionCode = AndroidUtil.getVersionCode(context);
            updateConfig.baseUrl = baseUrl;
            updateConfig.appName = appName;
            updateConfig.logo = logoResource;
            updateConfig.packageName = mContext.getPackageName();
        }

        public UpdateManager build() {
            return new UpdateManager(updateConfig, mContext);
        }

        /**
         * 當已是新版本時，是否吐司提示
         *
         * @param b
         * @return
         */
        public Builder showIsLatestToast(boolean b) {
            updateConfig.showIsLatestToast = b;
            return this;
        }
    }
}
