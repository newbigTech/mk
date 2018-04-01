package com.hand.promotion.service;



import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.order.OrderPojo;

import java.util.concurrent.ExecutionException;

/**
 * @Describe 订单执行促销活动业务逻辑控制流
 * @Author xinyang.Mei
 * @Date 2017/6/28 16:55
 */
public interface IOrderPromoteService {

    /**
     * 执行促销活动
     *
     * @param order
     * @return
     * @throws CloneNotSupportedException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    ResponseData promote(OrderPojo order) throws CloneNotSupportedException, ExecutionException, InterruptedException;

    /**
     * 执行霸王券逻辑
     *
     * @param orderPojo
     * @return
     */
    ResponseData promoteForExclusiveCoupon(OrderPojo orderPojo);


    /**
     * 执行用户选择的促销、优惠券
     *
     * @param orderPojo
     * @return
     */
    ResponseData executeChosenCouponPromote(OrderPojo orderPojo);
}
