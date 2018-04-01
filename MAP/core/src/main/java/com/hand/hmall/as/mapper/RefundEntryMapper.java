package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.RefundEntry;

import java.util.List;

/**
 * author: zhangzilong
 * name: RefundEntryMapper.java
 * discription: 退款单行Mapper
 * date: 2017/8/7
 * version: 0.1
 */
public interface RefundEntryMapper extends Mapper<RefundEntry> {

    RefundEntry queryForHPAY(RefundEntry dto);

    void updatePayStatus(RefundEntry dto);

    /**
     * 根据退款单ID获取对应的退款单行信息
     *
     * @param asRefundId
     * @return
     */
    List<RefundEntry> selectRefundOrderEntry(Long asRefundId);

}
