package com.hand.promotion.service.impl;

import com.hand.promotion.dao.CouponRedeptionDao;
import com.hand.promotion.pojo.coupon.CouponRedemptionPojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.service.ICouponRedeptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/11
 * @description 优惠券最大兑换量Service
 */
@Service
public class CouponRedeptionServiceImpl implements ICouponRedeptionService {

    @Autowired
    private CouponRedeptionDao couponRedeptionDao;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 优惠券最大兑换量Service
     *
     * @param couponsPojo 要记录信息的优惠券描述
     * @return
     */
    @Override
    public void insertFromCoupon(CouponsPojo couponsPojo) {
        CouponRedemptionPojo couponRedemptionPojo = new CouponRedemptionPojo();
        couponRedemptionPojo.setNumber(couponsPojo.getMaxRedemption());
        couponRedemptionPojo.setRedemptionId(couponsPojo.getId());
        couponRedeptionDao.insertPojo(couponRedemptionPojo);
    }

    /**
     * 根据redemptionId优惠券主键查询优惠券剩余兑换量.
     *
     * @param redemptionId 优惠券主键
     * @return
     */
    @Override
    public CouponRedemptionPojo queryByCid(String redemptionId) {
        CouponRedemptionPojo condition = new CouponRedemptionPojo();
        condition.setRedemptionId(redemptionId);
        List<CouponRedemptionPojo> byPojo = couponRedeptionDao.findByPojo(condition);
        if (CollectionUtils.isEmpty(byPojo) || byPojo.size() != 1) {
            return null;
        } else {
            return byPojo.get(0);
        }
    }

    /**
     * 优惠券最大兑换数记录减去count
     *
     * @param couponRedemptionPojo
     * @param count
     */
    @Override
    public void subCount(CouponRedemptionPojo couponRedemptionPojo, int count)  {
        couponRedemptionPojo.setNumber(couponRedemptionPojo.getNumber() - count);
        couponRedeptionDao.updatePojoByPK("id", couponRedemptionPojo.getId(), couponRedemptionPojo);
    }
}
