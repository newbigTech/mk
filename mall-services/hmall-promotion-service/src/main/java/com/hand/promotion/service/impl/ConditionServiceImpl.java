package com.hand.promotion.service.impl;


import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.activity.ChildPojo;
import com.hand.promotion.pojo.activity.ConditionPojo;
import com.hand.promotion.pojo.activity.ContainerPojo;
import com.hand.promotion.pojo.activity.GroupPojo;
import com.hand.promotion.pojo.activity.PromotionActivitiesPojo;
import com.hand.promotion.pojo.coupon.PromotionCouponsPojo;
import com.hand.promotion.pojo.enums.ConditionActions;
import com.hand.promotion.pojo.enums.MsgMenu;
import com.hand.promotion.pojo.enums.OperatorMenu;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.pojo.order.OrderEntryPojo;
import com.hand.promotion.pojo.order.OrderMatchedProductInfoPojo;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.IConditionService;
import com.hand.promotion.util.CompareUtil;
import com.hand.promotion.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author zhuweifeng
 * @date 2017/9/4
 * 条件计算service
 * 用途：用来根据订单和规则来计算是否满足条件
 */
@Service
public class ConditionServiceImpl implements IConditionService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 无条件
     */
    private final String NO_CONDITIONS = "NO_CONDITIONS";
    /**
     * 条件不满足
     */
    private final String NOT_MATCH_RULE = "CONDITION_NOT_MATCH";


    /**
     * 检查促销活动的条件是否正确
     *
     * @param order 订单
     * @param rule  规则
     */
    @Override
    public SimpleMessagePojo checkIsMatchCondition(OrderPojo order, PromotionActivitiesPojo rule) {
        //1.计算条件
        SimpleMessagePojo conditionResult = findConditionMatchResults(order, rule.getConditions(), rule.getGroups(), rule.getContainers(), rule.getActivity().getId());
        if (!conditionResult.isSuccess()) {
            return new SimpleMessagePojo(false, MsgMenu.NOT_MATCH_RULE, null);
        }
        //2.将结果存入订单
        OrderPojo conditionResultObj = (OrderPojo) conditionResult.getObj();
        return new SimpleMessagePojo(true, null, conditionResult.getObj());
    }

    /**
     * 检查订单是否满足使用条件
     *
     * @param order 订单
     * @param rule  规则
     */
    @Override
    public SimpleMessagePojo checkCouponIsMatchCondition(OrderPojo order, PromotionCouponsPojo rule) {
        //1.计算条件
        SimpleMessagePojo conditionResult = findConditionMatchResults(order, rule.getConditions(), rule.getGroups(), rule.getContainers(), rule.getCoupon().getCouponId());
        if (!conditionResult.isSuccess()) {
            return new SimpleMessagePojo(false, MsgMenu.NOT_MATCH_RULE, null);
        }
        //2.将结果存入订单
        return new SimpleMessagePojo(true, null, conditionResult.getObj());
    }

    /**
     * 条件判定结果
     */
    public SimpleMessagePojo findConditionMatchResults(OrderPojo order, List<ConditionPojo> conditions, List<GroupPojo> groups, List<ContainerPojo> containers, String activityId) {
        //1.验证3种条件是否存在
        if (CollectionUtils.isEmpty(conditions)
            && CollectionUtils.isEmpty(groups)
            && CollectionUtils.isEmpty(containers)) {
            logger.info("NO CONDITIONS found in this rule:{}", activityId);
            return new SimpleMessagePojo(false, MsgMenu.NO_CONDITIONS, null);
        }

        //3.基本条件
        SimpleMessagePojo baseConditionResult = checkInBaseConditions(order, conditions, false);
        logger.info("满足基本条件:{}", baseConditionResult.isSuccess());
        if (!baseConditionResult.isSuccess()) {
            return new SimpleMessagePojo(false, null, null);
        }

        //4.组条件
        if (!CollectionUtils.isEmpty(groups)) {
            SimpleMessagePojo groupConditionResult = checkInGroupConditions(order, groups);
            logger.info("满足组条件:{}", groupConditionResult.isSuccess());
            if (!groupConditionResult.isSuccess()) {
                return new SimpleMessagePojo(false, null, null);
            }
        }

        //5.容器条件
        if (!CollectionUtils.isEmpty(containers)) {
            SimpleMessagePojo containerConditionResult = checkInContainerConditions(order, containers);
            logger.info("满足容器条件:{}", containerConditionResult.isSuccess());
            if (!containerConditionResult.isSuccess()) {
                return new SimpleMessagePojo(false, null, null);
            }
        }
        return new SimpleMessagePojo(true, null, order);
    }

    /**
     * 基本条件判定
     * true:满足条件
     * false:不满足条件
     *
     * @param order         参与促销条件判定的订单数据
     * @param conditions    一条促销规则条件的集合
     * @param containerFlag 是否是容器
     */
    @Override
    public SimpleMessagePojo checkInBaseConditions(OrderPojo order, List<ConditionPojo> conditions, Boolean containerFlag) {
        //是否匹配标志
        Boolean matchFlag = true;
        if (CollectionUtils.isEmpty(conditions)) {
            logger.info("无基本条件:返回true，执行之后判定");
            return new SimpleMessagePojo();
        }
        //遍历所有条件
        for (ConditionPojo condition : conditions) {
            //遇到一个不匹配的条件直接跳出for循环
            if (!matchFlag) {
                break;
            }
            if (StringUtils.isEmpty(condition.getDefinitionId())) {
                logger.info("基本条件definitionId为空，跳过该条件");
                continue;
            }
            ConditionActions conditionActions = ConditionActions.getConditionActionsByValue(condition.getDefinitionId());
            if (conditionActions == null) {
                logger.info("基本条件definitionId:{}无效，跳过该条件", condition.getDefinitionId());
                continue;
            }
            String operator = null;
            Double value = null;
            String rangOperator = null;
            List<String> rangValue = new ArrayList<>();
            //匹配规则的sku信息
            OrderMatchedProductInfoPojo skuMatchCounts;
            //汇总订单总价，总件数，sku的集合,当满足总价格和总件数时直接使用
            OrderMatchedProductInfoPojo orderMatchedProductInfoPojo = order.getMatchedProduct();
            switch (conditionActions) {
                //订单满X元
                case o_total_reached:
                    Double totalPrice = order.getOrderAmount();
                    operator = condition.getParameters().transOperatorPojo().getValue();
                    value = Double.parseDouble(condition.getParameters().transValuePojo().getValue());
                    matchFlag = CompareUtil.compareNumber(operator, totalPrice, value);
                    break;
                //订单满X件
                case o_quantity_reached:
                    double totalQuantity = order.getQuantity() * 1.0;
                    String qrOpt = condition.getParameters().transOperatorPojo().getValue();
                    Double qrValue = Double.parseDouble(condition.getParameters().transValuePojo().getValue());
                    matchFlag = CompareUtil.compareNumber(qrOpt, totalQuantity, qrValue);
                    break;
                //订单行满x元
                case oe_total_reached:
                    value = Double.parseDouble(condition.getParameters().transValuePojo().getValue());
                    operator = condition.getParameters().transOperatorPojo().getValue();
                    List<OrderEntryPojo> orderEntryPojoList = order.getOrderEntryList();
                    //临时存储满足条件的订单行
                    OrderMatchedProductInfoPojo matchedProductInfo = new OrderMatchedProductInfoPojo();
                    List<OrderEntryPojo> matchedEntrys = new ArrayList<>();
                    for (OrderEntryPojo orderEntryPojo : orderEntryPojoList) {
                        boolean isMatched = CompareUtil.compareNumber(operator, orderEntryPojo.getTotalFee(), value);
                        if (isMatched) {
                            matchedEntrys.add(orderEntryPojo);
                        }
                    }
                    if (CollectionUtils.isEmpty(matchedEntrys)) {
                        matchFlag = false;
                    }
                    matchedProductInfo.setMatchedEntrys(matchedEntrys);
                    order.setMatchedProduct(compareOrderMatchedSkuInfoPojo(order, matchedProductInfo, containerFlag));
                    break;

                //商品范围(件)
                case o_product_range:
                    //1.商品范围-查询符合条件中商品范围的订单的sku的数量
                    rangOperator = condition.getParameters().getRangeOperator().getValue();
                    rangValue = condition.getParameters().getRangeValue().getValue();
                    //如果商品范围为空，范围设置为订单所有sku
                    if (CollectionUtils.isEmpty(rangValue)) {
                        rangValue = findOrderAllSkus(order);
                    }
                    skuMatchCounts = CompareUtil.compareSkuRange(rangOperator, rangValue, order);
                    if (skuMatchCounts.getProductTotalQty() == 0) {
                        //订单sku在商品条件范围中没有
                        matchFlag = false;
                    }
                    //2.符合范围的商品数量
                    if (matchFlag) {
                        operator = condition.getParameters().transOperatorPojo().getValue();
                        value = Double.parseDouble(condition.getParameters().transValuePojo().getValue());
                        //匹配到的件数
                        Double prSkuMatchCounts = Double.parseDouble(String.valueOf(skuMatchCounts.getProductTotalQty()));
                        matchFlag = CompareUtil.compareNumber(operator, prSkuMatchCounts, value);
                        order.setMatchedProduct(compareOrderMatchedSkuInfoPojo(order, skuMatchCounts, containerFlag));
                    }
                    break;
                //商品范围（总价）
                case o_product_range_number:
                    //1.商品范围-查询符合条件中商品范围的订单的sku的总价
                    rangOperator = condition.getParameters().getRangeOperator().getValue();
                    rangValue = condition.getParameters().getRangeValue().getValue();
//                    //如果商品范围为空，范围设置为订单所有sku
//                    if (CollectionUtils.isEmpty(rangValue)) {
//                        rangValue = findOrderAllSkus(order);
//                    }
                    skuMatchCounts = CompareUtil.compareSkuRange(rangOperator, rangValue, order);
                    if (skuMatchCounts.getProductTotalQty() == 0) {
                        //订单sku在商品条件范围中没有
                        matchFlag = false;
                    }
                    //2.符合范围的商品价格
                    if (matchFlag) {
                        operator = condition.getParameters().transOperatorPojo().getValue();
                        value = Double.parseDouble(condition.getParameters().transValuePojo().getValue());
                        //匹配到的价格
                        Double prSkuMatchCounts = skuMatchCounts.getProductTotalPrice();
                        matchFlag = CompareUtil.compareNumber(operator, prSkuMatchCounts, value);
                        order.setMatchedProduct(compareOrderMatchedSkuInfoPojo(order, skuMatchCounts, containerFlag));
                    }
                    break;
                //类别范围(件)
                case o_type_range:
                    //1.商品范围-查询符合条件中商品范围的订单的sku的G部门
                    rangOperator = condition.getParameters().getRangeOperator().getValue();
                    rangValue = condition.getParameters().getRangeValue().getValue();
//                    if (CollectionUtils.isEmpty(rangValue)) {
//                        rangValue = findOrderAllGdept(order);
//                    }
                    skuMatchCounts = CompareUtil.compareSkuGdeptRange(rangOperator, rangValue, order);
                    if (skuMatchCounts.getProductTotalQty() == 0) {
                        //订单sku在商品条件范围中没有
                        matchFlag = false;
                    }
                    //2.符合范围的商品价格
                    if (matchFlag) {
                        operator = condition.getParameters().transOperatorPojo().getValue();
                        value = Double.parseDouble(condition.getParameters().transValuePojo().getValue());
                        //匹配到的数量
                        Double prSkuMatchCounts = Double.parseDouble(String.valueOf(skuMatchCounts.getProductTotalQty()));
                        matchFlag = CompareUtil.compareNumber(operator, prSkuMatchCounts, value);
                        order.setMatchedProduct(compareOrderMatchedSkuInfoPojo(order, skuMatchCounts, containerFlag));
                    }
                    break;
                //类别范围(总价)
                case o_type_range_number:
                    //1.商品范围-查询符合条件中商品范围的订单的sku的G部门
                    rangOperator = condition.getParameters().getRangeOperator().getValue();
                    rangValue = condition.getParameters().getRangeValue().getValue();
                    if (CollectionUtils.isEmpty(rangValue)) {
                        rangValue = findOrderAllGdept(order);
                    }
                    skuMatchCounts = CompareUtil.compareSkuGdeptRange(rangOperator, rangValue, order);
                    if (skuMatchCounts.getProductTotalQty() == 0) {
                        //订单sku在商品条件范围中没有
                        matchFlag = false;
                    }
                    //2.符合范围的商品价格
                    if (matchFlag) {
                        operator = condition.getParameters().transOperatorPojo().getValue();
                        value = Double.parseDouble(condition.getParameters().transValuePojo().getValue());
                        //匹配到的总价
                        Double prSkuMatchPrice = Double.parseDouble(String.valueOf(skuMatchCounts.getProductTotalPrice()));
                        matchFlag = CompareUtil.compareNumber(operator, prSkuMatchPrice, value);
                        order.setMatchedProduct(compareOrderMatchedSkuInfoPojo(order, skuMatchCounts, containerFlag));
                    }
                    break;
                //客户范围
                case o_user_range:
                    //1.商品范围-查询符合条件中商品范围的订单的sku的客户范围
                    operator = condition.getParameters().transOperatorPojo().getValue();
                    rangValue = condition.getParameters().getRangeValue().getValue();
                    if (CollectionUtils.isEmpty(rangValue)) {
                        rangValue.add(order.getCustomerId());
                    }
                    matchFlag = CompareUtil.compareCommonRange(operator, rangValue, order.getCustomerId());
                    //订单级别，如果满足直接全部加入
                    order.setMatchedProduct(compareOrderMatchedSkuInfoPojo(order, orderMatchedProductInfoPojo, containerFlag));
                    break;
                //地区范围
                case o_area_range:
                    rangValue = condition.getParameters().getRangeValue().getValue();
                    //如果是快递配送比较地址中的city
                    if (PromotionConstants.EXPRESS.equals(order.getDistribution())) {
                        //如果规则中，地区范围未设置，则默认和订单的地址一致，即变相为全部地区
                        if (CollectionUtils.isEmpty(rangValue)) {
                            rangValue.add(order.getCityCode());
                        }
                        String cityId = order.getCityCode();
                        if (!rangValue.contains(cityId)) {
                            matchFlag = false;
                        }
                    }
                    //如果是门店自提比较门店ID distributionId
                    if (PromotionConstants.PICKUP.equals(order.getDistribution())) {
                        if (CollectionUtils.isEmpty(rangValue)) {
                            rangValue.add(order.getDistributionId());
                        }
                        String distributionId = order.getDistributionId();
                        if (!rangValue.contains(distributionId)) {
                            matchFlag = false;
                        }
                    }
                    //订单级别，如果满足直接全部加入
                    order.setMatchedProduct(compareOrderMatchedSkuInfoPojo(order, orderMatchedProductInfoPojo, containerFlag));
                    break;
                //渠道范围
                case o_channel_range:
                    rangValue = condition.getParameters().getRangeValue().getValue();
                    String saleChannel = order.getChannelCode();
                    if (!rangValue.contains(saleChannel)) {
                        matchFlag = false;
                    }
                    //订单级别，如果满足直接全部加入
                    order.setMatchedProduct(compareOrderMatchedSkuInfoPojo(order, orderMatchedProductInfoPojo, containerFlag));
                    break;
                default:
                    break;
            }
            logger.info("条件：{}，结果：{}", condition.getMeaning(), matchFlag);
        }
        return new SimpleMessagePojo(matchFlag, null, order);
    }

    /**
     * 条件"组"判定
     * true:满足条件
     * false:不满足条件
     */
    public SimpleMessagePojo checkInGroupConditions(OrderPojo orderPojo, List<GroupPojo> groups) {
        Boolean groupMatchFlag = true;
        for (GroupPojo group : groups) {
            ConditionPojo condition = new ConditionPojo();
            List<ChildPojo> children = group.getChild();
            //组或
            if (OperatorMenu.OR.getValue().equals(group.getOperator())) {
                Boolean childrenMatchFlag = false;
                for (ChildPojo child : children) {
                    List<ConditionPojo> conditions = new ArrayList<>();
                    condition.setDefinitionId(child.getDefinitionId());
                    condition.setParameters(child.getParameters());
                    conditions.add(condition);
                    SimpleMessagePojo result = checkInBaseConditions(orderPojo, conditions, false);
                    if (result.isSuccess()) {
                        childrenMatchFlag = true;
                    }
                    orderPojo.setMatchedProduct(compareOrderMatchedSkuInfoPojo(orderPojo, orderPojo.getMatchedProduct(), false));
                }
                if (!childrenMatchFlag) {
                    groupMatchFlag = false;
                }
            } else {//组且
                Boolean childrenMatchFlag = true;
                for (ChildPojo child : children) {
                    List<ConditionPojo> conditions = new ArrayList<>();
                    condition.setDefinitionId(child.getDefinitionId());
                    condition.setParameters(child.getParameters());
                    conditions.add(condition);
                    SimpleMessagePojo result = checkInBaseConditions(orderPojo, conditions, false);
                    if (!result.isSuccess()) {
                        childrenMatchFlag = false;
                    }
                    orderPojo.setMatchedProduct(compareOrderMatchedSkuInfoPojo(orderPojo, orderPojo.getMatchedProduct(), false));
                }
                if (!childrenMatchFlag) {
                    groupMatchFlag = false;
                }
            }
        }
        return new SimpleMessagePojo(groupMatchFlag, null, orderPojo);
    }

    /**
     * 条件"容器"判定
     * true:满足条件
     * false:不满足条件
     * 返回原订单数据
     */
    public SimpleMessagePojo checkInContainerConditions(OrderPojo orderPojo, List<ContainerPojo> containers) {
        Boolean childrenMatchFlag = true;
        //容器匹配的订单行集合
        Set<OrderEntryPojo> matchedEntrys = new HashSet<>();
        for (ContainerPojo container : containers) {
            ConditionPojo condition = new ConditionPojo();
            List<ChildPojo> children = container.getChild();
            List<ConditionPojo> conditions = new ArrayList<>();
            for (ChildPojo child : children) {
                condition.setDefinitionId(child.getDefinitionId());
                condition.setParameters(child.getParameters());
                conditions.add(condition);
            }
            //判断容器中所有条件是否满足
            SimpleMessagePojo result = checkInBaseConditions(orderPojo, conditions, true);
            if (!result.isSuccess()) {
                //容器中有一个条件不满足，直接跳出
                childrenMatchFlag = false;
                break;
            }
            //如果满足将满足条件的sku放入集合，容器之间的sku取并集
            OrderPojo orderCalInfoPojo = (OrderPojo) result.getObj();
            matchedEntrys.addAll(orderCalInfoPojo.getMatchedProduct().getMatchedEntrys());
        }
        if (childrenMatchFlag) {
            orderPojo.getMatchedProduct().setMatchedEntrys(new ArrayList<>(matchedEntrys));
        }
        return new SimpleMessagePojo(childrenMatchFlag, null, orderPojo);
    }

    /**
     * 获取满足条件的商品编码
     * 如果目前条件的sku范围小于之前的，则取小的
     */
    public OrderMatchedProductInfoPojo compareOrderMatchedSkuInfoPojo(OrderPojo orderPojo, OrderMatchedProductInfoPojo matchedPojo, Boolean containerFlag) {
        //订单之前参与促销活动sku信息
        OrderMatchedProductInfoPojo matchedProducts = orderPojo.getMatchedProduct();
        //如果没有表示是第一个促销活动，直接返回最新匹配的
        if (matchedProducts == null) {
            return matchedPojo;
        }
        List<OrderEntryPojo> orderMatchedSkus = matchedProducts.getMatchedEntrys();
        //如果这次匹配的没有，返回空
        if (matchedPojo == null) {
            return null;
        }
        List<OrderEntryPojo> matchedSkus = matchedPojo.getMatchedEntrys();
        if (containerFlag) {
            //如果是容器内的基本条件取并集后去重
            orderMatchedSkus.addAll(matchedSkus);
            ListUtil.distinctEntrys(orderMatchedSkus);
        } else {
            //和之前的取交集
            orderMatchedSkus.retainAll(matchedSkus);
        }
        matchedPojo.setMatchedEntrys(orderMatchedSkus);
        return matchedPojo;
    }

    /**
     * 获取订单所有商品编码集合  todo：：：？ 考虑去重吗？
     */
    public List<String> findOrderAllSkus(OrderPojo orderPojo) {
        List<String> skus = new ArrayList<>();
        List<OrderEntryPojo> entryPojos = orderPojo.getOrderEntryList();
        for (OrderEntryPojo entryPojo : entryPojos) {
            skus.add(entryPojo.getProduct());
        }
        return skus;
    }

    /**
     * 将订单中的所有商品类别存入集合    todo：：：？ 考虑去重吗？
     */
    public List<String> findOrderAllGdept(OrderPojo orderPojo) {
        List<String> cates = new ArrayList<>();
        List<OrderEntryPojo> entryPojos = orderPojo.getOrderEntryList();
        for (OrderEntryPojo entryPojo : entryPojos) {
            cates.add(entryPojo.getCategoryId());
        }
        return cates;
    }

}
