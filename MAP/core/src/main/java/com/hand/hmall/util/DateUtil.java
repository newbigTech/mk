package com.hand.hmall.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name DateUtil
 * @description 时间工具类
 * @date 2017年5月26日10:52:23
 */
public class DateUtil {

    /**
     * 将日期通过某种格式转换为字符串类型
     *
     * @param date
     * @param template yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getdate(Date date, String template) {
        SimpleDateFormat sdf = new SimpleDateFormat(template);
        return sdf.format(date);
    }

    /**
     * 将map中有的Date转换为字符串格式
     *
     * @param map
     * @return
     */
    public static Map<String, Object> formDate(Map<String, Object> map) {
        for (String key : map.keySet()) {
            //获取当前key得值
            Object value = map.get(key);
            try {
                Date date = new Date((Long) value);
                map.put(key, DateUtil.getdate(date, "yyyy-MM-dd HH:mm:ss"));
            } catch (Exception ex) {
                continue;
            }
        }
        return map;

    }


    public static List<Map<String, Object>> formDate(List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, DateUtil.formDate(list.get(i)));
        }
        return list;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getStartTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = "";
        time = df.format(new Date());
        return time;
    }

    /**
     * String转date
     *
     * @param str
     * @return
     */
    public static Date getStrToDateTime(String str) {

        if (StringUtils.isEmpty(str)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取现在时间
     * @return
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取指定格式当前时间
     */
    public static Date getSpecialNowDate(String format){
        Date currentTime = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat(format);
        try {
            return sdf1.parse(sdf1.format(currentTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
