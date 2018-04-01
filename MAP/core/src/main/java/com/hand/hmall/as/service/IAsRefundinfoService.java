package com.hand.hmall.as.service;

import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.as.dto.AsRefundinfo;

import java.util.List;

public interface IAsRefundinfoService extends IBaseService<AsRefundinfo>, ProxySelf<IAsRefundinfoService>{

    /**
     * 手工上载财务弹窗查询
     * @return
     */
    List<AsRefundinfo> getInfoForBalance(IRequest request, AsRefundinfo dto, int page, int pageSize);

    /**
     * 将退款单信息推送到Zmall
     *
     * @param refundinfoList
     * @throws WSCallException
     */
    void sendInfoToRetail(List<AsRefundinfo> refundinfoList) throws WSCallException;

    /**
     * 查询符合推送条件的退款信息明细
     *
     * @param refundinfo
     * @return
     */
    List<AsRefundinfo> getSuitSyncInfo(AsRefundinfo refundinfo);
}