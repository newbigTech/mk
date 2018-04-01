package com.hand.hmall.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name StringUtils
 * @description String工具类
 * @date 2017年5月26日10:52:23
 */
public class StringUtils {

    /**
     * 判断字符是否为空
     *
     * @param targetStr 目标字符
     * @return boolean 空TRUE/非空FALSE
     * @author peng.chen03@hand-china.com
     */
    public static boolean isEmpty(String targetStr) {
        return targetStr == null ? true : targetStr.trim().length() <= 0;
    }

    /**
     * 判断字符是否为数字
     *
     * @param str 目标字符
     * @return boolean 空TRUE/非空FALSE
     * @author peng.chen03@hand-china.com
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
