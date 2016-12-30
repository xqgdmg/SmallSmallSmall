package com.example.chris.screenonunlock;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    wakeUpAndUnlock(MainActivity.this);
                    SystemClock.sleep(15000);
                }
            }
        }).start();
    }

    public  void wakeUpAndUnlock(final Context context){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
                //解锁 禁用显示键盘锁定 需要在主线程中执行
                kl.disableKeyguard();
                //获取电源管理器对象
                PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
                //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
                //点亮屏幕
                wl.acquire();

                //释放
                wl.release();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        // 重新开启锁屏
                        kl.reenableKeyguard();
                    }
                }).start();



            }
        });
    }
}
