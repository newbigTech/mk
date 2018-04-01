package com.hand.hap.cloud.hpay.controller;

import com.hand.hap.cloud.hpay.data.RefundData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.services.pcServices.alipay.IAlipayRefundService;
import com.hand.hap.cloud.hpay.services.pcServices.union.service.IUnionRefundService;
import com.hand.hap.cloud.hpay.services.pcServices.wechat.IWeChatRefundService;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import com.hand.hap.cloud.hpay.validateData.ValidateRefundData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.controller
 * @Description 退款控制
 * @date 2017/7/10
 */
@RestController
@RequestMapping(value = "/v1")
public class RefundController {

    @Autowired
    private ValidateRefundData validateRefundData;

    @Autowired
    private IUnionRefundService unionPayRefundOrderService;

    @Autowired
    private IAlipayRefundService alipayRefundService;

    @Autowired
    private IWeChatRefundService weChatpayRefundService;

    /**
     * 订单退款
     *
     * @param refundData 退款数据
     * @return returndata ReturnData
     * @throws Exception exception
     */
    @RequestMapping(value = "/Refund", method = RequestMethod.POST)
    public ReturnData refund(@RequestBody RefundData refundData)
            throws Exception {
        ReturnData rd = validateRefundData.validateRefundData(refundData);
        //验证数据有效性
        if (!rd.isSuccess()) {
            return rd;
        }
        if (PropertiesUtil.getPayInfoValue("mode.unionpay").equals(refundData.getMode())) {
            rd = unionPayRefundOrderService.unionRefund(refundData);
        } else if (PropertiesUtil.getPayInfoValue("mode.alipay").equals(refundData.getMode())) {
            rd = alipayRefundService.alipayRefund(refundData);
        } else {
            rd = weChatpayRefundService.weChatRefund(refundData);
        }
        return rd;
    }
}
