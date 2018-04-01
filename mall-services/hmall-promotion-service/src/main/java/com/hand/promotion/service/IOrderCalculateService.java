package com.hand.promotion.service;

import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.order.MallPromotionResult;
import com.hand.promotion.pojo.order.OrderPojo;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/27
 * @description 订单参与促销过程中，金额计算 Service
 */
public interface IOrderCalculateService {


    /**
     * 校验参与促销计算的订单是否合法
     *
     * @param orderPojo
     * @return
     */
    SimpleMessagePojo ckeckOrderInvalid(OrderPojo orderPojo);

    /**
     * 补充参与促销计算需要的其他字段
     *
     * @param orderPojo
     * @return
     */
    SimpleMessagePojo appendNecessaryField(OrderPojo orderPojo) ;

    /**
     * 用于订单层级促销计算
     *
     * @param orderPojo
     */
    void computeOrderPromotPrice(OrderPojo orderPojo);

    /**
     * 根据订单行汇总订单的初始匹配商品
     * @param orderPojo
     */
    void collectMatchedProduct(OrderPojo orderPojo);

    /**
     * 用于商品层级促销活动计算
     *
     * @param orderPojo
     */
    void computePromotPrice(OrderPojo orderPojo);

    /**
     * 分摊促销活动金额
     *
     * @param orderPojo
     */
    void apportionFee(OrderPojo orderPojo);

    /**
     * 获取返回促销返回结果数据
     *
     * @param orderPojo
     */
    MallPromotionResult transToReturnPojo(OrderPojo orderPojo);

}
