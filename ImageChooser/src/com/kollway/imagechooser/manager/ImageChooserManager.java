package com.kollway.imagechooser.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.text.TextUtils;
import android.widget.Toast;

import com.kollway.gson.Gson;
import com.kollway.imagechooser.R;
import com.kollway.imagechooser.activity.ChooseDialogActivity;
import com.kollway.imagechooser.utils.DeviceUtil;


public final class ImageChooserManager {

    public static final int REQUEST_CODE_START_CHOOSE_IMAGE = 100;

    private static ImageChooserManager instance;

    private ImageChooserSettings mSettings;

    synchronized public static ImageChooserManager getInstance() {
        if (instance == null) {
            instance = new ImageChooserManager();
        }
        return instance;
    }

    private ImageChooserManager() {
        mSettings = new ImageChooserSettings();
    }

    public ImageChooserSettings settings() {
        return mSettings;
    }

    /**
     * 此方法提供为ImageChooserManager内部使用，外部无需调用此方法
     *
     * @param context
     * @return
     */
    public ImageChooserSettings loadSettings(Context context) {
        ImageChooserSettings settings = loadImageChooserSettingsFromSharedPreferences(context.getApplicationContext());
        if (settings != null) {
            mSettings = settings;
        }
        return mSettings;
    }

    /**
     * 这个方法有返回结果，谁调用返回给谁，这里是 MainActivity
     */
    public void startChooseImage(Activity activity, String title, int requestCode) {
        if (!isSDCardMounted(activity)) {
            Toast.makeText(activity, "选取图片失败, SD卡不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        Point screen = DeviceUtil.getDeviceSize(activity);
        int _40dp = DeviceUtil.dip2px(activity, 40);

        ImageChooserSettings settings = settings();

        if (settings.getCropSize() == -1) {
            //未设置裁剪大小，自动设置当前屏幕宽度-40dp的大小
            settings.setCropSize(screen.x - _40dp);
        }
        if (settings.getScaleWidth() == -1 && settings.getScaleHeight() == -1) {
            //未设置缩放目标大小，自动设置当前屏幕宽度/2和当前屏幕高度/2的大小
            settings.setScaleWidth(screen.x / 2);
            settings.setScaleHeight(screen.y / 2);
        }

        saveImageChooserSettingsToSharedPreferences(activity.getApplicationContext(), settings);

        Intent intent = new Intent(activity, ChooseDialogActivity.class);
        intent.putExtra("title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivityForResult(intent, requestCode);// 返回的结果在 调用这个方法的类
        activity.overridePendingTransition(R.anim.fast_choose_in, R.anim.fast_choose_out);
    }

    private void saveImageChooserSettingsToSharedPreferences(Context context, ImageChooserSettings settings) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences("ImageChooser.settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        editor.putString("ImageChooserSettings", json);
        editor.commit();
    }

    private ImageChooserSettings loadImageChooserSettingsFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences("ImageChooser.settings", Context.MODE_PRIVATE);
        ImageChooserSettings settings = null;
        String json = sharedPreferences.getString("ImageChooserSettings", null);
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();
            settings = gson.fromJson(json, ImageChooserSettings.class);
        }
        return settings;
    }

    private boolean isSDCardMounted(Context context) {
        ImageChooserSettings settings = instance.settings();
        String folderPath = settings.getFolderPath();

        if (TextUtils.isEmpty(folderPath) || !new File(folderPath).exists()) {
            return mkdir(context);
        }

        return true;
    }

    public static boolean mkdir(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        String folderPath = externalFilesDir != null ? externalFilesDir.getAbsolutePath() + "/KollwayImageChooser" : null;
        if (TextUtils.isEmpty(folderPath)) {
            return false;
        }

        instance.mSettings.setFolderPath(folderPath);

        final File folderPathDir = new File(folderPath);
        if (!folderPathDir.exists()) {
            return folderPathDir.mkdirs();
        }

        return true;
    }

    public static List<String> getResultImagePath(Intent resultData) {
        String path = "";
        List<String> pathList = new ArrayList<String>();

        if (resultData != null) {
            // change
            resultData.getStringExtra("path");
//            path = resultData.getStringExtra("path");
//            if (path == null) {
//                path = "";
//            }
        }
        return pathList;
    }

    public static void clearCachedImages() {
        ImageChooserSettings settings = ImageChooserManager.getInstance().settings();
        File folder = new File(settings.getFolderPath());
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.getName().equals(".nomedia")) {
                        file.delete();
                    }
                }
            }
        }
    }
}
