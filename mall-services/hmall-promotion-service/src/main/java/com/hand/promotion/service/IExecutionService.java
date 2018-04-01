package com.hand.promotion.service;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.order.OrderPojo;

import java.util.Map;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/5
 * @description 商城, 中台促销执行, 重算接口. 查询商品关联的促销活动
 */
public interface IExecutionService {

    /**
     * 商城\中台调用促销执行接口
     *
     * @param orderPojo 进行促销计算的订单数据
     * @return
     */
    ResponseData promoteOrder(OrderPojo orderPojo);

    /**
     * 商城调用查询商品可用促销
     *
     * @param productMap 商品信息
     * @return
     */
    ResponseData getActivity(Map productMap);

}

