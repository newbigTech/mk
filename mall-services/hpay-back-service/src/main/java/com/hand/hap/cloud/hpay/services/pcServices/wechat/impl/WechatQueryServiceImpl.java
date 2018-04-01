package com.hand.hap.cloud.hpay.services.pcServices.wechat.impl;

import com.hand.hap.cloud.hpay.data.QueryData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.pcServices.wechat.IWechatQueryService;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.HttpUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.PayCommonUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.WXPayUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import com.hand.hap.cloud.hpay.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.hand.hap.cloud.hpay.utils.XmlUtil.xml2map;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.wechat.impl
 * @Description
 * @date 2017/8/21
 */
@Service
public class WechatQueryServiceImpl implements IWechatQueryService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MessageSender messageSender;

    @Override
    public ReturnData wechatRefundQuery(QueryData queryData) {
        ReturnData respData = new ReturnData();

        // 账号信息
        String appid = PropertiesUtil.getHpayValue("wechat.appId"); // appid
        String mch_id = PropertiesUtil.getHpayValue("wechat.mchId"); // 商业号
        String key = PropertiesUtil.getHpayValue("wechat.key"); // key

        String nonce_str = Util.getRandomStringByLength(32);

        String out_refund_no = queryData.getOutRefundNo(); // 订单号

        SortedMap<String, String> requestQueryMap = new TreeMap<>();

        requestQueryMap.put("appid", appid);
        requestQueryMap.put("mch_id", mch_id);
        requestQueryMap.put("nonce_str", nonce_str);
        requestQueryMap.put("out_refund_no", out_refund_no);

        String sign = null;
        try {
            sign = WXPayUtil.generateSignature(requestQueryMap, key);
        } catch (Exception e) {
            logger.error("微信退款查询请求，签名失败", e);
        }
        requestQueryMap.put("sign", sign);
        logger.info("微信request-map==》" + requestQueryMap.toString());

        String requestQueryXML = PayCommonUtil.getRequestXml(requestQueryMap);
        logger.info("微信请求参数==>" + requestQueryXML);

        ThirdPartyApiLogs logs = new ThirdPartyApiLogs();
        logs.setRequestBody(requestQueryXML);
        Date requestTime = new Date();
        logs.setRequestTime(requestTime);
        logs.setClientType("PC");
        logs.setTarget("WECHAT");
        logs.setRequestMethod("REFUND-QUERY");


        String responseQueryXML = HttpUtil.postData(PropertiesUtil.getHpayValue("wechat.refundQueryApi"), requestQueryXML);

        Date responseTime = new Date();
        logs.setResponseTime(responseTime);
        logs.setResponseBody(responseQueryXML);
        logs.setDuring(responseTime.getTime() - requestTime.getTime());

        logger.info("微信response-resXml" + responseQueryXML);
        Map<String, String> responseQueryMap = xml2map(responseQueryXML, false);
        logger.info("微信response-map" + responseQueryMap);
        try {
            if (!WXPayUtil.isSignatureValid(responseQueryMap, key)) {
                respData.setSuccess(false);
                respData.setMsgCode(PropertiesUtil.getPayInfoValue("fail"));
                respData.setMsg("签名失败");
                logger.error("微信退款查询签名验证失败");
                logs.setSuccess(false);
                logs.setRespMsg("签名失败");
                messageSender.sendMsg(logs);
                return respData;
            }
        } catch (Exception e) {
            logger.error("微信退款查询签名验证失败", e);
            logs.setSuccess(false);
            logs.setRespMsg("签名失败");
            logs.setExceptionStack(e.getStackTrace());
            messageSender.sendMsg(logs);
        }
        if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responseQueryMap.get("return_code"))) {
            if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responseQueryMap.get("result_code"))) {

                List<Map<String, Object>> responsQueryList = new ArrayList<>();

                Map<String, Object> respQueryMap = new HashMap<>();

                respQueryMap.put("mode", PropertiesUtil.getPayInfoValue("mode.wechatpay"));
                respQueryMap.put("tradeType", responseQueryMap.get("trade_type"));
                respQueryMap.put("paymentTypeCode", queryData.getPaymentTypeCode());
                respQueryMap.put("tradeResultCode", responseQueryMap.get("result_code"));
                respQueryMap.put("tradeDescription", responseQueryMap.get("trade_state_desc"));
                respQueryMap.put("tradeStatus", responseQueryMap.get("trade_state"));
                respQueryMap.put("tradeNo", responseQueryMap.get("transaction_id"));
                respQueryMap.put("outTradeNo", responseQueryMap.get("out_trade_no"));
                respQueryMap.put("totalAmount", responseQueryMap.get("total_fee"));
                respQueryMap.put("receipAmount", responseQueryMap.get("total_fee"));
                responsQueryList.add(respQueryMap);
                respData.setSuccess(true);
                respData.setMsgCode(responseQueryMap.get("result_code"));
                respData.setMsg(responseQueryMap.get("trade_state_desc"));
                respData.setResp(responsQueryList);
                logger.info("微信支付查询成功-data-", respData);
                logs.setSuccess(true);
                logs.setRespMsgCode(responseQueryMap.get("return_code"));
                messageSender.sendMsg(logs);
                return respData;
            } else {
                respData.setSuccess(false);
                respData.setMsgCode(responseQueryMap.get("err_code"));
                respData.setMsg(responseQueryMap.get("err_code_des"));
                logs.setRespMsg(responseQueryMap.get("err_code_des"));
                logs.setRespMsgCode(responseQueryMap.get("err_code"));
                logs.setSuccess(false);
                messageSender.sendMsg(logs);
                logger.error("微信退款查询失败，业务失败-errorCode-" + responseQueryMap.get("err_code") + "errorMsg-" + responseQueryMap.get("err_code_des"));
                return respData;
            }
        } else {
            respData.setSuccess(false);
            respData.setMsgCode(responseQueryMap.get("return_code"));
            respData.setMsg(responseQueryMap.get("return_msg"));
            logs.setRespMsg(responseQueryMap.get("return_msg"));
            logs.setRespMsgCode(responseQueryMap.get("return_code"));
            logs.setSuccess(false);
            messageSender.sendMsg(logs);
            logger.error("微信退款查询失败，通信失败-errorCode-" + responseQueryMap.get("return_code") + "errorMsg-" + responseQueryMap.get("return_msg"));
            return respData;
        }
    }

    @Override
    public ReturnData wechatPayQuery(QueryData queryData) {
        ReturnData respData = new ReturnData();
        // 账号信息
        String appId = PropertiesUtil.getHpayValue("wechat.appId"); // appId
        String mchId = PropertiesUtil.getHpayValue("wechat.mchId"); // 商业号
        String key = PropertiesUtil.getHpayValue("wechat.key"); // key

        String nonceStr = Util.getRandomStringByLength(32);

        String out_trade_no = queryData.getOutTradeNo(); // 订单号

        SortedMap<String, String> requestQueryMap = new TreeMap<>();

        requestQueryMap.put("appid", appId);
        requestQueryMap.put("mch_id", mchId);
        requestQueryMap.put("nonce_str", nonceStr);
        requestQueryMap.put("out_trade_no", out_trade_no);

        String sign = null;
        try {
            sign = WXPayUtil.generateSignature(requestQueryMap, key);
        } catch (Exception e) {
            ThirdPartyApiLogs logs = new ThirdPartyApiLogs();
            logs.setRequestTime(new Date());
            logs.setClientType("PC");
            logs.setTarget("WECHAT");
            logs.setRequestMethod("PAY-QUERY");
            logs.setExceptionStack(e.getStackTrace());
            messageSender.sendMsg(logs);
            logger.error("微信支付查询请求，签名失败", e);
        }
        requestQueryMap.put("sign", sign);
        logger.info("微信request-requestQueryMap==》" + requestQueryMap.toString());

        String requestQueryXML = PayCommonUtil.getRequestXml(requestQueryMap);
        logger.info("微信request-requestQueryXML==>" + requestQueryXML);
        //记录日志 请求
        ThirdPartyApiLogs logs = new ThirdPartyApiLogs();
        logs.setRequestBody(requestQueryXML);
        Date requestTime = new Date();
        logs.setRequestTime(requestTime);
        logs.setClientType("PC");
        logs.setTarget("WECHAT");
        logs.setRequestMethod("REFUND-QUERY");

        String responseQueryXml = HttpUtil.postData(PropertiesUtil.getHpayValue("wechat.payQueryApi"), requestQueryXML);

        logs.setResponseBody(responseQueryXml);
        Date responseTime = new Date();
        logs.setResponseTime(responseTime);
        logs.setDuring(responseTime.getTime() - requestTime.getTime());

        logger.info("微信response-responseQueryXml" + responseQueryXml);
        Map<String, String> responseQueryMap = xml2map(responseQueryXml, false);

        logger.info("微信response-responseQueryMap" + responseQueryMap);

        try {
            if (!WXPayUtil.isSignatureValid(responseQueryMap, key)) {
                respData.setSuccess(false);
                respData.setMsgCode(PropertiesUtil.getPayInfoValue("fail"));
                respData.setMsg("微信支付查询签名验证失败");
                logger.error("微信支付查询签名验证失败");
                logs.setSuccess(false);
                logs.setRespMsg("签名失败");
                messageSender.sendMsg(logs);
                return respData;
            }
        } catch (Exception e) {
            logs.setSuccess(false);
            logs.setRespMsg("签名失败");
            logs.setExceptionStack(e.getStackTrace());
            messageSender.sendMsg(logs);
            logger.error("微信支付查询签名验证失败", e);
        }
        if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responseQueryMap.get("return_code"))) {
            if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responseQueryMap.get("result_code"))) {
                List<Map<String, Object>> requestZmallList = new ArrayList<>();

                Map<String, Object> requestZmallMap = new HashMap<>();

                requestZmallMap.put("mode", PropertiesUtil.getPayInfoValue("mode.wechatpay"));
                requestZmallMap.put("tradeType", queryData.getTradeType());
                requestZmallMap.put("paymentTypeCode", queryData.getPaymentTypeCode());
                requestZmallMap.put("tradeResultCode", responseQueryMap.get("result_code"));
                requestZmallMap.put("tradeDescription", responseQueryMap.get("trade_state_desc"));
                requestZmallMap.put("tradeStatus", responseQueryMap.get("trade_state"));
                requestZmallMap.put("tradeNo", responseQueryMap.get("transaction_id"));
                requestZmallMap.put("outTradeNo", responseQueryMap.get("out_trade_no"));
                requestZmallMap.put("totalAmount", responseQueryMap.get("total_fee"));
                requestZmallMap.put("receipAmount", responseQueryMap.get("total_fee"));
                requestZmallList.add(requestZmallMap);
                respData.setSuccess(true);
                respData.setMsgCode(responseQueryMap.get("result_code"));
                respData.setMsg(responseQueryMap.get("trade_state_desc"));
                respData.setResp(requestZmallList);
                logger.info("微信支付查询成功-data-", respData);
                logs.setSuccess(true);
                logs.setRespMsgCode(responseQueryMap.get("return_code"));
                messageSender.sendMsg(logs);
                return respData;
            } else {
                respData.setSuccess(false);
                respData.setMsgCode(responseQueryMap.get("err_code"));
                respData.setMsg(responseQueryMap.get("err_code_des"));
                logger.error("微信支付查询失败，业务失败-errorCode-" + responseQueryMap.get("err_code") + "errorMsg-" + responseQueryMap.get("err_code_des"));
                logs.setRespMsg(responseQueryMap.get("err_code_des"));
                logs.setRespMsgCode(responseQueryMap.get("err_code"));
                logs.setSuccess(false);
                messageSender.sendMsg(logs);
                return respData;
            }
        } else {
            respData.setSuccess(false);
            respData.setMsgCode(responseQueryMap.get("return_code"));
            respData.setMsg(responseQueryMap.get("return_msg"));
            logger.error("微信支付查询失败，通信失败-errorCode-" + responseQueryMap.get("return_code") + "errorMsg-" + responseQueryMap.get("return_msg"));
            logs.setRespMsg(responseQueryMap.get("return_msg"));
            logs.setRespMsgCode(responseQueryMap.get("return_code"));
            logs.setSuccess(false);
            messageSender.sendMsg(logs);
            return respData;
        }
    }
}
