package com.hand.hmall.as.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.RetrieveOrder;
import com.hand.hmall.as.dto.ReturnOrder;

import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name IReturnOrderService 退货单Service接口
 * @description
 * @date 2017/7/20
 */
public interface IReturnOrderService extends IBaseService<ReturnOrder>, ProxySelf<IReturnOrderService> {

    /**
     * 根据退货单id查询对应的退货单
     *
     * @param dto
     * @return List<ReturnOrder>
     */
    List<ReturnOrder> selectReturnOrderById(ReturnOrder dto);

    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return List<ReturnOrder>
     */
    List<ReturnOrder> selectUserInfoByOrderId(RetrieveOrder dto);
}
