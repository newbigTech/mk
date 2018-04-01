package com.hand.hmall.services.order.service;

import com.hand.hmall.services.order.dto.PaymentInfo;

/**
 * @author alaowan
 * Created at 2017/12/28 18:45
 * @description
 */
public interface IServiceOrderService {

    /**
     * 服务销售单支付信息接收并保存
     *
     * @param paymentInfo
     * @return
     */
    void saveServOrderPaymentInfo(PaymentInfo paymentInfo);
}
