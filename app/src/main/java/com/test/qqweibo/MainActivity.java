package com.test.qqweibo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.tencent.tauth.TAuthView;

public class MainActivity extends AppCompatActivity {

    private String scope = "all";// 授权范围
    private AuthReceiver receiver;
    //	private String scope = "get_user_info,get_user_profile,get_simple_userinfo,add_share,add_topic,list_album,upload_pic,add_album";// 授权范围


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        registerIntentReceivers();
    }

    /**
     * QQ webView登录
     * @param v
     */
    public void login(View v){
        Intent intent = new Intent(this, com.tencent.tauth.TAuthView.class);

        intent.putExtra(TAuthView.CLIENT_ID, "1105519159");//创建应用，QQ分配的appId
        intent.putExtra(TAuthView.SCOPE, scope);//
        intent.putExtra(TAuthView.TARGET, "_slef");//打开登录页面的方式：“_slef”以webview方式打开; "_blank"以内置安装的浏览器方式打开
        startActivity(intent);
    }

    private void registerIntentReceivers() {
        receiver = new AuthReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TAuthView.AUTH_BROADCAST);
        registerReceiver(receiver, filter);
    }

    private void unregisterIntentReceivers() {
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterIntentReceivers();
        }
    }

    public class AuthReceiver extends BroadcastReceiver {

        private static final String TAG = "AuthReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle exts = intent.getExtras();
            String raw = exts.getString("raw");
            String access_token = exts.getString(TAuthView.ACCESS_TOKEN);//令牌

            String expires_in = exts.getString(TAuthView.EXPIRES_IN);
            String error_ret = exts.getString(TAuthView.ERROR_RET);
            String error_des = exts.getString(TAuthView.ERROR_DES);

            showLog(access_token);
        }

    }

    private void showLog(String msg){
        Log.e("result", "" +msg);
    }
}
