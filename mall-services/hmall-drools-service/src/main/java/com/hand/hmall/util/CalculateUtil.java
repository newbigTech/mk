package com.hand.hmall.util;

import java.math.BigDecimal;

/**
 * @author Youlong.Peng
 * @Title: CalculateUtil
 * @Description: Java的简单类型不能够精确的对浮点数进行运算，该工具类提供精确的浮点数运算，包括加减乘除和四舍五入等
 * @date: 2017年8月28日
 */
public class CalculateUtil {

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    /************************************************************加法运算***********************************************/
    /**
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @description 提供精确的加法运算
     * @author youlong.peng
     */
    public static int add(final Integer v1, final Integer v2) {
        BigDecimal b1;
        BigDecimal b2;
        if (null == v1) {
            b1 = new BigDecimal(0);
        } else {
            b1 = new BigDecimal(v1);
        }

        if (null == v2) {
            b2 = new BigDecimal(0);
        } else {
            b2 = new BigDecimal(v2);
        }
        return b1.add(b2).intValue();
    }

    /**
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @description 提供精确的加法运算
     * @author youlong.peng
     */
    public static int add(final int v1, final int v2) {
        final BigDecimal b1 = new BigDecimal(v1);
        final BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).intValue();
    }

    /**
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @description 提供精确的加法运算
     * @author youlong.peng
     */
    public static long add(final Long v1, final Long v2) {
        BigDecimal b1;
        BigDecimal b2;
        if (null == v1) {
            b1 = new BigDecimal(0);
        } else {
            b1 = new BigDecimal(v1);
        }

        if (null == v2) {
            b2 = new BigDecimal(0);
        } else {
            b2 = new BigDecimal(v2);
        }
        return b1.add(b2).longValue();
    }

    /**
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @description 提供精确的加法运算
     * @author youlong.peng
     */
    public static long add(final long v1, final long v2) {
        final BigDecimal b1 = new BigDecimal(v1);
        final BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).longValue();
    }

    /**
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @description 提供精确的加法运算
     * @author youlong.peng
     */
    public static double add(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @description 提供精确的加法运算
     * @author youlong.peng
     */
    public static float add(final float v1, final float v2) {
        final BigDecimal b1 = new BigDecimal(Float.toString(v1));
        final BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.add(b2).floatValue();
    }

    /************************************************************累加运算***********************************************/
    /**
     * @param vals int类型的变量数组
     * @return 多个int类型数字的和
     * @description 累加运算
     * @author youlong.peng
     */
    public static int arraySum(final int... vals) {
        if (vals == null || vals.length <= 0) {
            return 0;
        }
        BigDecimal sum = new BigDecimal(0);
        for (final int val : vals) {
            final BigDecimal b = new BigDecimal(val);
            sum = sum.add(b);
        }
        return sum.intValue();
    }

    /**
     * @param vals long类型的变量数组
     * @return 多个long类型数字的和
     * @description 累加运算
     * @author youlong.peng
     */
    public static long arraySum(final long... vals) {
        if (vals == null || vals.length <= 0) {
            return 0;
        }
        BigDecimal sum = new BigDecimal(0);
        for (final long val : vals) {
            final BigDecimal b = new BigDecimal(val);
            sum = sum.add(b);
        }
        return sum.longValue();
    }

    /**
     * @param vals double类型的变量数组
     * @return 多个double类型数字的和
     * @description 累加运算
     * @author youlong.peng
     */
    public static double arraySum(final double... vals) {
        if (vals == null || vals.length <= 0) {
            return 0;
        }
        BigDecimal sum = new BigDecimal(0);
        for (final double val : vals) {
            final BigDecimal b = new BigDecimal(Double.toString(val));
            sum = sum.add(b);
        }
        return sum.doubleValue();
    }

    /************************************************************减法运算***********************************************/
    /**
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     * @description 提供精确的减法运算
     * @author youlong.peng
     */
    public static double sub(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     * @description 提供精确的减法运算
     * @author youlong.peng
     */
    public static float sub(final float v1, final float v2) {
        final BigDecimal b1 = new BigDecimal(Float.toString(v1));
        final BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.subtract(b2).floatValue();
    }

    /**
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     * @description 提供精确的减法运算
     * @author youlong.peng
     */
    public static int sub(final Integer v1, final Integer v2) {
        BigDecimal b1;
        BigDecimal b2;
        if (null == v1) {
            b1 = new BigDecimal(0);
        } else {
            b1 = new BigDecimal(v1);
        }

        if (null == v2) {
            b2 = new BigDecimal(0);
        } else {
            b2 = new BigDecimal(v2);
        }
        return b1.subtract(b2).intValue();
    }

    /**
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     * @description 提供精确的减法运算
     * @author youlong.peng
     */
    public static int sub(final int v1, final int v2) {
        final BigDecimal b1 = new BigDecimal(v1);
        final BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).intValue();
    }

    /**
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     * @description 提供精确的减法运算
     * @author youlong.peng
     */
    public static long sub(final Long v1, final Long v2) {

        BigDecimal b1;
        BigDecimal b2;

        if (null == v1) {
            b1 = new BigDecimal(0);
        } else {
            b1 = new BigDecimal(v1);
        }

        if (null == v2) {
            b2 = new BigDecimal(0);
        } else {
            b2 = new BigDecimal(v2);
        }
        return b1.subtract(b2).longValue();
    }

    /**
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     * @description 提供精确的减法运算
     * @author youlong.peng
     */
    public static long sub(final long v1, final long v2) {
        final BigDecimal b1 = new BigDecimal(v1);
        final BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).longValue();
    }

    /************************************************************乘法运算***********************************************/
    /**
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     * @description 提供精确的乘法运算, 不处理小数
     * @author youlong.peng
     */
    public static double mul(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     * @description 提供精确的乘法运算, 默认保留10位小数，四舍五入
     * @author youlong.peng
     */
    public static double mulround(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param v1        被乘数
     * @param v2        乘数
     * @param scale     精确的小数位数
     * @param operation 小数位处理时，向上进位-UP，或者直接舍位-DOWN
     *                  可选值为 UP/DOWN
     *                  默认做向上进位，如果参数为非法值，则做默认处理
     * @return 两个参数的积
     * @description 提供精确的乘法运算, 默认保留10位小数，四舍五入
     * @author youlong.peng
     */
    public static double mul(final double v1, final double v2, final int scale, String operation) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).setScale(scale, operation.equals("DOWN") ? BigDecimal.ROUND_DOWN : BigDecimal.ROUND_UP).doubleValue();

    }

    /**
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     * @description 提供精确的乘法运算
     * @author youlong.peng
     */
    public static int mul(final int v1, final int v2) {
        final BigDecimal b1 = new BigDecimal(v1);
        final BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).intValue();
    }

    /**
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     * @description 提供精确的乘法运算
     * @author youlong.peng
     */
    public static int mul(final Integer v1, final Integer v2) {
        BigDecimal b1;
        BigDecimal b2;
        if (null == v1 || null == v2 || 0 == v1 || 0 == v2) {
            return 0;
        } else {
            b1 = new BigDecimal(v1);
            b2 = new BigDecimal(v2);
            return b1.multiply(b2).intValue();
        }
    }

    /************************************************************除法运算***********************************************/
    /**
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     * @description 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后n位，以后的数字四舍五入
     * @author youlong.peng
     */
    public static double div(final int v1, final int v2, final int scale) {

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

        final BigDecimal b1 = new BigDecimal(v1);
        final BigDecimal b2 = new BigDecimal(v2);
        final double quotient = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return quotient;
    }

    /**
     * @param v1        被除数
     * @param v2        除数
     * @param scale     表示需要精确到小数点以后几位
     * @param operation 小数位处理时，向上进位-UP，或者直接舍位-DOWN
     *                  可选值为 UP/DOWN
     *                  默认做向上进位，如果参数为非法值，则做默认处理
     * @return 两个参数的商
     * @description 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     * @author youlong.peng
     */
    public static double div(final int v1, final int v2, final int scale, final String operation) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        final BigDecimal b1 = new BigDecimal(v1);
        final BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, operation.equals("DOWN") ? BigDecimal.ROUND_DOWN : BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * @param v1
     * @param v2
     * @return 两个整型数据相除，四舍五入返回整型结果
     * @author youlong.peng
     */
    public static int divInt(final int v1, final int v2) {
        return (int) div(v1, v2, 0);
    }

    /**
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     * @description 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后10位，以后的数字四舍五入
     * @author youlong.peng
     */
    public static double div(final int v1, final int v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     * @description 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后10位，以后的数字四舍五入
     * @author youlong.peng
     */
    public static double div(final Integer v1, final Integer v2) {
        if (null == v1 || null == v2 || 0 == v1.intValue() || 0 == v2.intValue()) {
            return 0;
        } else {
            return div(v1.intValue(), v2.intValue(), DEF_DIV_SCALE);
        }
    }

    /**
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     * @description 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     * @author youlong.peng
     */
    public static double div(final double v1, final double v2, final int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param v1        被除数
     * @param v2        除数
     * @param scale     表示需要精确到小数点以后几位
     * @param operation 小数位处理时，向上进位-UP，或者直接舍位-DOWN
     *                  可选值为 UP/DOWN
     *                  默认做向上进位，如果参数为非法值，则做默认处理
     * @return 两个参数的商
     * @description 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     * @author youlong.peng
     */
    public static double div(final double v1, final double v2, final int scale, final String operation) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, operation.equals("DOWN") ? BigDecimal.ROUND_DOWN : BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     * @description 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后10位，以后的数字四舍五入
     * @author youlong.peng
     */
    public static double div(final double v1, final double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }
    /************************************************************四舍五入***********************************************/
    /**
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     * @description 提供精确的小数位四舍五入处理
     * @author youlong.peng
     */
    public static double round(final double v, final int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        final BigDecimal b = new BigDecimal(Double.toString(v));
        final BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     * @description 提供精确的小数位四舍五入处理
     * @author youlong.peng
     */
    public static float round(final float v, final int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        final BigDecimal b = new BigDecimal(Float.toString(v));
        final BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /************************************************************数字比较***********************************************/
    /**
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 返回两个数中大的一个值
     * @description 返回两个数中大的一个值
     * @author youlong.peng
     */
    public static double returnMax(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.max(b2).doubleValue();
    }

    /**
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 返回两个数中小的一个值
     * @description 返回两个数中小的一个值
     * @author youlong.peng
     */
    public static double returnMin(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.min(b2).doubleValue();
    }

    /**
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
     * @description 精确对比两个数字
     * @author youlong.peng
     */
    public static int compareTo(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.compareTo(b2);
    }

}
