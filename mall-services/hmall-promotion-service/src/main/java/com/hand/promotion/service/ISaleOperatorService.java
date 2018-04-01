package com.hand.promotion.service;


import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.activity.ActivityPojo;
import com.hand.promotion.pojo.coupon.CouponsPojo;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/25
 * @description 促销、优惠券操作人信息Service
 */
public interface ISaleOperatorService {

    /**
     * 根据baseId（关联的促销活动主键）查询操作人信息
     *
     * @param baseId
     * @param page
     * @param pageSize
     * @return
     */
    ResponseData queryByBaseId(String baseId, int page, int pageSize);

    /**
     * 插入促销操作记录
     *
     * @param activityPojo 修改后的促销信息
     * @param changeMessage 改动信息
     * @param userId 修改人
     */
    void insertActivityOp(ActivityPojo activityPojo, String changeMessage, String userId);

    /**
     * 插入促销操作记录
     *
     * @param couponsPojo 修改后的优惠券信息
     * @param changeMessage 改动信息
     * @param userId 修改人
     */
    void insertCouponOp(CouponsPojo couponsPojo, String changeMessage, String userId);
}
