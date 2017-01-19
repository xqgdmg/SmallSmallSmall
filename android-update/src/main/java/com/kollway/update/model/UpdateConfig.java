package com.kollway.update.model;

import java.io.Serializable;

/**
 * Created by Chris on 2016/12/11.
 * description：
 *              更新相关设置
 */
public class UpdateConfig implements Serializable {
    public static final String TAG = "UPDATE_CONFIG";

    //packageName 默認為context包名
    public String packageName;

    //服務器地址
    public String baseUrl;

    //應用圖標
    public int logo;

    //應用名稱
    public String appName;

    //平台標識（0表示android，1表示iOS）
    public int platform = 0;

    //当前应用的version_code
    public int versionCode;

    //是否只在wifi下更新
    public boolean updateOnlyWifi = false;

    //當已是新版本的時候，是否toast提示
    public boolean showIsLatestToast = false;

    //當已下載過該apk時，不重複下載，直接安裝apk(未確認該驗證方式是否可靠)
    public boolean smartDownload = false;

    //當已是新版本的時候，顯示的toast提示
    public String latestToast = "当前已是最新版本，无需更新";

}
