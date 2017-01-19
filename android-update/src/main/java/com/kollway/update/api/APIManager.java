package com.kollway.update.api;

import android.content.Context;
import android.os.RecoverySystem;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kollway.update.api.typeadapter.BooleanAdapter;
import com.kollway.update.callback.DownloadCallback;
import com.kollway.update.listener.ProgressListener;
import com.kollway.update.listener.ProgressResponseBody;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Created by houweibin on 16/4/15.
 */
public class APIManager {

    public static REST buildUpdateApi(Context context, String baseUrl) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(myConverter())
                .setEndpoint(baseUrl)
                .build();
        return restAdapter.create(REST.class);
    }


    public static Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(boolean.class, new BooleanAdapter())
            .registerTypeAdapter(Boolean.class, new BooleanAdapter())
            .create();

    private static Converter myConverter() {
        return new GsonConverter(GSON);
    }

    /**
     * 添加进度的功能
     * @param downloadUrl      下載地址
     * @param downloadCallback 下載回調
     * @return
     */
    public static OkHttpClient downloadApk(String downloadUrl, final DownloadCallback downloadCallback) {

        ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {// 注意回调参数
                downloadCallback.update(bytesRead, contentLength, done);
            }
        };

        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                downloadCallback.onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                downloadCallback.onResponse(response);
            }
        };

        //
        OkHttpClient oktttpClient = downloadApk(downloadUrl, progressListener, callback);

        return oktttpClient;
    }

    /**
     * @param downloadUrl      下載地址
     * @param progressListener 進度監聽
     * @param callback         請求回調
     * @return
     */
    private static OkHttpClient downloadApk(String downloadUrl, final ProgressListener progressListener, final Callback callback) {
        OkHttpClient oktttpClient = new OkHttpClient();

        //添加拦截器，自定义 ResponseBody，添加下载进度
        oktttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener))// ResponseBody,原始返回的数据加上一个进度的监听
                        .build();
            }
        });

        //封装请求
        Request request = new Request.Builder()
                //下载地址
                .url(downloadUrl)
                .build();

        //发送异步请求
        oktttpClient.newCall(request).enqueue(callback);

        return oktttpClient;
    }
}
