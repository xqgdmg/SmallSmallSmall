package com.test.eventbus3.event;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者：Chris
 * 邮箱：395932265@qq.com
 * 描述:
 *      TODO
 */
public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //服务创建成功
        //-->Activity
        EventBus.getDefault().post(new MyEvent1(10, "billy"));
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
