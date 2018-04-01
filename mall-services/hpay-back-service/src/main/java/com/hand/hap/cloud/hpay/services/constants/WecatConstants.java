package com.hand.hap.cloud.hpay.services.constants;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.phoneServices.wechat.utils
 * @Description
 * @date 2017/8/21
 */
public class WecatConstants {

    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";
    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "alipay.signType";

    public enum SignType {
        MD5, HMACSHA256
    }
}
