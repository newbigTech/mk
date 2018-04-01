package com.hand.promotion.service;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.order.OrderPojo;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/6
 * @description 快递物流运费计算Service
 */
public interface ICalShippingFeeService {
    /**
     * 计算快递与物流的运费
     *
     * @param order
     * @return
     */
    ResponseData caculateFreight(OrderPojo order);
}
