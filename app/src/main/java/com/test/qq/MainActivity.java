package com.test.qq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements IUiListener{

    private Tencent mTencent;
    private UserInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        initTencentApi();
    }

    /**
     * 初始化腾讯api
     */
    private void initTencentApi() {
        mTencent = Tencent.createInstance("1105519159", this);
    }

    /**
     * 点击事件
     */
    public void qqLoginByClient(View v){
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", this);
        } else {
            mTencent.logout(this);
        }
    }

    //授权登录成功
    @Override
    public void onComplete(Object o) {
        JSONObject jsonObject = (JSONObject) o;

        String token = jsonObject.optString(Constants.PARAM_ACCESS_TOKEN);
        String expires = jsonObject.optString(Constants.PARAM_EXPIRES_IN);
        String openId = jsonObject.optString(Constants.PARAM_OPEN_ID);
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                && !TextUtils.isEmpty(openId)) {
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);

            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject jsonObject = (JSONObject) o;
                    showLog(jsonObject.toString());
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }
        showLog(jsonObject.toString());
    }

    //登录失败
    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showLog(String msg){
        Log.d("result", "" + msg);
    }
}
