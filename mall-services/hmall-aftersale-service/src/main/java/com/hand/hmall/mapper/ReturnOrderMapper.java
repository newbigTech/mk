package com.hand.hmall.mapper;

import com.hand.hmall.dto.ReturnOrder;
import com.hand.hmall.dto.ServiceOrder;

import java.util.List;

/**
 * @version 1.0
 * @name ExchangeOrderEntryMapper
 * @Describe 退货单持久层接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface ReturnOrderMapper {

    /**
     * 查询服务单对应的退货单，并设置到服务单中
     *
     * @param so
     * @return
     */
    List<ReturnOrder> selectByServiceOrder(ServiceOrder so);

}
