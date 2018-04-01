package com.hand.hmall.om.service;

import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hmall.om.dto.Paymentinfo;

import java.util.List;

/**
 * @author peng.chen
 * @version 0.1
 * @name IPaymentinfoService
 * @description
 * @date 2017年5月25日18:53:32
 */
public interface IPaymentinfoService extends IBaseService<Paymentinfo>, ProxySelf<IPaymentinfoService> {

    /**
     * 手工对账界面查询
     *
     * @return
     */
    List<Paymentinfo> getInfoForBalance(IRequest request, Paymentinfo dto, int page, int pageSize);

    /**
     * 获取payMode=UNION settleTime=null的数据
     *
     * @return
     */
    List<Paymentinfo> selectPaymentsByModeWithSettleTimeIsNull(Paymentinfo dto);

    /**
     * 根据订单ID查询所有相关的支付信息（售后退款单界面）
     *
     * @param orderId
     * @return
     */
    List<Paymentinfo> getPaymentinfoByOrderId(Long orderId);

    /**
     * 将支付信息同步到retail
     *
     * @param paymentinfos
     */
    void postToRetail(List<Paymentinfo> paymentinfos) throws WSCallException;
}