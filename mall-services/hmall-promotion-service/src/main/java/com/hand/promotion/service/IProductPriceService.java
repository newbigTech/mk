package com.hand.promotion.service;

import com.hand.promotion.pojo.order.OrderPojo;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/6
 * @description 促销计算中商品金额查询Service
 */
public interface IProductPriceService {

    /**
     * 获取订单数据的这是价格
     *
     * @param order
     */
    void getRealPrice(OrderPojo order);
}
