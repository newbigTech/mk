package com.hand.hmall.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PriceFormule
 * @description 价格公式
 * @date 2017/9/12 16:44
 */
public class PriceFormule {

    // 采购价格计算模板
    private static final String ORDER_PRICE_FORMAT = "%s(%s)[platformPrice[%s] + totalPartPrice[%s] + specialPrice[%s]";

    // 销售价格计算模板
    private static final String SALE_PRICE_FORMAT = "%s(%s)[platformPrice[%s] + totalPartPrice[%s]";

    // 保存计算公式
    private static final ThreadLocal<List<String>> calculateFormule = new ThreadLocal<>();

    /**
     * 采购价格统计
     * @param totalPrice 组件总价格
     * @param vCode 组件v码
     * @param platformPrice 组件平台价格
     * @param totalPartPrice 组件总零部件价格
     * @param specialPrice 组件特殊工艺价格
     */
    public static void setOrderPrice(Double totalPrice, String vCode, Double platformPrice, Double totalPartPrice, Double specialPrice) {
        String formule = String.format(ORDER_PRICE_FORMAT, totalPrice, vCode, platformPrice, totalPartPrice, specialPrice);
        if (calculateFormule.get() == null) {
            calculateFormule.set(new ArrayList<>());
        }
        calculateFormule.get().add(formule);
    }

    /**
     * 采购价格统计
     * @param totalPrice 组件总价格
     * @param vCode 组件v码
     * @param platformPrice 组件平台价格
     * @param totalPartPrice 组件总零部件价格
     */
    public static void setSalePrice(Double totalPrice, String vCode, Double platformPrice, Double totalPartPrice) {
        String formule = String.format(SALE_PRICE_FORMAT, totalPrice, vCode, platformPrice, totalPartPrice);
        if (calculateFormule.get() == null) {
            calculateFormule.set(new ArrayList<>());
        }
        calculateFormule.get().add(formule);
    }

    /**
     * 获取价格公式
     * @param totalPrice 套件总价格
     * @param vCode 套件v码
     * @return String
     */
    public static String getCalculateFormule(Double totalPrice, String vCode) {
        StringBuffer sb = new StringBuffer();
        sb.append(totalPrice).append("(").append(vCode).append(") = ");
        calculateFormule.get().stream().forEach(formula -> sb.append(formula).append(" + "));
        sb.delete(sb.lastIndexOf(" + "), sb.length());
        calculateFormule.remove();
        return sb.toString();
    }
}
