package com.example.chris.update;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kollway.update.UpdateManager;
import com.kollway.update.api.APIManager;

public class MainActivity extends AppCompatActivity {

    public static String API_URL = "api.linshangmen.com.cn";
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initListener() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdate();// 检查更新
                Log.e("TAG", "haha");
            }
        });
    }

    private void initView() {
        mBtn = (Button) findViewById(R.id.btn);
    }

    // 检查更新
    private void checkUpdate() {
        UpdateManager.Builder builder = new UpdateManager.Builder(this,
                "newShop", R.mipmap.ic_launcher, getBaseUrl());

        UpdateManager updateManager = builder.build();
        updateManager.update();
    }

    // 获取网址
    public static String getBaseUrl() {
        return "http://" + API_URL;
    }
}
