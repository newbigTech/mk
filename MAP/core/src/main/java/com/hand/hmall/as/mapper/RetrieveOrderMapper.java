package com.hand.hmall.as.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hmall.as.dto.RetrieveOrder;

import java.util.List;

/**
 * @author qinzhipeng
 * @version 0.1
 * @name RetrieveOrderMapper
 * @description 
 * @date
 */
public interface RetrieveOrderMapper extends Mapper<RetrieveOrder> {

    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return
     */
    List<RetrieveOrder> selectUserInfoByOrderId(RetrieveOrder dto);

    /**
     * 查询取回单详细信息
     *
     * @param dto
     * @return
     */
    List<RetrieveOrder> selectRetrieveOrderById(RetrieveOrder dto);
}