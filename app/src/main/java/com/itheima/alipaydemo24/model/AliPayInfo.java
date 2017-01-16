package com.itheima.alipaydemo24.model;

/**支付宝支付参数模型
 * Created by youliang.ji on 2016/8/1.
 */
public class AliPayInfo {

    /**
     * payInfo :
     * errMsg : 请求成功
     * errCode : 0
     * payType : 1
     */

    private String payInfo; //支付串码
    private String errMsg;
    private String errCode;
    private String payType;

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "AliPayInfo{" +
                "payInfo='" + payInfo + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", errCode='" + errCode + '\'' +
                ", payType='" + payType + '\'' +
                '}';
    }
}
