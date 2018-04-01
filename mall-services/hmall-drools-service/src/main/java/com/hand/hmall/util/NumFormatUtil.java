package com.hand.hmall.util;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author XinyangMei
 * @Title NumFormatUtil
 * @Description 数字格式化
 * @date 2017/8/24 17:49
 */
public class NumFormatUtil {


    private static ThreadLocal<DecimalFormat> df = new ThreadLocal<DecimalFormat>() {

        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("#.00");
        }
    };

    public static String format(String pattern, Object num) {
        df.get().applyPattern(pattern);
        String result = "";
        try {
            df.get().setRoundingMode(RoundingMode.HALF_UP);
            df.get().format(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String format(Double num) {
        try {
            if (num.isNaN() || num < 0)
                return "0";
            df.get().setRoundingMode(RoundingMode.HALF_UP);
            return df.get().format(num);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatNotCheck(Double num) {
        try {
            df.get().setRoundingMode(RoundingMode.HALF_UP);
            return df.get().format(num);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
