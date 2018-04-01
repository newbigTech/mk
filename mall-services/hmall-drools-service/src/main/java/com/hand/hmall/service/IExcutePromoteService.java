package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.Order;
import com.hand.hmall.pojo.OrderEntryPojo;
import com.hand.hmall.pojo.OrderPojo;

import java.util.List;

/**
 * @Describe d
 * @Author noob
 * @Date 2017/6/28 17:09
 */
public interface IExcutePromoteService {
    ResponseData payByActivity(OrderPojo order, String type);

    ResponseData payByActivitySingle(String activityId, OrderPojo order);

    ResponseData optionCouponWithoutActivity(OrderPojo order) throws CloneNotSupportedException, InterruptedException;

    ResponseData useCoupon(OrderPojo order);

    ResponseData payByCoupon(String couponId, OrderPojo order, String currentUserId);

    void orderEntryPromote(OrderPojo orderPojo);

}
