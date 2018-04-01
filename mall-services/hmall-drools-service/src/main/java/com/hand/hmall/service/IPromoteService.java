package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.pojo.OrderPojo;

import java.util.concurrent.ExecutionException;

/**
 * @Describe d
 * @Author noob
 * @Date 2017/6/28 16:55
 */
public interface IPromoteService {
    ResponseData promote(OrderPojo order) throws CloneNotSupportedException, ExecutionException, InterruptedException;

    public ResponseData getUsefulPromoteAndCoupon(OrderPojo order) throws CloneNotSupportedException;

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
