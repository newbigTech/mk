package com.hand.hmall.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name DateUtil
 * @description 时间工具类
 * @date 2017/6/2 19:49
 */
public class DateUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date parse(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        } else {
            try {
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    public static Date parse(String dateStr, SimpleDateFormat _sdf) {
        if (StringUtils.isBlank(dateStr) || _sdf == null) {
            return null;
        } else {
            try {
                return _sdf.parse(dateStr);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    public static String format(Date date) {
        return sdf.format(date);
    }

    public static String format(Date date, SimpleDateFormat _sdf) {
        return _sdf.format(date);
    }
}
