package com.hand.hmall.om.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.OrderEntryBk;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name IOrderEntryBkService
 * @description 订单行备份Service接口
 * @date 2017/8/4 11:36
 */
public interface IOrderEntryBkService extends IBaseService<OrderEntryBk> {

    /**
     * 订单行查询详情
     *
     * @param dto
     * @return
     */
    List<OrderEntryBk> queryInfo(IRequest iRequest, OrderEntryBk dto, int page, int pagesize);
}
