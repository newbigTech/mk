package com.hand.hmall.mapper;

import com.hand.hmall.model.HmallOmPromotionRule;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface HmallOmPromotionRuleMapper extends Mapper<HmallOmPromotionRule> {

    //根据订单Id找到促销信息
    HmallOmPromotionRule selectByOrderId(Long orderId);

    //根据订单行Id找到促销信息
    HmallOmPromotionRule selectByOrderEntryId(Long orderEntryId);

    //根据orderId删除订单原促销信息
    int deleteByOrderId(Long orderId);

    //根据orderEntryId删除订单行原促销信息
    int deleteByOrderEntryId(Long orderEntryId);
}