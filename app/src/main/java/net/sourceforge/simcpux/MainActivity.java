package net.sourceforge.simcpux;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.model.WechatPayInfo;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String>{

    private String url;
    private WechatPayInfo wechatPayInfo;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        url ="http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
        initWechat();
    }

    /**
     * 初始化微信api
     */
    private void initWechat() {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//        api.handleIntent(getIntent(), this);
    }

    public void wechatPay(View v){
        //1.post信息到服务器
        StringRequest request = new StringRequest(url, this, this);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        //2.解析获取“支付串码”

        //-----------------分割线------------------
        //3.调用支付SDK的支付方法，传参（支付串码）

        //4.处理支付结果:在WXPayEntryActivity类里面的onResp

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        showLog("error");
    }

    @Override
    public void onResponse(String response) {
        showLog(response);
        //2.解析获取“支付串码”
        response = response.replaceAll("package", "packageValue");
        wechatPayInfo = JSON.parseObject(response, WechatPayInfo.class);
        showLog(wechatPayInfo.toString());

        //3.调用支付SDK的支付方法，传参（支付串码）
        sendPayRequest();
    }

    /**调用微信支付*/
    public void sendPayRequest() {
        PayReq req = new PayReq();
        req.appId = wechatPayInfo.getAppid();
        req.partnerId = wechatPayInfo.getPartnerid();
        req.prepayId = wechatPayInfo.getPrepayid();//预支付订单号
        req.nonceStr = wechatPayInfo.getNoncestr();
        req.timeStamp = wechatPayInfo.getTimestamp() + "";
        req.packageValue = wechatPayInfo.getPackageValue();
        req.sign = wechatPayInfo.getSign();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        //3.调用微信支付sdk支付方法
        api.sendReq(req);
    }


    private void showLog(String msg){
        Log.e("result", "" + msg);
    }
}
