package com.hand.hmall.mapper;

import com.hand.hmall.dto.RefundOrder;
import com.hand.hmall.dto.RefundOrderEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @name ExchangeOrderEntryMapper
 * @Describe 退款单行持久层接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface RefundOrderEntryMapper {

    /**
     * 查询退款单对应的退款单行信息
     * @param refundOrder
     * @return
     */
    List<RefundOrderEntry> selectByRefundOrder(RefundOrder refundOrder);
}
