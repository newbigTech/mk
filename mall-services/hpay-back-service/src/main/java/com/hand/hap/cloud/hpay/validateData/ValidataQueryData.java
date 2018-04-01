package com.hand.hap.cloud.hpay.validateData;

import com.hand.hap.cloud.hpay.data.QueryData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.validateData
 * @Description
 * @date 2017/7/14
 */
@Service
public class ValidataQueryData {

    /**
     * 验证查询数据
     *
     * @param queryData
     * @return
     */
    public ReturnData validataQueryData(QueryData queryData) {

        ReturnData responseData = new ReturnData();

        //判断paymentTypeCode是否为空
        if (StringUtils.isEmpty(queryData.getPaymentTypeCode())) {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.QueryData.paymentTypeCode.null");
            responseData.setMsg("paymentTypeCode-null");
            return responseData;
        }
        //判断payMentTypeCode是否有效
        if (!PropertiesUtil.getPayInfoValue("paymentTypeCode.pay").equalsIgnoreCase(queryData.getPaymentTypeCode())
                && !PropertiesUtil.getPayInfoValue("paymentTypecode.refund").equalsIgnoreCase(queryData.getPaymentTypeCode())) {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.QueryData.paymentTypeCode.fail");
            responseData.setMsg("paymentTypeCode-fail");
            return responseData;
        }
        if (PropertiesUtil.getPayInfoValue("mode.alipay").equalsIgnoreCase(queryData.getMode())) {
            //alipay-pay
            if (PropertiesUtil.getPayInfoValue("paymentTypeCode.pay").equalsIgnoreCase(queryData.getPaymentTypeCode())) {
                if (StringUtils.isEmpty(queryData.getOutTradeNo())) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode("data.QueryData.outTradeNo.null");
                    responseData.setMsg("outTradeNo-null");
                    return responseData;
                }
                //alipay-refund
            } else {
                if (StringUtils.isEmpty(queryData.getTradeNo())) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode("data.QueryData.tradeNo.null");
                    responseData.setMsg("tradeNo-null");
                    return responseData;
                }
                if (StringUtils.isEmpty(queryData.getOutRefundNo())) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode("data.QueryData.outRefundNo.null");
                    responseData.setMsg("outRefundNo-null");
                    return responseData;
                }
            }
        } else if (PropertiesUtil.getPayInfoValue("mode.wechatpay").equalsIgnoreCase(queryData.getMode())) {
            //wechat-pay
            if (PropertiesUtil.getPayInfoValue("paymentTypeCode.pay").equalsIgnoreCase(queryData.getPaymentTypeCode())) {
                if (StringUtils.isEmpty(queryData.getOutTradeNo())) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode("data.QueryData.outTradeNo.null");
                    responseData.setMsg("outTradeNo-null");
                    return responseData;
                }
                //wechat-pay
            } else {
                if (StringUtils.isEmpty(queryData.getOutRefundNo())) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode("data.QueryData.outRefundNo.null");
                    responseData.setMsg("outRefundNo-null");
                    return responseData;
                }
            }
        } else if (PropertiesUtil.getPayInfoValue("mode.unionpay").equalsIgnoreCase(queryData.getMode())) {
            //union 订单时间不能为空
            if (StringUtils.isEmpty(queryData.getTxnTime())) {
                responseData.setSuccess(false);
                responseData.setMsgCode("data.QueryData.txnTime.null");
                responseData.setMsg("txnTime-null");
                return responseData;
            }
            //union-pay
            if (PropertiesUtil.getPayInfoValue("paymentTypeCode.pay").equalsIgnoreCase(queryData.getPaymentTypeCode())) {
                if (StringUtils.isEmpty(queryData.getOutTradeNo())) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode("data.QueryData.outTradeNo.null");
                    responseData.setMsg("outTradeNo-null");
                    return responseData;
                }
                //union-refund
            } else {
                if (StringUtils.isEmpty(queryData.getOutRefundNo())) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode("data.QueryData.outRefundNo.null");
                    responseData.setMsg("outRefundNo-null");
                    return responseData;
                }
            }
        } else {
            responseData.setSuccess(false);
            responseData.setMsgCode("data.QueryData.mode.fail");
            responseData.setMsg("mode-fail");
            return responseData;
        }
        responseData.setSuccess(true);
        return responseData;
    }
}
