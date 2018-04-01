package com.hand.promotion.pojo.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/19
 * @description 正则表达式工具类
 */
public class RegexMessage {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 匹配正整数
     */
    public final static String POSITIVE_INTEGER = "^[1-9]\\d*$";

    /**
     * 非负整数
     */
    public final static String NONE_NEGITIVE_INTEGER = "^\\d+$";

    /**
     * 匹配非负数
     */
    public final static String POSITIVE_NUM = "^\\d+(\\.{0,1}\\d+){0,1}$";

    /**
     * 匹配0-10
     */
    public final static String ZERO_TEN = "^(\\d(\\.\\d)?|10)$";

    /**
     * 测试字符串是否匹配正则表达式
     *
     * @param num
     * @return
     */
    public static boolean matches(String num, String regex) {
        return Pattern.matches(regex, num);
    }


}
