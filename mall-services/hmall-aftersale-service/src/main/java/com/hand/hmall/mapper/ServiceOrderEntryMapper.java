package com.hand.hmall.mapper;

import com.hand.hmall.dto.ServiceOrder;
import com.hand.hmall.dto.ServiceOrderEntry;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @name ExchangeOrderEntryMapper
 * @Describe 服务单行持久层接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface ServiceOrderEntryMapper extends Mapper<ServiceOrderEntry> {

    /**
     * 根据服务单查询服务单行列表
     *
     * @param so
     * @return
     */
    List<ServiceOrderEntry> selectByServiceOrder(ServiceOrder so);

    /**
     * 查询订单行下一个PIN码信息
     * @return
     */
    Long nextLineNumber();
}
