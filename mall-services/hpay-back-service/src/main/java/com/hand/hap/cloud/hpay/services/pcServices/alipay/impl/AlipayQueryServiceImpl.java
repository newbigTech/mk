package com.hand.hap.cloud.hpay.services.pcServices.alipay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.hand.hap.cloud.hpay.data.QueryData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.pcServices.alipay.IAlipayQueryService;
import com.hand.hap.cloud.hpay.utils.AmonutUtils;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.hand.hap.cloud.hpay.services.constants.Constants.*;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
@Service
public class AlipayQueryServiceImpl implements IAlipayQueryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MessageSender messageSender;

    /**
     * 支付查询
     *
     * @param queryData 请求数据
     * @return returndata
     */
    @Override
    public ReturnData alipayPayQuery(QueryData queryData) {

        ReturnData respData = new ReturnData();

        AlipayClient alipayClient = new DefaultAlipayClient(
                PropertiesUtil.getHpayValue("alipay.gatewayUrl"),
                PropertiesUtil.getHpayValue("alipay.appId"),
                PropertiesUtil.getHpayValue("alipay.merchantPrivatekey"),
                PropertiesUtil.getHpayValue("alipay.format"),
                PropertiesUtil.getHpayValue("alipay.charset"),
                PropertiesUtil.getHpayValue("alipay.publicKey"),
                PropertiesUtil.getHpayValue("alipay.signType"));
        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        alipayTradeQueryRequest.setBizContent("{" +
                "\"out_trade_no\":\"" + queryData.getOutTradeNo() + "\"" +
                "}");
        //开始记录日志
        ThirdPartyApiLogs log = new ThirdPartyApiLogs();
        //业务数据
        log.setRequestBody(alipayTradeQueryRequest.getBizContent());
        //请求时间
        Date requestTime = new Date();
        log.setRequestTime(requestTime);
        //请求目标
        log.setTarget(PropertiesUtil.getPayInfoValue("mode.alipay"));
        //请求方法
        log.setRequestMethod(PropertiesUtil.getPayInfoValue("pamentType.query"));
        //请求源
        log.setClientType(PropertiesUtil.getPayInfoValue("mode.tradeType.pc"));
        AlipayTradeQueryResponse alipayTradeQueryResponse;
        try {
            alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
            Date responseTime = new Date();
            log.setResponseTime(responseTime);
            log.setDuring(responseTime.getTime() - requestTime.getTime());
        } catch (AlipayApiException e) {
            logger.error("AlipayApiException", e);
            respData.setSuccess(false);
            respData.setMsgCode(e.getErrCode());
            respData.setMsg(e.getErrMsg());
            log.setSuccess(false);
            log.setExceptionCode(e.getErrCode());
            log.setExceptionMsg(e.getErrMsg());
            log.setExceptionStack(e.getStackTrace());
            messageSender.sendMsg(log);
            return respData;
        }

        List<Map<String, Object>> respQueryList = new ArrayList<>();

        Map<String, Object> respQueryMap = new HashMap<>();

        if ("Success".equals(alipayTradeQueryResponse.getMsg())) {

            respQueryMap.put(PARAM_MODE, PropertiesUtil.getPayInfoValue("mode.alipay"));
            respQueryMap.put(PARAM_TRADE_TYPE, queryData.getTradeType());
            respQueryMap.put(PARAM_PAYMENT_TYPE_CODE, queryData.getPaymentTypeCode());
            respQueryMap.put(PARAM_TRADE_RESULT_CODE, alipayTradeQueryResponse.getCode());
            respQueryMap.put(PARAM_TRADE_DESCRIPTION, alipayTradeQueryResponse.getMsg());
            respQueryMap.put(PARAM_TRADE_STATUS, alipayTradeQueryResponse.getTradeStatus());
            respQueryMap.put(PARAM_TRADE_NO, alipayTradeQueryResponse.getTradeNo());
            respQueryMap.put(PARAM_OUT_TRADE_NO, alipayTradeQueryResponse.getOutTradeNo());
            respQueryMap.put(PARAM_TOTAL_AMOUNT, AmonutUtils.changeY2F(alipayTradeQueryResponse.getTotalAmount()));
            respQueryMap.put(PARAM_RECEIP_AMOUNT, AmonutUtils.changeY2F(alipayTradeQueryResponse.getReceiptAmount()));
            //记录日志
            log.setResponseBody(respQueryMap);
            log.setSuccess(true);
            messageSender.sendMsg(log);
            respQueryList.add(respQueryMap);

            respData.setSuccess(true);
            respData.setMsgCode(alipayTradeQueryResponse.getCode());
            respData.setMsg(alipayTradeQueryResponse.getMsg());
            respData.setResp(respQueryList);
            return respData;
        } else {
            respData.setSuccess(false);
            log.setSuccess(false);
            log.setRespMsgCode(alipayTradeQueryResponse.getCode());
            log.setRespMsg(alipayTradeQueryResponse.getMsg() + "-" + alipayTradeQueryResponse.getSubCode() + "-" + alipayTradeQueryResponse.getSubMsg());
            messageSender.sendMsg(log);
            respData.setMsgCode(alipayTradeQueryResponse.getCode());
            respData.setMsg(alipayTradeQueryResponse.getMsg() + "-" + alipayTradeQueryResponse.getSubCode() + "-" + alipayTradeQueryResponse.getSubMsg());
            return respData;
        }
    }


    /**
     * 退款查询
     *
     * @param queryData 查询数据
     * @return returndata
     */
    @Override
    public ReturnData alipayRefundQuery(QueryData queryData) {

        ReturnData respData = new ReturnData();

        AlipayClient alipayClient = new DefaultAlipayClient(
                PropertiesUtil.getHpayValue("alipay.gatewayUrl"),
                PropertiesUtil.getHpayValue("alipay.appId"),
                PropertiesUtil.getHpayValue("alipay.merchantPrivatekey"),
                PropertiesUtil.getHpayValue("alipay.format"),
                PropertiesUtil.getHpayValue("alipay.charset"),
                PropertiesUtil.getHpayValue("alipay.publicKey"),
                PropertiesUtil.getHpayValue("alipay.signType"));

        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();

        request.setBizContent("{" +
                "\"trade_no\":\"" + queryData.getTradeNo() + "\"," +
                "\"out_request_no\":\"" + queryData.getOutRefundNo() + "\"" +
                "  }");
        AlipayTradeFastpayRefundQueryResponse response;

        try {
            response = alipayClient.execute(request);
            logger.info("支付宝退款查询返回信息-", response.toString());
        } catch (AlipayApiException e) {
            logger.error("alipayClient.execute-fail", e);
            respData.setSuccess(false);
            respData.setMsgCode(e.getErrCode());
            respData.setMsg(e.getErrMsg());
            return respData;
        }

        List<Map<String, Object>> responseQueryList = new ArrayList<>();

        Map<String, Object> responseQueryMap = new HashMap<>();

        if ("Success".equals(response.getMsg())) {

            responseQueryMap.put(PARAM_MODE, PropertiesUtil.getPayInfoValue("mode.alipay"));
            responseQueryMap.put(PARAM_TRADE_TYPE, queryData.getTradeType());
            responseQueryMap.put(PARAM_PAYMENT_TYPE_CODE, queryData.getPaymentTypeCode());
            responseQueryMap.put(PARAM_TRADE_RESULT_CODE, response.getCode());
            responseQueryMap.put(PARAM_TRADE_DESCRIPTION, response.getMsg());
            responseQueryMap.put(PARAM_TRADE_NO, response.getTradeNo());
            responseQueryMap.put(PARAM_OUT_TRADE_NO, response.getOutTradeNo());
            responseQueryMap.put(PARAM_OUT_REQUEST_NO, response.getOutRequestNo());
            responseQueryMap.put(PARAM_REFUND_REASON, response.getRefundReason());
            responseQueryMap.put(PARAM_TOTAL_AMOUNT, AmonutUtils.changeY2F(response.getTotalAmount()));
            responseQueryMap.put(PARAM_REFUND_AMOUNT, AmonutUtils.changeY2F(response.getRefundAmount()));

            responseQueryList.add(responseQueryMap);

            respData.setSuccess(true);
            respData.setMsgCode(response.getCode());
            respData.setMsg(response.getTradeNo());
            respData.setResp(responseQueryList);
            logger.info("支付宝退款查询成功", respData);
            return respData;
        } else {
            respData.setSuccess(false);
            respData.setMsgCode(response.getCode());
            respData.setMsg("支付宝退款查询失败" + response.getMsg() + "-" + response.getSubCode() + "-" + response.getSubMsg());
            return respData;
        }
    }
}