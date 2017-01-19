package com.kollway.update.callback;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by houweibin on 16/4/18.
 */
public interface DownloadCallback {
    void update(long bytesRead, long contentLength, boolean done);

    void onFailure(Request request, IOException e);

    void onResponse(Response response) throws IOException;

}
