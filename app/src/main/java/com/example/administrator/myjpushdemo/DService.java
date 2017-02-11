package com.example.administrator.myjpushdemo;

import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/2/11.
 */
@SuppressLint("NewApi")
public class DService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Toast.makeText(ExampleApplication.mContext,"onNotificationPosted",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Toast.makeText(ExampleApplication.mContext,"onNotificationRemoved",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(ExampleApplication.mContext,"NotificationListenerService Running...",Toast.LENGTH_LONG).show();
    }
}
