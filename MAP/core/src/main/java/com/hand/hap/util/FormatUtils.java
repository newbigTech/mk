package com.hand.hap.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对字符进行一些处理判断的类
 * Created by heng.zhang04@hand-china.com
 * 16:21 2017/5/28.
 */
public class FormatUtils {

    /**
     * 判断是否为日期
     *
     * @param dateStr : 日期字符串
     * @return 日期
     */
    public Date getDate(String dateStr)throws Exception {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date result = null;

        try {
            result = datetimeFormat.parse(dateStr);
        } catch (ParseException e) {
            result = dateFormat.parse(dateStr);
        }

        return result;
    }

    /**
     * 判断是否为数字
     *
     * @param numberStr
     * @return boolean
     * @description 判断是否为数字
     */
    public boolean isNum(String numberStr) {
        return numberStr.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 判断字段是否为空.
     *
     * @param str 字段值
     * @return { 空: true, 非空: false}
     */
    public boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }
}
