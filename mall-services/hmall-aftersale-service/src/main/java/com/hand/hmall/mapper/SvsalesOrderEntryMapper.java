package com.hand.hmall.mapper;

import com.hand.hmall.dto.SvsalesOrder;
import com.hand.hmall.dto.SvsalesOrderEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @name ExchangeOrderEntryMapper
 * @Describe 服务销售单行持久层接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
public interface SvsalesOrderEntryMapper {

    /**
     * 查询售后服务单对应的售后服务单行信息
     *
     * @param svsalesOrder
     * @return
     */
    List<SvsalesOrderEntry> selectBySvsalesOrder(SvsalesOrder svsalesOrder);
}
