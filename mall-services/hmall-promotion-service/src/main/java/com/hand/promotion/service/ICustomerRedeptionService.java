package com.hand.promotion.service;

import com.hand.promotion.pojo.coupon.CustomerRedeptionPojo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/12
 * @description 客户单张优惠券还能兑换的次数Service
 */
public interface ICustomerRedeptionService {

    /**
     * 根据优惠券主键,用户账号查询用户对该优惠券的最大兑换数
     *
     * @param cid        优惠券主键
     * @param customerId 发放账号
     * @return
     */
    List<CustomerRedeptionPojo> queryByCouponIdAndUid(String cid, String customerId) ;

    void insert(CustomerRedeptionPojo customerRedeptionPojo);

    /**
     * 兑换记录减去兑换数量
     */
    void subCount(CustomerRedeptionPojo customerRedeptionPojo, int count) ;

}
