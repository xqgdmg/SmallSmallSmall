package com.kollway.update.listener;

/**
 * Created by houweibin on 16/4/18.
 */
public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
