package com.hand.hmall.om.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.om.dto.OrderEntryBk;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name OrderEntryBkMapper
 * @description 订单行备份Mapperr
 * @date 2017/8/4 10:43
 */
public interface OrderEntryBkMapper extends Mapper<OrderEntryBk> {

    /**
     * 订单行查询详情
     *
     * @param dto
     * @return
     */
    List<OrderEntryBk> queryInfo(OrderEntryBk dto);

}
