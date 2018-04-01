package com.hand.hap.cloud.hpay.services.pcServices.wechat;

import com.hand.hap.cloud.hpay.data.RefundData;
import com.hand.hap.cloud.hpay.data.ReturnData;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
public interface IWeChatRefundService {

    /**
     * 微信退款
     *
     * @param refundData 退款数据
     * @return returnData
     * @throws Exception exception
     */
    ReturnData weChatRefund(RefundData refundData) throws Exception;
}
