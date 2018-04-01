package com.hand.hmall.service;


import java.util.Map;

public interface
IActionService {

    /**
     * 订单减元
     *
     * @param variables 数据模型（订单、商品、responseData）
     *
     * @param actionData 简单类型的前台数据
     */
    void orderDiscount(Map variables, double actionData);

    /**
     * 订单行减元
     *
     * @param variables 数据模型（订单、商品、responseData）
     *
     * @param actionData 简单类型的前台数据
     */
    void orderEntryDiscount(Map variables, double actionData);

    /**
     * 订单每满X元 减元、打折
     *
     * @param variables 数据模型（订单、商品、responseData）
     *
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     *
     * @param type 类型（减元或者打折）
     */
    void orderMeetDiscount(Map variables, String activityId, String type);
    /**
     * 订单行每满X元 减元、打折
     *
     * @param variables 数据模型（订单、商品、responseData）
     *
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     *
     * @param type 类型（减元或者打折）
     */
    void orderEntryMeetDiscount(Map variables, String activityId, String type);

    /**
     * 订单打折
     *
     * @param variables 数据模型（订单、商品、responseData）
     *
     *  @param actionData 简单类型的前台数据
     */
    void orderPercentageDiscount(Map variables, double actionData);
    /**
     * 订单行打折
     *
     * @param variables 数据模型（订单、商品、responseData）
     *
     *  @param actionData 简单类型的前台数据
     */
    void orderEntryPercentageDiscount(Map variables, double actionData);

    /**
     * 免单
     *
     * @param variables 数据模型（订单、商品、responseData）
     */
    void orderExempt(Map variables, Integer number);
    /**
     * 是否执行了优惠券优惠逻辑
     *
     *  @param variables 数据模型（订单、商品、responseData）
     *
     */
    void checkedCoupon(Map variables);

    /**
     * 是否执行了促销优惠逻辑
     *
     *
     * @param variables 数据模型（订单、商品、responseData）
     */
    void checkedActivity(Map variables, String activityId);

    /**
     * 抢购，针对前X件商品打折或者减价
     *
     * @param variables  数据模型（订单、商品、responseData）
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     * @param type       抢购的优惠的类型（打折和减元）
     */
    void scareBuying(Map variables, String activityId, String type);

    /**
     * 免运费
     *
     *  @param variables 数据模型（订单、商品、responseData）
     */
    void freeFreight(Map variables);

    /**
     * 赠送优惠券
     *  @param variables 数据模型（订单、商品、responseData）
     *
     *  @param activityId 根据促销的id查询出复杂的数据，然后解析
     */
    void sendCoupon(Map variables, String activityId);

    /**
     * 固定价格、折扣购买商品
     *  @param variables 数据模型（订单、商品、responseData）
     *
     * @param value 简单类型的结果数据
     *
     * @param type 抢购的优惠的类型（打折和固定价格）
     */
    void productDiscount(Map variables, Double value, String type);

    /**
     * 赠送赠品
     *
     *  @param variables 数据模型（订单、商品、responseData）
     *
     * @param activityId 根据促销的id查询出复杂的数据，然后解析

     */
    void sendGift(Map variables, String activityId);

    /**
     * 固定价格、折扣购买其他商品
     *
     *  @param variables 数据模型（订单、商品、responseData）
     *
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
     *
     * @param type 抢购的优惠的类型（打折和固定价格）
     */
    void useContainer(Map variables, String activityId, String type) throws Exception;

    /**
    * 目标包商品
     *
     *  @param variables 数据模型（订单、商品、responseData）
     *
     * @param activityId 根据促销的id查询出复杂的数据，然后解析
    */
    void targetPackage(Map variables, String activityId) throws Exception;

    /**
     * 换购  暂时没用
     * @param variables
     * @param activityId
     */
    void purchaseOther(Map variables,String activityId);

    /**
     * 订单阶梯促销
     * @param variables
     * @param activityId
     */
    void laddersDiscount(Map variables,String activityId);

    /**
     * x件商品减Y元
     *
     * @param variables
     * @param activityId
     */
    void productFixDiscount(Map variables, String activityId);

    /**
     * 订单阶梯促销
     *
     * @param variables
     * @param activityId
     */
    void productPercentageDiscount(Map variables, String activityId);

}
