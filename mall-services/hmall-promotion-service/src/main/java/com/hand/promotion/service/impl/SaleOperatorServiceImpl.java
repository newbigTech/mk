package com.hand.promotion.service.impl;

import com.hand.dto.ResponseData;
import com.hand.promotion.dao.SaleOperatorDao;
import com.hand.promotion.pojo.SaleOperatorPojo;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;
import com.hand.promotion.service.ISaleOperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/25
 * @description 促销、优惠券操作人信息Service
 */
@Service
public class SaleOperatorServiceImpl implements ISaleOperatorService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SaleOperatorDao saleOperatorDao;

    @Override
    public ResponseData queryByBaseId(String baseId, int page, int pageSize) {
        return new ResponseData(saleOperatorDao.queryByBaseId(baseId, page, pageSize));
    }

    /**
     * 插入促销操作记录
     *
     * @param activityPojo 修改后促销活动信息
     * @param changeMsg    改动前促销描述信息
     * @param userId       操作人
     */
    @Override
    public void insertActivityOp(ActivityPojo activityPojo, String changeMsg, String userId) {
        SaleOperatorPojo operatorPojo = new SaleOperatorPojo();
        operatorPojo.setOperator(userId);
        operatorPojo.setBaseId(activityPojo.getActivityId());
        operatorPojo.setOperation(changeMsg+"--->"+activityPojo.getActivityDes());
        operatorPojo.setChangeDate(activityPojo.getCreationTime());
        operatorPojo.setType("ACTIVITY");
        saleOperatorDao.insertPojo(operatorPojo);
    }

    /**
     * @param couponsPojo   修改后优惠券信息
     * @param changeMessage 改动前优惠券描述信息
     * @param userId
     */
    @Override
    public void insertCouponOp(CouponsPojo couponsPojo, String changeMessage, String userId) {
        SaleOperatorPojo operatorPojo = new SaleOperatorPojo();
        operatorPojo.setOperator(userId);
        operatorPojo.setBaseId(couponsPojo.getCouponId());
        operatorPojo.setOperation(changeMessage+"--->"+couponsPojo.getCouponDes());
        operatorPojo.setChangeDate(couponsPojo.getCreationTime());
        operatorPojo.setType("COUPON");
        saleOperatorDao.insertPojo(operatorPojo);

    }

}
