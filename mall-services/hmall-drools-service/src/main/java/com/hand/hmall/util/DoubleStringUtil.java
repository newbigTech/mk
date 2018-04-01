package com.hand.hmall.util;

import java.text.DecimalFormat;

/**
 * Created by hand on 2017/3/16.
 */
public class DoubleStringUtil {
     public static Double toDoubleTwoBit(double originData){
         DecimalFormat decimalFormat=new DecimalFormat("#0.00");
         return Double.parseDouble(decimalFormat.format(originData));
     }

    public static String toStringTwoBit(double originData){
        DecimalFormat decimalFormat=new DecimalFormat("#0.00");
        return  decimalFormat.format(originData);
    }
}
