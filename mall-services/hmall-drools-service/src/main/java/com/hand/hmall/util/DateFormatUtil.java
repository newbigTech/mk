package com.hand.hmall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hand on 2017/2/21.
 */
public class DateFormatUtil {
    private static final  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String timeStampToString(String time){

        return simpleDateFormat.format(new Date(Long.parseLong(time)));
    }

    public static Long stringToTimeStamp(String time){

        try {
            Date date= simpleDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
