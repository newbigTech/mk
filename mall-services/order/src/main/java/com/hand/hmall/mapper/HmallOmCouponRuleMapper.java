/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallOmCouponRule;
import tk.mybatis.mapper.common.Mapper;

public interface HmallOmCouponRuleMapper extends Mapper<HmallOmCouponRule> {

    //根据orderId删除订单原优惠券信息
    int deleteByOrderId(Long orderId);
}