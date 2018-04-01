package com.hand.hmall.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 孙锐
 * @Title:GeneraterUtil
 * @Description: 工具类，生成当前时间字符串和流水号
 * @date 2017/5/24 14:41
 * @version 1.0
 */
public class GeneraterUtil {
    /**
     * 获取现在时间
     *
     * @return返回字符串格式yyyyMMdd
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 由年月日时分秒+8位随机数 生成流水号
     *
     * @return
     */
    public static String getNum() {
        String t = getStringDate();
        String s = getStore();
        int x = (int) (Math.random() * 90000000) + 10000000;
        String serial = s + t + x;
        return serial;
    }

    public static String getStore(){
        String s = "JB";
        return s;
    }
}
