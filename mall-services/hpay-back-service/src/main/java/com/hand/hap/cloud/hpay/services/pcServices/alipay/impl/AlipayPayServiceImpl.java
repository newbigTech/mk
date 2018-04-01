package com.hand.hap.cloud.hpay.services.pcServices.alipay.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.OutBoundLogs;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.pcServices.alipay.IAlipayPayService;
import com.hand.hap.cloud.hpay.services.toZmall.IPostTargetSystemService;
import com.hand.hap.cloud.hpay.utils.AmonutUtils;
import com.hand.hap.cloud.hpay.utils.DateFormatUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hand.hap.cloud.hpay.services.constants.AlipayConstants.*;
import static com.hand.hap.cloud.hpay.services.constants.Constants.*;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
@Service
public class AlipayPayServiceImpl implements IAlipayPayService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${alipay.notifyUrl}")
    private String alipayNotifyUrl;

    @Value("${zmall.notifyUrl}")
    private String zmallNotifyUrl;

    @Value("${hmall.notifyUrl}")
    private String hmallNotifyUrl;

    @Autowired
    private MessageSender messageSender;
    @Autowired
    private IPostTargetSystemService postTargetSystemService;

    /**
     * 封装支付信息提交请求支付接口
     *
     * @param orderData 订单数据
     * @return returnData
     */
    @Override
    public ReturnData pcAlipay(OrderData orderData) throws Exception {

        ReturnData returnData = new ReturnData();
        //分->元
        String totalAmount = AmonutUtils.changeF2Y(orderData.getAmount());
        //组装数据，alipay基础数据
        AlipayClient client = new DefaultAlipayClient(
                PropertiesUtil.getHpayValue("alipay.gatewayUrl"),
                PropertiesUtil.getHpayValue("alipay.appId"),
                PropertiesUtil.getHpayValue("alipay.merchantPrivatekey"),
                PropertiesUtil.getHpayValue("alipay.format"),
                PropertiesUtil.getHpayValue("alipay.charset"),
                PropertiesUtil.getHpayValue("alipay.publicKey"),
                PropertiesUtil.getHpayValue("alipay.signType"));

        //设置请求参数
        AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
        alipayTradePagePayRequest.setReturnUrl(orderData.getReturnUrl());
        alipayTradePagePayRequest.setNotifyUrl(alipayNotifyUrl);
        alipayTradePagePayRequest.setBizContent(
                "{\"out_trade_no\":\"" + orderData.getOutTradeNo() + "\","
                        + "\"total_amount\":\"" + totalAmount + "\","
                        + "\"subject\":\"" + orderData.getProductName() + "\","
                        + "\"qr_pay_mode\":\"" + orderData.getQrPayMode() + "\","
                        + "\"body\":\"" + orderData.getDescription() + "\","
                        //用来传输支付类型
                        + "\"passback_params\":\"" + orderData.getTradeType() + "\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        logger.info("=====pcAlipay-request========" + alipayTradePagePayRequest);
        //开始记录日志
        ThirdPartyApiLogs log = new ThirdPartyApiLogs();
        //业务数据
        log.setRequestBody(alipayTradePagePayRequest.getBizContent());
        //请求时间
        Date requestTime = new Date();
        log.setRequestTime(requestTime);
        //请求目标
        log.setTarget(PropertiesUtil.getPayInfoValue("mode.alipay"));
        //请求方法
        log.setRequestMethod(PropertiesUtil.getPayInfoValue("paymentTypeCode.pay"));
        //请求源
        log.setClientType(PropertiesUtil.getPayInfoValue("mode.tradeType.pc"));

        String responsePayStr;
        try {
            //发送请求
            responsePayStr = client.pageExecute(alipayTradePagePayRequest).getBody();
            Date responseTime = new Date();
            log.setResponseTime(responseTime);
            log.setResponseBody(responsePayStr);
            log.setDuring(responseTime.getTime() - requestTime.getTime());
            log.setSuccess(true);
            messageSender.sendMsg(log);
            logger.info("AlipayPayServiceImpl-pcAlipay-client.pageExecute-success-responsePayStr-" + responsePayStr);
        } catch (AlipayApiException e) {
            logger.error("AlipayPayServiceImpl-pcAlipay-client.pageExecute-fail-", e);
            returnData.setSuccess(false);
            returnData.setMsgCode(e.getErrCode());
            returnData.setMsg(e.getErrMsg());
            log.setSuccess(false);
            log.setExceptionCode(e.getErrCode());
            log.setExceptionMsg(e.getErrMsg());
            log.setExceptionStack(e.getStackTrace());
            messageSender.sendMsg(log);
            return returnData;
        }
        returnData.setSuccess(true);
        returnData.setMsg(responsePayStr);
        return returnData;
    }

    /**
     * 支付宝支付回调验证请求
     *
     * @param request  request
     * @param response response
     * @throws IOException IOException
     */
    public String alipayNotify(HttpServletRequest request, HttpServletResponse response) {
        logger.info("支付宝回调开始");
        //获取支付宝POST过来反馈信息
        Map<String, String[]> notifyParams = request.getParameterMap();
        Map<String, String> notifyParam = new HashMap<>();

        OutBoundLogs logs = new OutBoundLogs();

        for (String name : notifyParams.keySet()) {

            String[] values = notifyParams.get(name);
            String valueStr = "";

            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            notifyParam.put(name, valueStr);
        }
        logger.info("获得支付宝回调Map" + notifyParam);

        boolean signVerified = false;
        try {
            //调用SDK验证签名
            signVerified = AlipaySignature.rsaCheckV1(
                    notifyParam,
                    PropertiesUtil.getHpayValue("alipay.publicKey"),
                    PropertiesUtil.getHpayValue("alipay.charset"),
                    PropertiesUtil.getHpayValue("alipay.signType"));
        } catch (AlipayApiException e) {
            logs.setExceptionStack(e.getStackTrace());
            logs.setSuccess(false);
            logs.setMessage("未能发送请求：签名验证失败 AlipayPayServiceImpl-alipayNotify-AlipaySignature.rsaCheckV1-fail " + e.getErrMsg());
            messageSender.sendMsg(logs);
            logger.error("AlipayPayServiceImpl-alipayNotify-AlipaySignature.rsaCheckV1-fail", e);
        }
        HashMap<String, Object> reqeustMap = new HashMap();
        if (signVerified) {
            //验证成功
            logger.info("AlipayPayServiceImpl-alipayNotify-AlipaySignature.rsaCheckV1-success");

            String tradeResultCode = "";

            String tradeDescription = "";

            //订单状态
            String tradeStatus = request.getParameter("trade_status");

            //交易付款时间
            String gmtPayment = "";

            //付款总金额
            String buyerPayAmount = "";

            if (PARAM_WAIT_BUYER_PAY.equals(tradeStatus)) {
                tradeResultCode = PropertiesUtil.getPayInfoValue("failed");
                tradeDescription = "交易创建，等待买家付款";

            }

            if (PARAM_TRADE_CLOSED.equals(tradeStatus)) {
                tradeResultCode = PropertiesUtil.getPayInfoValue("failed");
                tradeDescription = "未付款交易超时关闭，或支付完成后全额退款";
            }

            if (PARAM_TRADE_SUCCESS.equals(tradeStatus) || PARAM_TRADE_FINISHED.equals(tradeStatus)) {

                if (PARAM_TRADE_SUCCESS.equals(tradeStatus)) {
                    tradeResultCode = PropertiesUtil.getPayInfoValue("success");
                    tradeDescription = "交易支付成功";
                }

                if (PARAM_TRADE_FINISHED.equals(tradeStatus)) {
                    tradeResultCode = PropertiesUtil.getPayInfoValue("success");
                    tradeDescription = "交易结束，不可退款";
                }
                //交易付款时间
                gmtPayment = request.getParameter("gmt_payment");

                buyerPayAmount = AmonutUtils.changeY2F(request.getParameter("buyer_pay_amount"));
            }

            reqeustMap.put(PARAM_TRADE_RESULT_CODE, tradeResultCode);
            reqeustMap.put(PARAM_TRADE_DESCRIPTION, tradeDescription);
            reqeustMap.put(PARAM_TRADE_TYPE, request.getParameter("passback_params"));
            reqeustMap.put(PARAM_NOTIFY_ID, request.getParameter("trade_no"));
            reqeustMap.put(PARAM_TRADE_CREATE_TIME, request.getParameter("gmt_create"));
            reqeustMap.put(PARAM_NOTIFY_TIME, DateFormatUtil.getCurrentTime());
            reqeustMap.put(PARAM_BUYER_PAY_AMOUNT, buyerPayAmount);

            reqeustMap.put(PARAM_MODE, PropertiesUtil.getPayInfoValue("mode.alipay"));
            reqeustMap.put(PARAM_TOTAL_AMOUNT, AmonutUtils.changeY2F(request.getParameter("total_amount")));
            reqeustMap.put(PARAM_TRADE_NO, request.getParameter("trade_no"));
            reqeustMap.put(PARAM_OUT_TRADE_NO, request.getParameter("out_trade_no"));
            reqeustMap.put(PARAM_TRADE_PAY_TIME, gmtPayment);
            String url = "";
            try {
                //服务销售单支付信息回调给hmall
                if (request.getParameter("out_trade_no").startsWith("SV")) {
                    url = hmallNotifyUrl;
                    String content = JSON.toJSONString(reqeustMap);
                    logs.setRequestTime(new Date());
                    logs.setTargetSystem("HMALL");
                    logs.setUsage("ALIPAY-PAY");
                    logger.info("支付宝回调，请求hmall数据-" + content);
                    logs.setRequestAddr(hmallNotifyUrl);
                    logs.setRequestBody(content);
                    JSONObject responseFromHmall = postTargetSystemService.postToTargetSystem(hmallNotifyUrl, content, null);
                    String responseString = JSON.toJSONString(responseFromHmall);
                    Date responseTime = new Date();
                    logs.setResponseTime(responseTime);
                    logs.setDuring(responseTime.getTime() - logs.getRequestTime().getTime());
                    logs.setResponseBody(responseString);
                    if (!(Boolean) responseFromHmall.get("success")) {
                        logger.error("请求hmall失败，业务失败-" + responseFromHmall.getString("msg"));
                        logs.setMessage("请求hmall失败，业务失败-" + responseFromHmall.getString("msg"));
                        logs.setSuccess(false);
                        messageSender.sendMsg(logs);
                        return PropertiesUtil.getPayInfoValue("fail");
                    }
                } else {//订单支付信息回调给zmall
                    url = zmallNotifyUrl;
                    String content = JSON.toJSONString(reqeustMap);
                    logs.setRequestTime(new Date());
                    logs.setTargetSystem("ZMALL");
                    logs.setUsage("ALIPAY-PAY");
                    logger.info("支付宝回调，请求zmall数据-" + content);
                    logs.setRequestAddr(zmallNotifyUrl);
                    logs.setRequestBody(content);
                    JSONObject responseFromZmall = postTargetSystemService.postToTargetSystem(zmallNotifyUrl, content, null);
                    String responseString = JSON.toJSONString(responseFromZmall);
                    Date responseTime = new Date();
                    logs.setResponseTime(responseTime);
                    logs.setDuring(responseTime.getTime() - logs.getRequestTime().getTime());
                    logs.setResponseBody(responseString);
                    if (StringUtils.isEmpty(responseFromZmall.getString("code")) || !"1".equals(responseFromZmall.getString("code"))) {
                        logger.error("请求zmall失败，业务失败-errorCode" + responseFromZmall.getString("code") + "errorMsg-" + responseFromZmall.getString("message"));
                        logs.setMessage("请求zmall失败，业务失败-errorCode" + responseFromZmall.getString("code") + "errorMsg-" + responseFromZmall.getString("message"));
                        logs.setSuccess(false);
                        messageSender.sendMsg(logs);
                        return PropertiesUtil.getPayInfoValue("fail");
                    }
                }
            } catch (Exception e) {
                logs.setMessage("请求目标系统:" + url + "失败,请求失败");
                logs.setSuccess(false);
                logs.setExceptionStack(e.getStackTrace());
                messageSender.sendMsg(logs);
                logger.error("请求目标系统:" + url + "失败,请求失败", e);
                return PropertiesUtil.getPayInfoValue("fail");
            }
            //收到异步通知
            logs.setSuccess(true);
            messageSender.sendMsg(logs);
            return PropertiesUtil.getPayInfoValue("success");
        } else {
            //验证失败
            logger.error("支付宝回调签名验证失败");
            return PropertiesUtil.getPayInfoValue("fail");
        }
    }
}
