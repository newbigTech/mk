package com.hand.hap.cloud.hpay.validateData;

import com.hand.hap.cloud.hpay.data.RefundData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.validateData
 * @Description
 * @date 2017/7/18
 */
@Service
public class ValidateRefundData {
    public ReturnData validateRefundData(RefundData refundData) {

        ReturnData responseData = new ReturnData();

        //判断mode
        if (StringUtils.isEmpty(refundData.getMode())) {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.RefundData.mode.null-mode-null");
            responseData.setMsg("mode-null");
            return responseData;
        }

        if (!PropertiesUtil.getPayInfoValue("mode.unionpay").equals(refundData.getMode()) &&
                !PropertiesUtil.getPayInfoValue("mode.alipay").equals(refundData.getMode()) &&
                !PropertiesUtil.getPayInfoValue("mode.wechatpay").equals(refundData.getMode())) {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.RefundData.mode.fail-mode-fail");
            responseData.setMsg("mode-fail");
            return responseData;
        }

        //判断aymentTypeCode notNull && refund
        if (StringUtils.isEmpty(refundData.getPaymentTypeCode())) {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.RefundData.paymentTypeCode.null-paymentTypeCode-null");
            responseData.setMsg("paymentTypeCode-null");
            return responseData;
        }

        if (!PropertiesUtil.getPayInfoValue("paymentTypecode.refund").equals(refundData.getPaymentTypeCode())) {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.RefundData.paymentTypeCode.fail-paymentTypeCode-fail");
            responseData.setMsg("paymentTypeCode-fail");
            return responseData;
        }

        //outRefundNo notNull
        if (StringUtils.isEmpty(refundData.getOutRefundNo())) {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.OrderData.outRefundNo.null-outRefundNo-null");
            responseData.setMsg("outRefundNo-null");
            return responseData;
        }

        //tradeNo notNull
        if (StringUtils.isEmpty(refundData.getTradeNo())) {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.OrderData.tradeNo.null-tradeNo-null");
            responseData.setMsg("tradeNo-null");
            return responseData;
        }

        //amount notNull
        if (StringUtils.isEmpty(refundData.getAmount())) {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.OrderData.amount.null-amount-null");
            responseData.setMsg("amount-null");
            return responseData;
        }

        //当mode为alipay时 refundReason notNull
        if (PropertiesUtil.getPayInfoValue("mode.alipay").equals(refundData.getMode())) {
            if (StringUtils.isEmpty(refundData.getRefundReason())) {
                responseData.setSuccess(false);
                responseData.setMsgCode("data.RefundData.refundReason.null-refundReason-null");
                responseData.setMsg("refundReason-null");
                return responseData;
            }
        }

        //当mode为wechatpay时,订单金额不能为空
        if (PropertiesUtil.getPayInfoValue("mode.wechatpay").equals(refundData.getMode())) {
            if (StringUtils.isEmpty(refundData.getTotalAmount())) {
                responseData.setSuccess(false);
                responseData.setMsgCode("data.RefundData.totalAmount.null-totalAmount-null");
                responseData.setMsg("totalAmount-null");
                return responseData;
            }
        }
        responseData.setSuccess(true);
        return responseData;
    }
}
