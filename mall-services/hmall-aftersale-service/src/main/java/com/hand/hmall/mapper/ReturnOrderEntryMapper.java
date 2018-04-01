package com.hand.hmall.mapper;

import com.hand.hmall.dto.ReturnOrder;
import com.hand.hmall.dto.ReturnOrderEntry;

import java.util.List;

/**
 * @version 1.0
 * @name ExchangeOrderEntryMapper
 * @Describe 退货单行持久层接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface ReturnOrderEntryMapper {

    /**
     * 查询退货单对应的退货单行信息
     *
     * @param returnOrder - 退货单
     * @return
     */
    List<ReturnOrderEntry> selectByReturnOrder(ReturnOrder returnOrder);

}
