package com.test.demo.event;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import de.greenrobot.event.EventBus;

/**
 * 创建者     伍碧林
 * 创建时间   2016/6/23 09:41
 * 描述	      ${TODO}
 *
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
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
