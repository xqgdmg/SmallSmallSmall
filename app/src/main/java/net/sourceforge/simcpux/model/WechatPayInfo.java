package net.sourceforge.simcpux.model;

/**微信支付模型对象
 * Created by youliang.ji on 2016/8/1.
 */
public class WechatPayInfo {

    /**
     * appid : wxb4ba3c02aa476ea1
     * partnerid : 1305176001
     * packageValue : Sign=WXPay
     * noncestr : 6373475c08109649d9046ab1c3c6234c
     * timestamp : 1470040110
     * prepayid : wx201608011628309e487c359a0312399216
     * sign : 0D491B75666FDA1D2B13EFE4C44DB8AB
     */

    private String appid;
    private String partnerid;
    private String packageValue;
    private String noncestr;
    private int timestamp;
    private String prepayid;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WechatPayInfo{" +
                "appid='" + appid + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp=" + timestamp +
                ", prepayid='" + prepayid + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
