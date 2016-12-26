package com.test.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.test.demo.event.MyEvent1;
import com.test.demo.event.MyService;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1.注册-->成为订阅者-->可以接收到消息
        EventBus.getDefault().register(this);
    }

    public void click1(View view) {
        //主线程->发布消息给自己
        EventBus.getDefault().post(new MyEvent1(10, "billy"));
    }

    public void click2(View view) {
        //子click1线程->发布消息给自己
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new MyEvent1(10, "billy"));
            }
        }).start();
    }


    public void click3(View view) {
        //启动服务
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
    }


    public void onEvent(MyEvent1 event) {
        Log.i(TAG, "--onEvent--ThreadName->" + Thread.currentThread().getName()
                + " name:" + event.name + " age:" + event.age);
    }

    //一定在主线程
    public void onEventMainThread(MyEvent1 event) {//用的多
        Log.i(TAG, "--onEventMainThread--ThreadName->" + Thread.currentThread().getName()
                + " name:" + event.name + " age:" + event.age);
    }
    //发布者主线程-->在EventBus的线程池
    //发布者子线程-->在子线程

    public void onEventBackgroundThread(MyEvent1 event) {//用的少
        Log.i(TAG, "--onEventBackgroundThread--ThreadName->" + Thread.currentThread().getName()
                + " name:" + event.name + " age:" + event.age);
    }

    //一定在线程池(EventBus的线程池)

    public void onEventAsync(MyEvent1 event) {//用的少
        Log.i(TAG, "--onEventAsync--ThreadName->" + Thread.currentThread().getName()
                + " name:" + event.name + " age:" + event.age);
    }
}
