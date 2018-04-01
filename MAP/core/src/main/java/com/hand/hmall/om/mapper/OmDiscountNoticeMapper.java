package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OmDiscountNotice;

/**
 * @author zhangmeng
 * @name OmDiscountNoticeMapper
 * @description 折扣行通知
 * @date 2018/02/09
 */
public interface OmDiscountNoticeMapper extends Mapper<OmDiscountNotice> {

    /**
     * 根据折扣行ID删除
     *
     * @param omDiscountNotice
     * @return
     */
    int deleteByDiscountId(OmDiscountNotice omDiscountNotice);
}