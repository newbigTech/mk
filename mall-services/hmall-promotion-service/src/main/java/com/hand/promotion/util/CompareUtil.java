package com.hand.promotion.util;


import com.hand.promotion.pojo.activity.ChildPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.ParameterPojo;
import com.hand.promotion.pojo.enums.ConditionActions;
import com.hand.promotion.pojo.enums.OperatorMenu;
import com.hand.promotion.pojo.order.OrderEntryPojo;
import com.hand.promotion.pojo.order.OrderMatchedProductInfoPojo;
import com.hand.promotion.pojo.order.OrderPojo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class CompareUtil {

    /**
     * 数值型比较
     *
     * @param operator 操作符
     * @param vd1      第一个数值
     * @param vd2      第二个数值
     * @return 满足条件true，反之false
     */
    public static Boolean compareNumber(String operator, Double vd1, Double vd2) {
        boolean result = false;
        OperatorMenu om = OperatorMenu.findOperatorMenuByValue(operator);
        switch (om) {
            //>=
            case GEATER_THAN_OR_EQUAL:
                if (DecimalCalculate.compareTo(vd1, vd2) >= 0) {
                    result = true;
                }
                break;
            //>
            case GEATER_THAN:
                if (DecimalCalculate.compareTo(vd1, vd2) == 1) {
                    result = true;
                }
                break;
            //==
            case EQUAL:
                if (DecimalCalculate.compareTo(vd1, vd2) == 0) {
                    result = true;
                }
                break;
            //<=
            case LESS_THAN_OR_EQUAL:
                if (DecimalCalculate.compareTo(vd1, vd2) <= 0) {
                    result = true;
                }
                break;
            //<
            case LESS_THAN:
                if (DecimalCalculate.compareTo(vd1, vd2) == -1) {
                    result = true;
                }
                break;
            default:
                break;

        }
        return result;
    }

    /**
     * 返回满足条件范围内的sku的的相关信息
     *
     * @param rangValue 条件范围
     * @param orderPojo 订单sku
     */
    public static OrderMatchedProductInfoPojo compareSkuRange(String rangeOperator, List<String> rangValue, OrderPojo orderPojo) {
        OrderMatchedProductInfoPojo calSkuInfo = new OrderMatchedProductInfoPojo();
        OperatorMenu om = OperatorMenu.findOperatorMenuByValue(rangeOperator);
        switch (om) {
            //包含
            case MEMBER_OF:
                calSkuInfo = findSkuContains(rangValue, orderPojo, true);
                break;
            //不包含
            case NOT_MEMBER_OF:
                calSkuInfo = findSkuContains(rangValue, orderPojo, false);
                break;
            case CONTAINS:
                break;
            case NOT_CONTAINS:
                break;
            case IN:
                break;
            case NOT_IN:
                break;
            default:
                break;
        }
        return calSkuInfo;
    }

    /**
     * 返回满足条件范围内的sku的的相关信息(G部门)
     *
     * @param rangValue 条件范围
     * @param orderPojo 订单sku
     */
    public static OrderMatchedProductInfoPojo compareSkuGdeptRange(String rangeOperator, List<String> rangValue, OrderPojo orderPojo) {
        OrderMatchedProductInfoPojo calSkuInfo = new OrderMatchedProductInfoPojo();
        OperatorMenu om = OperatorMenu.findOperatorMenuByValue(rangeOperator);
        switch (om) {
            //包含
            case MEMBER_OF:
                calSkuInfo = findSkuGdeptContains(rangValue, orderPojo, true);
                break;
            //不包含
            case NOT_MEMBER_OF:
                calSkuInfo = findSkuGdeptContains(rangValue, orderPojo, false);
                break;
            case CONTAINS:
                break;
            case NOT_CONTAINS:
                break;
            case IN:
                break;
            case NOT_IN:
                break;
            default:
                break;
        }
        return calSkuInfo;
    }

    /**
     * 公共范围方法
     * 用于比较是否满足范围条件
     * 客户范围，地区范围，渠道范围
     */
    public static Boolean compareCommonRange(String operator, List<String> rangValue, String orderRange) {
        OperatorMenu om = OperatorMenu.findOperatorMenuByValue(operator);
        switch (om) {
            //包含
            case MEMBER_OF:
                if (rangValue.contains(orderRange)) {
                    return true;
                }
                break;
            //不包含
            case NOT_MEMBER_OF:
                if (!rangValue.contains(orderRange)) {
                    return false;
                }
                break;
            case CONTAINS:
                break;
            case NOT_CONTAINS:
                break;
            case IN:
                break;
            case NOT_IN:
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 返回订单中满足条件范围内的商品编码集合
     *
     * @param rangValue 条件范围
     */
    public static OrderMatchedProductInfoPojo findSkuContains(List<String> rangValue, OrderPojo orderPojo, boolean contanisFlag) {
        List<OrderEntryPojo> entryList = new ArrayList<>();
        List<OrderEntryPojo> matchedEntrys = orderPojo.getMatchedProduct().getMatchedEntrys();
        for (OrderEntryPojo matchedEntry : matchedEntrys) {
            //包含
            if (contanisFlag) {
                if (rangValue.contains(matchedEntry.getProductId())) {
                    entryList.add(matchedEntry);
                }
            } else {//不包含
                if (!rangValue.contains(matchedEntry.getProductId())) {
                    entryList.add(matchedEntry);
                }
            }
        }
        OrderMatchedProductInfoPojo calSkuInfo = calOrderMatchedSkuInfo(entryList);
        return calSkuInfo;
    }

    /**
     * 返回满足条件范围内的sku的总数
     *
     * @param rangValue    条件范围
     * @param orderPojo    订单sku
     * @param contanisFlag 是否是容器
     */
    public static OrderMatchedProductInfoPojo findSkuGdeptContains(List<String> rangValue, OrderPojo orderPojo, boolean contanisFlag) {
        List<OrderEntryPojo> matchEntrys = new ArrayList<>();
        List<OrderEntryPojo> entrys = orderPojo.getMatchedProduct().getMatchedEntrys();
        for (OrderEntryPojo entryInfo : entrys) {
            //包含
            if (contanisFlag) {
                if (rangValue.contains(entryInfo.getCategoryId())) {
                    matchEntrys.add(entryInfo);
                }
            } else {//不包含
                if (!rangValue.contains(entryInfo.getCategoryId())) {
                    matchEntrys.add(entryInfo);
                }
            }
        }
        OrderMatchedProductInfoPojo calSkuInfo = calOrderMatchedSkuInfo(matchEntrys);
        return calSkuInfo;
    }

    /**
     * 从汇总信息中计算单价和总价
     * 汇总满足商品、类别范围的商品的总价、总数量信息
     *
     * @param matchedEntrys 满足条件的订单行
     */
    public static OrderMatchedProductInfoPojo calOrderMatchedSkuInfo(List<OrderEntryPojo> matchedEntrys) {
        Integer totalQty = 0;
        Double totalFee = 0D;
        OrderMatchedProductInfoPojo matchedProductInfo = new OrderMatchedProductInfoPojo();
        for (OrderEntryPojo entry : matchedEntrys) {
            totalFee = DecimalCalculate.add(totalFee, entry.getTotalFee());
            totalQty = totalQty + entry.getQuantity();
        }
        matchedProductInfo.setProductTotalPrice(totalFee);
        matchedProductInfo.setProductTotalQty(totalQty);
        matchedProductInfo.setMatchedEntrys(matchedEntrys);
        return matchedProductInfo;
    }

    /**
     * 获取容器匹配的订单行集合
     *
     * @param orderPojo
     * @param containerPojo
     * @return
     */
    public static OrderMatchedProductInfoPojo findContainerMatchedEntrys(OrderPojo orderPojo, ContainerPojo containerPojo) {
        List<ChildPojo> childs = containerPojo.getChild();
        //保存匹配容器条件的订单行信息
        List<OrderEntryPojo> containerMatchedEntrys = new ArrayList<>();
        //遍历容器中的条件,获取满足条件的订单行数据
        for (ChildPojo child : childs) {
            //目标包现在只处理商品类别
            if (!ConditionActions.o_product_range.name().equals(child.getDefinitionId())) {
                continue;
            }
            ParameterPojo childParameters = child.getParameters();
            OrderMatchedProductInfoPojo childMatchedEntrys = compareSkuRange(childParameters.getRangeOperator().getValue(), childParameters.getRangeValue().getValue(), orderPojo);
            //child匹配的订单行集合与容器匹配的订单行取交集
            if (CollectionUtils.isEmpty(containerMatchedEntrys)) {
                containerMatchedEntrys.addAll(childMatchedEntrys.getMatchedEntrys());
            } else {
                containerMatchedEntrys.retainAll(childMatchedEntrys.getMatchedEntrys());
            }
        }
        OrderMatchedProductInfoPojo calSkuInfo = calOrderMatchedSkuInfo(containerMatchedEntrys);
        return calSkuInfo;
    }
}
