package com.hand.hmall.mapper;

import com.hand.hmall.dto.RefundOrder;
import com.hand.hmall.dto.ServiceOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @name ExchangeOrderEntryMapper
 * @Describe 退款单持久层接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface RefundOrderMapper {

    /**
     * 查询服务单对应的退款单，并设置到服务单中
     *
     * @param so
     * @return
     */
    List<RefundOrder> selectByServiceOrder(ServiceOrder so);

    /**
     * 事后促销使用
     * @return
     */
    List<RefundOrder> selectByCondition(@Param("orderId") Long orderId);
}
