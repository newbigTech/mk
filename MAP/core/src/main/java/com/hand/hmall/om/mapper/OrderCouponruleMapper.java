package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OrderCouponrule;
import com.hand.hmall.om.dto.OrderCouponruleDto;

import java.util.List;

public interface OrderCouponruleMapper extends Mapper<OrderCouponrule> {

    /**
     * 根据订单ID查询对应的优惠劵信息
     *
     * @param orderId
     * @return
     */
    List<OrderCouponruleDto> selectOrderSyncByOrderId(Long orderId);

    /**
     * 根据订单ID更新优惠券信息
     *
     * @param orderCouponrule
     * @return
     */
    int updateOrderCouponruleByOrderId(OrderCouponrule orderCouponrule);

    /**
     * 根据订单ID查询优惠券信息
     *
     * @param orderCouponrule
     * @return
     */
    List<OrderCouponrule> selectOrderCouponruleByOrderId(OrderCouponrule orderCouponrule);

    /**
     * 根据订单ID删除优惠券信息
     *
     * @param orderCouponrule
     * @return
     */
    int deleteOrderCouponruleByOrderId(OrderCouponrule orderCouponrule);

}