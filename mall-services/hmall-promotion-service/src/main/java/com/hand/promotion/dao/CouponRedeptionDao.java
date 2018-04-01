package com.hand.promotion.dao;


import com.hand.promotion.pojo.coupon.CouponRedemptionPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/8
 * @description 优惠券最大兑换次数DAO
 */
@Repository
public class CouponRedeptionDao extends BaseMongoDao<CouponRedemptionPojo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

}
