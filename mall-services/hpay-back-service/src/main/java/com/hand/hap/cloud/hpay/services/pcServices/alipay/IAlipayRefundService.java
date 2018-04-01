package com.hand.hap.cloud.hpay.services.pcServices.alipay;

import com.hand.hap.cloud.hpay.data.RefundData;
import com.hand.hap.cloud.hpay.data.ReturnData;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
public interface IAlipayRefundService {

    /**
     * 支付宝退款
     *
     * @param refundData 请求数据
     * @return ReturnData
     */
    ReturnData alipayRefund(RefundData refundData) throws Exception;
}