package com.hand.hmall.as.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.Dispatchorder;
import com.hand.hmall.as.dto.ServiceorderEntry;

import java.util.List;

public interface IDispatchorderService extends IBaseService<Dispatchorder>, ProxySelf<IDispatchorderService> {

    /**
     * @param dispatchorder
     * @description 派工单界面保存按钮保存派工单信息
     */
    public List<Dispatchorder> saveDispatchOrederInfo(IRequest iRequest, Dispatchorder dispatchorder);

    /**
     * 根据退款单ID查询对应的退款单行
     * @param requestContext
     * @param serviceOrderId
     * @param page
     * @param pageSize
     * @return
     */
    List<ServiceorderEntry> selectDispatchOrderEntry(IRequest requestContext, Long serviceOrderId, int page, int pageSize);
}