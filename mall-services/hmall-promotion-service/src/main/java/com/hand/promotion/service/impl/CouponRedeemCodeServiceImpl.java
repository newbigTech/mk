package com.hand.promotion.service.impl;

import com.hand.promotion.dao.CouponRedeemCodeDao;
import com.hand.promotion.pojo.coupon.CouponRedeemCodePojo;
import com.hand.promotion.pojo.enums.PromotionConstants;
import com.hand.promotion.service.ICouponRedeemCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/1/12
 * @description
 */
@Service
public class CouponRedeemCodeServiceImpl implements ICouponRedeemCodeService {

    @Autowired
    private CouponRedeemCodeDao couponRedeemCodeDao;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据兑换码查询兑换码详细信息
     *
     * @param redeemCode
     * @return
     */
    @Override
    public CouponRedeemCodePojo queryByCode(String redeemCode)  {
        CouponRedeemCodePojo condition = new CouponRedeemCodePojo();
        condition.setCode(redeemCode);
        List<CouponRedeemCodePojo> byPojo = couponRedeemCodeDao.findByPojo(condition);
        if (CollectionUtils.isEmpty(byPojo)) {
            return null;
        } else if (byPojo.size() != 1) {
            return null;
        } else {
            return byPojo.get(0);
        }
    }

    /**
     * 根据优惠券编码查询优惠券兑换码
     *
     * @param couponCode 优惠券编码
     * @return
     */
    @Override
    public List<CouponRedeemCodePojo> quertyByCouponCode(String couponCode)  {
        CouponRedeemCodePojo condition = new CouponRedeemCodePojo();
        condition.setCouponCode(couponCode);
        List<CouponRedeemCodePojo> byPojo = couponRedeemCodeDao.findByPojo(condition);
        if (CollectionUtils.isEmpty(byPojo)) {
            return Collections.EMPTY_LIST;
        }
        return byPojo;
    }

    /**
     * 根据优惠券编码查询可用的优惠券兑换码
     *
     * @param couponCode 优惠券编码
     * @return
     */
    @Override
    public List<CouponRedeemCodePojo> quertyUsefulByCouponCode(String couponCode) {
        CouponRedeemCodePojo condition = new CouponRedeemCodePojo();
        condition.setCouponCode(couponCode);
        condition.setUsed(PromotionConstants.Y);
        List<CouponRedeemCodePojo> byPojo = couponRedeemCodeDao.findByPojo(condition);
        if (CollectionUtils.isEmpty(byPojo)) {
            return Collections.EMPTY_LIST;
        }
        return byPojo;
    }

    /**
     * 插入兑换码
     *
     * @param couponRedeemCodePojo
     */
    @Override
    public void insert(CouponRedeemCodePojo couponRedeemCodePojo) {
        couponRedeemCodeDao.insertPojo(couponRedeemCodePojo);
    }

    /**
     * 批量插入兑换码
     *
     * @param couponRedeemCodePojos
     */
    @Override
    public void batchInsert(List<CouponRedeemCodePojo> couponRedeemCodePojos) {
        couponRedeemCodeDao.insertPojos(couponRedeemCodePojos);
    }

    /**
     * 占用优惠券兑换码
     *
     * @param id 兑换码主键
     */
    @Override
    public void occupyRedeemCode(String id) {
        couponRedeemCodeDao.updateUsedByPK(id, PromotionConstants.Y);
    }
}
