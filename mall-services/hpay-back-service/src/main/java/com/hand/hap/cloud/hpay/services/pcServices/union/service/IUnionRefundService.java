package com.hand.hap.cloud.hpay.services.pcServices.union.service;


import com.hand.hap.cloud.hpay.data.RefundData;
import com.hand.hap.cloud.hpay.data.ReturnData;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
public interface IUnionRefundService {

    /**
     * 请求银联退款
     *
     * @param refundData 退款信息
     * @return ReturnData
     */
    ReturnData unionRefund(RefundData refundData);
}
