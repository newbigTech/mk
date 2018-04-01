package com.hand.hap.cloud.hpay.services.phoneServices.alipay.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.phoneServices.alipay.IPhoneAlipayService;
import com.hand.hap.cloud.hpay.utils.AmonutUtils;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.phoneServices.alipay.impl
 * @Description
 * @date 2017/8/3
 */
@Service
public class PhoneAlipayServiceImpl implements IPhoneAlipayService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${alipay.notifyUrl}")
    private String alipayNotifyUrl;

    @Autowired
    private MessageSender messageSender;

    /**
     * alipayH5支付
     *
     * @param request   request
     * @param response  response
     * @param orderData 订单数据
     * @return returnData
     * @throws Exception exception
     */
    @Override
    public ReturnData phoneH5Alipay(HttpServletRequest request, HttpServletResponse response, OrderData orderData) throws Exception {

        ReturnData returnData = new ReturnData();
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = orderData.getOutTradeNo();
        // 订单名称，必填
        String subject = orderData.getProductName();
        // 付款金额，必填
        String total_amount = AmonutUtils.changeF2Y(orderData.getAmount());
        // 商品描述，可空
        String body = orderData.getDescription();
        //用来传输支付类型
        String passback_params = orderData.getTradeType();
        // 销售产品码 必填
        String product_code = "QUICK_WAP_PAY";
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(
                PropertiesUtil.getHpayValue("alipay.gatewayUrl"),
                PropertiesUtil.getHpayValue("alipay.appId"),
                PropertiesUtil.getHpayValue("alipay.merchantPrivatekey"),
                PropertiesUtil.getHpayValue("alipay.format"),
                PropertiesUtil.getHpayValue("alipay.charset"),
                PropertiesUtil.getHpayValue("alipay.publicKey"),
                PropertiesUtil.getHpayValue("alipay.signType"));
        AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel alipayTradeWapPayModel = new AlipayTradeWapPayModel();
        alipayTradeWapPayModel.setOutTradeNo(out_trade_no);
        alipayTradeWapPayModel.setSubject(subject);
        alipayTradeWapPayModel.setTotalAmount(total_amount);
        alipayTradeWapPayModel.setBody(body);
        alipayTradeWapPayModel.setProductCode(product_code);
        alipayTradeWapPayModel.setPassbackParams(passback_params);
        alipayTradeWapPayRequest.setBizModel(alipayTradeWapPayModel);
        // 设置异步通知地址
        alipayTradeWapPayRequest.setNotifyUrl(alipayNotifyUrl);
        // 设置同步地址
        alipayTradeWapPayRequest.setReturnUrl(orderData.getReturnUrl());

        //打印请求参数
        logger.info(alipayTradeWapPayRequest.toString());
        // form表单生产
        String responseFromAlipay;
        ThirdPartyApiLogs logs = new ThirdPartyApiLogs();
        logs.setRequestBody(JSONObject.toJSONString(alipayTradeWapPayModel));
        logs.setRequestMethod("PAY");
        logs.setClientType("MOBILE");
        logs.setTarget("ALIPAY");
        Date requestTime = new Date();
        logs.setRequestTime(requestTime);

        try {
            //调用SDK生成表单
            responseFromAlipay = client.pageExecute(alipayTradeWapPayRequest).getBody();
            logs.setResponseBody(responseFromAlipay);
            Date responseTime = new Date();
            logs.setResponseTime(responseTime);
            logs.setDuring(responseTime.getTime() - requestTime.getTime());
            logs.setSuccess(true);
            returnData.setSuccess(true);
            returnData.setMsg(responseFromAlipay);
            logger.info(responseFromAlipay);
        } catch (AlipayApiException e) {
            logger.error(this.getClass().getName(),e);
            returnData.setSuccess(false);
            returnData.setMsgCode(e.getErrCode());
            returnData.setMsg(e.getErrMsg());

            logs.setSuccess(false);
            logs.setExceptionStack(e.getStackTrace());
            logs.setRespMsgCode(e.getErrCode());
            logs.setRespMsg(e.getErrMsg());
            messageSender.sendMsg(logs);
            return returnData;
        }
        messageSender.sendMsg(logs);
        return returnData;
    }
}
