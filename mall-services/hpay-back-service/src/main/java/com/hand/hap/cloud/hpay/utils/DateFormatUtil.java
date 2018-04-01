package com.hand.hap.cloud.hpay.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jianjun.tan
 * @Title DateFormatUtil
 * @Description
 * @date 2017/5/5
 */
public class DateFormatUtil {


    // 商户发送交易时间 格式:YYYYMMDDhhmmss
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String format(Date date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            return null;
        }
    }

}
