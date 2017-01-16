package com.itheima.alipaydemo24;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itheima.alipaydemo24.model.AliPayInfo;
import com.itheima.alipaydemo24.model.PayResult;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

    private String url;
    private AliPayInfo aliPayInfo;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        url = "http://192.168.34.32:8080/HeiMaPay/Pay?goodId=111&count=1&price=1";
        context = this;
    }

    public void alipay(View v){
        //1.post信息到服务器
        StringRequest request = new StringRequest(url, this, this);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        //2.解析获取“支付串码”

        //-----------------分割线------------------
        //3.调用支付SDK的支付方法，传参（支付串码）
        //4.处理支付结果
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        showLog("error");
    }

    @Override
    public void onResponse(String response) {
        showLog(response);
        //2.解析获取“支付串码”
        aliPayInfo = JSON.parseObject(response, AliPayInfo.class);
        showLog(aliPayInfo.toString());
        //3.调用支付SDK的支付方法，传参（支付串码）
        pay();
    }

    private void pay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask task = new PayTask(MainActivity.this);
                //支付结果
                String payResult = task.pay(aliPayInfo.getPayInfo(), true);
                showLog(payResult);
                Message msg = mHandler.obtainMessage();
                msg.obj = payResult;
                //4.处理支付结果
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private void showLog(String msg){
        Log.e("result", "" + msg);
    }

    private MyHandler mHandler = new MyHandler();

    class MyHandler extends  Handler{
        @Override
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            /**
             * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
             * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
             * docType=1) 建议商户依赖异步通知
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息

            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
            } else if(resultStatus.equals("6001")){
                Toast.makeText(context, "取消支付", Toast.LENGTH_SHORT).show();
            }else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();

                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();

                }
            }
            super.handleMessage(msg);
        }
    }
}
