package com.hand.hmall.mapper;

import com.hand.hmall.dto.ServiceOrder;
import com.hand.hmall.dto.SvsalesOrder;

import java.util.List;

/**
 * @version 1.0
 * @name ExchangeOrderEntryMapper
 * @Describe 服务销售单持久层接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface SvsalesOrderMapper {

    /**
     * 查询服务单对应的服务销售单，并设置到服务单中
     *
     * @param so
     * @return
     */
    List<SvsalesOrder> selectByServiceOrder(ServiceOrder so);

}
