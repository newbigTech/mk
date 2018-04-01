package com.hand.hap.cloud.hpay.services.pcServices.wechat.impl;

import com.hand.hap.cloud.hpay.data.RefundData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.pcServices.wechat.IWeChatRefundService;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.HttpUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.PayCommonUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.WXPayUtil;
import com.hand.hap.cloud.hpay.utils.DateFormatUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import com.hand.hap.cloud.hpay.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.hand.hap.cloud.hpay.services.constants.Constants.*;
import static com.hand.hap.cloud.hpay.utils.XmlUtil.xml2map;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
@Service
public class WeChatRefundServiceImpl implements IWeChatRefundService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSender messageSender;

    /**
     * 微信支付订单退款
     *
     * @param refundData 退款数据
     * @return rturnData
     */
    @Override
    public ReturnData weChatRefund(RefundData refundData) throws Exception {

        ReturnData respData = new ReturnData();
        // 账号信息
        String appId = PropertiesUtil.getHpayValue("wechat.appId"); // appId
        String mchId = PropertiesUtil.getHpayValue("wechat.mchId"); // 商业号
        String key = PropertiesUtil.getHpayValue("wechat.key"); // key

        String nonceStr = Util.getRandomStringByLength(32);
        String out_refund_no = refundData.getOutRefundNo();
        String transaction_id = refundData.getTradeNo(); // 订单号
        //退款金额
        String refund_fee = refundData.getAmount();
        //订单总金额
        String orderTotalFee = refundData.getTotalAmount();
        SortedMap<String, String> requestRefundMap = new TreeMap<>();

        requestRefundMap.put("appid", appId);
        requestRefundMap.put("mch_id", mchId);
        requestRefundMap.put("nonce_str", nonceStr);
        requestRefundMap.put("out_refund_no", out_refund_no);
        requestRefundMap.put("transaction_id", transaction_id);
        requestRefundMap.put("refund_fee", refund_fee);
        requestRefundMap.put("total_fee", orderTotalFee);

        String sign = WXPayUtil.generateSignature(requestRefundMap, key);
        requestRefundMap.put("sign", sign);
        logger.info("微信request-responseRefundMap==》" + requestRefundMap.toString());

        String requestRefundXML = PayCommonUtil.getRequestXml(requestRefundMap);
        logger.info("微信请求参数==>" + requestRefundXML);
        //记录日志 请求
        ThirdPartyApiLogs logs = new ThirdPartyApiLogs();
        Date requestTime = new Date();
        logs.setRequestTime(requestTime);
        logs.setTarget("WECHAT");
        logs.setRequestBody(requestRefundXML);
        logs.setRequestMethod("");
        logs.setClientType("PC");

        String responseRefundXML = HttpUtil.postDataWithCert(
                PropertiesUtil.getHpayValue("wechat.certLocalPath"),
                PropertiesUtil.getHpayValue("wechat.certPassWord"),
                PropertiesUtil.getHpayValue("wechat.refundApi"),
                requestRefundXML);
        //日志返回
        logs.setResponseBody(responseRefundXML);
        Date responseTime = new Date();
        logs.setResponseTime(responseTime);
        logs.setDuring(responseTime.getTime() - requestTime.getTime());
        logger.info("微信response-responseRefundXML" + responseRefundXML);
        Map<String, String> responseRefundMap = xml2map(responseRefundXML, false);
        logger.info("微信response-responseRefundMap" + responseRefundMap);
        if (!WXPayUtil.isSignatureValid(requestRefundMap, key)) {
            respData.setSuccess(false);
            respData.setMsgCode(PropertiesUtil.getPayInfoValue("success"));
            respData.setMsg("签名失败");

            logs.setRespMsg("签名失败");
            logs.setRespMsgCode(PropertiesUtil.getPayInfoValue("fail"));
            logs.setSuccess(false);
            messageSender.sendMsg(logs);
            return respData;
        }
        if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responseRefundMap.get("return_code"))) {
            if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responseRefundMap.get("result_code"))) {

                List<Map<String, Object>> responseRefundList = new ArrayList<>();

                Map<String, Object> responseMap = new HashMap<>();

                responseMap.put(PARAM_MODE, PropertiesUtil.getPayInfoValue("mode.wechatpay"));
                responseMap.put(PARAM_PAYMENT_TYPE_CODE, refundData.getPaymentTypeCode());
                responseMap.put(PARAM_TRADE_RESULT_CODE, responseRefundMap.get("result_code"));
                responseMap.put(PARAM_TRADE_DESCRIPTION, responseRefundMap.get("return_msg"));
                responseMap.put(PARAM_TRADE_STATUS, responseRefundMap.get("result_code"));
                responseMap.put(PARAM_TRADE_NO, responseRefundMap.get("transaction_id"));
                responseMap.put(PARAM_OUT_TRADE_NO, responseRefundMap.get("out_trade_no"));
                responseMap.put(PARAM_TOTAL_AMOUNT, responseRefundMap.get("total_fee"));
                responseMap.put(PARAM_RECEIP_AMOUNT, responseRefundMap.get("refund_fee"));
                responseMap.put("tradePayTime", DateFormatUtil.format(new Date(), "yyyyMMddHHmmss"));
                //退款总金额
                responseMap.put("buyerPayAmount", responseRefundMap.get("refund_fee"));

                responseRefundList.add(responseMap);
                respData.setSuccess(true);
                respData.setMsgCode(responseRefundMap.get("result_code"));
                respData.setMsg(responseRefundMap.get("return_msg"));
                respData.setResp(responseRefundList);
                logger.info("微信退款成功");
                logs.setSuccess(true);
                logs.setRespMsgCode(responseRefundMap.get("result_code"));
                logs.setRespMsg(responseRefundMap.get("return_msg"));
                messageSender.sendMsg(logs);
                return respData;
            } else {
                respData.setSuccess(false);
                respData.setMsgCode(responseRefundMap.get("err_code"));
                respData.setMsg(responseRefundMap.get("err_code_des"));
                logger.error("微信退款失败，业务失败-errorCode-" + responseRefundMap.get("err_code") + "-errorMsg-" + responseRefundMap.get("err_code_des"));
                logs.setRespMsg(responseRefundMap.get("err_code_des"));
                logs.setRespMsgCode(responseRefundMap.get("err_code"));
                logs.setSuccess(false);
                messageSender.sendMsg(logs);
                return respData;
            }
        } else {
            respData.setSuccess(false);
            respData.setMsgCode(responseRefundMap.get("return_code"));
            respData.setMsg(responseRefundMap.get("return_msg"));
            logger.error("微信退款失败，通信失败-errorCode-" + responseRefundMap.get("return_code") + "-errorMsg-" + responseRefundMap.get("return_msg"));
            logs.setRespMsg(responseRefundMap.get("return_msg"));
            logs.setRespMsgCode(responseRefundMap.get("return_code"));
            logs.setSuccess(false);
            messageSender.sendMsg(logs);
            return respData;
        }
    }
}