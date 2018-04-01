package com.hand.hap.cloud.hpay.services.phoneServices.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.data.h5_info;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.service.IPhoneWechatService;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.HttpUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.PayCommonUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.WXPayUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import com.hand.hap.cloud.hpay.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static com.hand.hap.cloud.hpay.utils.XmlUtil.xml2map;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.phoneServices.alipay.impl
 * @Description
 * @date 2017/8/3
 */
@Service
public class PhoneWechatServiceImpl implements IPhoneWechatService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${wechat.notifyUrl}")
    private String wechatNotifyUrl;

    @Autowired
    private MessageSender messageSender;

    /**
     * wechatH5支付
     *
     * @param request   request
     * @param resp      resp
     * @param orderData 支付数据
     * @return returnData
     * @throws Exception exception
     */
    @Override
    public ReturnData phoneH5Wechat(HttpServletRequest request, HttpServletResponse resp, OrderData orderData) {
        // 账号信息
        // appid
        String appid = PropertiesUtil.getHpayValue("wechat.appId");
        // 商业号
        String mch_id = PropertiesUtil.getHpayValue("wechat.mchId");
        String key = PropertiesUtil.getHpayValue("wechat.key");
        String nonce_str = Util.getRandomStringByLength(32);
        // 订单号
        String outTradeNo = orderData.getOutTradeNo();
        // 订单金额
        String account = orderData.getAmount();
        // 商品名称
        String body = orderData.getDescription();

        String sceneInfo = JSON.toJSONString(new h5_info("Wap", "", ""));
        // 回调接口
        String notifyUrl = wechatNotifyUrl;

        logger.info("微信h5支付异步回调地址：" + notifyUrl);
        // 交易类型
        String tradeType = orderData.getTradeType();

        TreeMap<String, String> requestWXMap = new TreeMap<>();

        requestWXMap.put("appid", appid);
        requestWXMap.put("mch_id", mch_id);
        requestWXMap.put("nonce_str", nonce_str);
        requestWXMap.put("body", body);
        requestWXMap.put("out_trade_no", outTradeNo);
        requestWXMap.put("total_fee", account);
        requestWXMap.put("spbill_create_ip", orderData.getIp());//移动端这个IP要改
        requestWXMap.put("notify_url", notifyUrl);
        requestWXMap.put("trade_type", tradeType);
        requestWXMap.put("scene_info", sceneInfo);

        String sign = null;
        try {
            sign = WXPayUtil.generateSignature(requestWXMap, key);
        } catch (Exception e) {
            logger.error("WXPayUtil-generateSignature-fail", e);
        }
        requestWXMap.put("sign", sign);
        logger.info("微信request-responseMapFromWX==》" + requestWXMap.toString());

        String requestWXXML = PayCommonUtil.getRequestXml(requestWXMap);
        logger.info("微信requestWXXML==>" + requestWXXML);
        //记录日志 请求
        ThirdPartyApiLogs logs = new ThirdPartyApiLogs();
        Date requestTime = new Date();
        logs.setRequestTime(requestTime);
        logs.setTarget("WECHAT");
        logs.setRequestBody(requestWXXML);
        logs.setRequestMethod("PAY");
        logs.setClientType("MOBILE");

        String responseXMLFromWX = HttpUtil.postData(PropertiesUtil.getHpayValue("wechat.unifiedPayApi"), requestWXXML);
        Date responseTime = new Date();
        logs.setResponseTime(responseTime);
        logs.setResponseBody(responseXMLFromWX);
        logs.setDuring(responseTime.getTime() - requestTime.getTime());
        logger.info("微信respomse-Xml==>" + responseXMLFromWX);

        Map<String, String> responsePayMapFromWX = xml2map(responseXMLFromWX, false);
        logger.info("微信response-responseMapFromWX==>" + responsePayMapFromWX);

        if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responsePayMapFromWX.get("return_code"))) {
            if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responsePayMapFromWX.get("result_code"))) {
                String url = responsePayMapFromWX.get("mweb_url");
                if (null != url && !"".equals(url)) {
                    logger.info("微信PHONE支付请求成功：" + url);
                    logs.setSuccess(true);
                    logs.setRespMsgCode(responsePayMapFromWX.get("return_code"));
                    messageSender.sendMsg(logs);
                    return new ReturnData(responsePayMapFromWX.get("return_code"), url, true);
                }
                logger.info("微信PHONE支付请求失败(URL为空)：" + responsePayMapFromWX.get("err_code") + "-err_code_des-" + responsePayMapFromWX.get("err_code_des"));
                logs.setRespMsgCode(responsePayMapFromWX.get("err_code_des"));
                logs.setRespMsg(responsePayMapFromWX.get("err_code"));
                logs.setSuccess(false);
                messageSender.sendMsg(logs);
                return new ReturnData(responsePayMapFromWX.get("err_code"), responsePayMapFromWX.get("err_code_des"), false);
            }
            logger.info("微信PHONE支付请求失败(业务失败)：" + responsePayMapFromWX.get("err_code") + "-err_code_des-" + responsePayMapFromWX.get("err_code_des"));
            logs.setRespMsgCode(responsePayMapFromWX.get("err_code_des"));
            logs.setRespMsg(responsePayMapFromWX.get("err_code"));
            logs.setSuccess(false);
            messageSender.sendMsg(logs);
            return new ReturnData(responsePayMapFromWX.get("err_code"), responsePayMapFromWX.get("err_code_des"), false);
        }
        logger.info("微信PHONE支付请求失败(请求信息有误)" + responsePayMapFromWX.get("return_code") + "-err_code_des-" + responsePayMapFromWX.get("return_msg"));
        logs.setRespMsgCode(responsePayMapFromWX.get("return_code"));
        logs.setRespMsg(responsePayMapFromWX.get("return_msg"));
        logs.setSuccess(false);
        messageSender.sendMsg(logs);
        return new ReturnData(responsePayMapFromWX.get("return_code"), responsePayMapFromWX.get("return_msg"), false);
    }
}
