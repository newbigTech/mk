package com.hand.hap.cloud.hpay.services.pcServices.alipay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hand.hap.cloud.hpay.data.RefundData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.pcServices.alipay.IAlipayRefundService;
import com.hand.hap.cloud.hpay.utils.AmonutUtils;
import com.hand.hap.cloud.hpay.utils.DateFormatUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */

@Service
public class AlipayRefundServiceImpl implements IAlipayRefundService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MessageSender messageSender;

    @Override
    public ReturnData alipayRefund(RefundData refundData) throws Exception {

        //交易创建时间 txnTime
        String gmtCreate = DateFormatUtil.getCurrentTime();

        ReturnData responseData = new ReturnData();

        String amount = AmonutUtils.changeF2Y(refundData.getAmount());

        List<Map<String, String>> reseponseRefundList = new ArrayList<>();

        AlipayClient alipayClient = new DefaultAlipayClient(
                PropertiesUtil.getHpayValue("alipay.gatewayUrl"),
                PropertiesUtil.getHpayValue("alipay.appId"),
                PropertiesUtil.getHpayValue("alipay.merchantPrivatekey"),
                PropertiesUtil.getHpayValue("alipay.format"),
                PropertiesUtil.getHpayValue("alipay.charset"),
                PropertiesUtil.getHpayValue("alipay.publicKey"),
                PropertiesUtil.getHpayValue("alipay.signType"));
        AlipayTradeRefundRequest alipayTradeRefundRequest = new AlipayTradeRefundRequest();
        //商户订单号，商户网站订单系统中唯一订单号
        alipayTradeRefundRequest.setBizContent("{" +
                "\"trade_no\":\"" + refundData.getTradeNo() + "\"," +
                "\"refund_amount\":\"" + amount + "\"," +
                "\"refund_reason\":\"" + refundData.getRefundReason() + "\"," +
                "\"out_request_no\":\"" + refundData.getOutRefundNo() + "\"" +
                "}");
        //开始记录日志
        ThirdPartyApiLogs log = new ThirdPartyApiLogs();
        //业务数据
        log.setRequestBody(alipayTradeRefundRequest.getBizContent());
        //请求时间
        Date requestTime = new Date();
        log.setRequestTime(requestTime);
        //请求目标
        log.setTarget(PropertiesUtil.getPayInfoValue("mode.alipay"));
        //请求方法
        log.setRequestMethod(PropertiesUtil.getPayInfoValue("paymentTypecode.refund"));
        //请求源
        log.setClientType(PropertiesUtil.getPayInfoValue("mode.tradeType.pc"));
        AlipayTradeRefundResponse response;
        try {
            response = alipayClient.execute(alipayTradeRefundRequest);
            Date responseTime = new Date();
            log.setResponseTime(responseTime);
            log.setDuring(responseTime.getTime() - requestTime.getTime());
            if (response.isSuccess()) {

                HashMap<String, String> reqMap = new HashMap();

                reqMap.put("mode", PropertiesUtil.getPayInfoValue("mode.alipay"));
                reqMap.put("tradeResultCode", response.getCode());
                reqMap.put("tradeDescription", response.getMsg());
                reqMap.put("tradeStatus", "REFUND_SUCCESS");
                reqMap.put("notifyId", response.getTradeNo());
                reqMap.put("tradeCreateTime", gmtCreate);
                reqMap.put("tradePayTime", DateFormatUtil.format(response.getGmtRefundPay(), "yyyyMMddHHmmss"));
                //订单总金额
                reqMap.put("totalAmount", AmonutUtils.changeY2F(response.getRefundFee()));
                //退款总金额
                reqMap.put("buyerPayAmount", AmonutUtils.changeY2F(response.getRefundFee()));
                reqMap.put("tradeNo", response.getTradeNo());
                reqMap.put("outTradeNo", response.getOutTradeNo());

                //记录日志
                log.setResponseBody(reqMap);
                log.setSuccess(true);
                messageSender.sendMsg(log);
                logger.info("支付宝退款成功-", reqMap);
                responseData.setSuccess(true);
                responseData.setMsgCode(response.getCode());
                responseData.setMsg(response.getMsg());
                reseponseRefundList.add(reqMap);
                responseData.setResp(reseponseRefundList);
            } else {
                logger.error("支付宝退款失败-" + response.getCode() + "-" + response.getMsg());
                //日志
                log.setSuccess(false);
                log.setRespMsgCode(response.getCode());
                log.setRespMsg(response.getMsg() + "-" + response.getSubCode() + "-" + response.getSubMsg());
                messageSender.sendMsg(log);
                responseData.setSuccess(false);
                responseData.setMsgCode(response.getCode() + "-" + response.getMsg() + "-" + response.getSubCode() + "-" + response.getSubMsg());
                responseData.setMsg(response.getMsg());
            }
        } catch (AlipayApiException e) {
            //日志
            log.setSuccess(false);
            log.setExceptionCode(e.getErrCode());
            log.setExceptionMsg(e.getErrMsg());
            log.setExceptionStack(e.getStackTrace());
            messageSender.sendMsg(log);
            logger.error("alipayClient.execute-fail", e);
        }
        return responseData;
    }
}