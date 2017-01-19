package com.kollway.update.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

import java.io.Serializable;

/**
 * Created by Chris on 2016/12/11.
 * description：
 *              TODO
 */
public class AppVersion implements Serializable {
    public static final String TAG = "APP_VERSION";

    public String channel;
    public String name;

    @SerializedName("changeLog")
    public String changeLog;

    @SerializedName("downloadUrl")
    public String downloadUrl;

    @SerializedName("updateType")
    public String updateType;

    public String status;
}
