package com.kollway.update.api;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by houweibin on 16/4/15.
 */
public interface REST {

    /**
     * @param callback    回調
     */
    @Headers("Content-Type: application/json")
    @POST("/api/v1/versions/last")
    void getLatestVersion(
            @Body HashMap<String, String> body,
            Callback<CheckResult> callback
    );
}
