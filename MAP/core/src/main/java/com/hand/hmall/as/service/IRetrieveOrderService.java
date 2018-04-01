package com.hand.hmall.as.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.RetrieveOrder;

import java.util.List;

/**
 * @author qinzhipeng
 * @version 0.1
 * @name IRetrieveOrderService
 * @description
 * @date
 */
public interface IRetrieveOrderService extends IBaseService<RetrieveOrder>, ProxySelf<IRetrieveOrderService> {

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