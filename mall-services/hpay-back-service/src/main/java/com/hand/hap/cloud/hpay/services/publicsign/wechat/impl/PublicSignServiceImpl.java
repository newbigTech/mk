package com.hand.hap.cloud.hpay.services.publicsign.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.constants.WecatConstants;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.HttpUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.PayCommonUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.WXPayUtil;
import com.hand.hap.cloud.hpay.services.publicsign.wechat.IPublicSignService;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import com.hand.hap.cloud.hpay.utils.SendReq;
import com.hand.hap.cloud.hpay.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.hand.hap.cloud.hpay.utils.XmlUtil.xml2map;

/**
 * @Author:zhangyanan
 * @Description:微信公众号支付
 * @Date:Crated in 15:34 2017/10/21
 * @Modified By:
 */
@Service
public class PublicSignServiceImpl implements IPublicSignService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${wechat.notifyUrl}")
    private String wechatNotifyUrl;

    //传送openId给zmall地址
    @Value("${zmall.authorizationOpenIdUrl}")
    private String authorizationOpenIdUrl;

    @Autowired
    private MessageSender messageSender;

    /**
     * 微信公众号支付
     *
     * @param request   request
     * @param resp      resp
     * @param orderData 订单数据
     * @return returnData
     * @throws Exception exception
     */
    @Override
    public ReturnData publicSign(HttpServletRequest request, HttpServletResponse resp, OrderData orderData) throws Exception {

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
        //终端IP
        String ip = orderData.getIp();
        // 回调接口
        String notifyUrl = wechatNotifyUrl;
        logger.info("微信公众号支付异步回调地址：" + notifyUrl);
        // 交易类型 手机H5支付、公众号支付trade_type都传"MWEB"
        String tradeType = orderData.getTradeType();

        TreeMap<String, String> requestWXMap = new TreeMap<>();
        requestWXMap.put("appid", appid);
        requestWXMap.put("mch_id", mch_id);
        requestWXMap.put("nonce_str", nonce_str);
        requestWXMap.put("body", body);
        requestWXMap.put("out_trade_no", outTradeNo);
        requestWXMap.put("total_fee", account);
        requestWXMap.put("spbill_create_ip", ip);
        requestWXMap.put("notify_url", notifyUrl);
        requestWXMap.put("trade_type", tradeType);
        requestWXMap.put("openid", orderData.getOpenId());

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
        logs.setClientType("JSAPI");

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
                //公众号支付(微信内H5调起支付)
                if (PropertiesUtil.getPayInfoValue("mode.tradeType.JSAPI").equals(orderData.getTradeType())) {
                    Map<String, String> returnZmall = new HashMap<>();
                    returnZmall.put("appId", appid);
                    returnZmall.put("timeStamp", String.valueOf(System.currentTimeMillis()));
                    returnZmall.put("nonceStr", Util.getRandomStringByLength(32));
                    returnZmall.put("package", "prepay_id=" + responsePayMapFromWX.get("prepay_id"));
                    returnZmall.put("signType", WecatConstants.MD5);
                    //将传送给zmall的数据生成签名
                    try {
                        sign = WXPayUtil.generateSignature(returnZmall, key);
                    } catch (Exception e) {
                        logger.error("WXPayUtil-generateSignature-fail", e);
                    }
                    returnZmall.put("paySign", sign);
                    logger.info("微信公众号支付请求成功：prepay_id=" + responsePayMapFromWX.get("prepay_id"));
                    logs.setSuccess(true);
                    logs.setRespMsgCode(responsePayMapFromWX.get("return_code"));
                    logs.setRequestBody(JSONObject.toJSONString(returnZmall));
                    messageSender.sendMsg(logs);
                    List<Map<String, String>> list = new ArrayList<>();
                    list.add(returnZmall);
                    return new ReturnData(responsePayMapFromWX.get("return_code"), "", list, true);
                } else {
                    logs.setRespMsgCode(responsePayMapFromWX.get("err_code_des"));
                    logs.setRespMsg(responsePayMapFromWX.get("err_code"));
                    logs.setSuccess(false);
                    messageSender.sendMsg(logs);
                    return new ReturnData(responsePayMapFromWX.get("err_code"), responsePayMapFromWX.get("err_code_des"), false);
                }
            } else {
                logger.info("微信公众号支付请求失败(业务失败)：" + responsePayMapFromWX.get("err_code") + "-err_code_des-" + responsePayMapFromWX.get("err_code_des"));
                logs.setRespMsgCode(responsePayMapFromWX.get("err_code_des"));
                logs.setRespMsg(responsePayMapFromWX.get("err_code"));
                logs.setSuccess(false);
                messageSender.sendMsg(logs);
                return new ReturnData(responsePayMapFromWX.get("err_code"), responsePayMapFromWX.get("err_code_des"), false);
            }
        } else {
            logger.info("微信公众号支付请求失败(请求信息有误)" + responsePayMapFromWX.get("err_code") + "-err_code_des-" + responsePayMapFromWX.get("return_msg"));
            logs.setRespMsgCode(responsePayMapFromWX.get("return_code"));
            logs.setRespMsg(responsePayMapFromWX.get("return_msg"));
            logs.setSuccess(false);
            messageSender.sendMsg(logs);
            return new ReturnData(responsePayMapFromWX.get("return_code"), responsePayMapFromWX.get("return_msg"), false);
        }
    }

    /**
     * 根据zmall传送过来的Code,调用微信接口获取access_token、openId等信息,并将openId等信息传给Zmall
     *
     * @param code
     */
    @Override
    public ReturnData getOpenIdByCode(String code) {
        //返回前台信息
        ReturnData returnData = new ReturnData();
        List<JSONObject> list = new ArrayList<>();
        //使用String.format将字符串值拼接在url中
        String requestUrl = String.format(
                PropertiesUtil.getHpayValue("wechat.getOpenIdUrl"),
                PropertiesUtil.getHpayValue("wechat.appId"),
                PropertiesUtil.getHpayValue("wechat.secret").trim(),
                code,
                PropertiesUtil.getHpayValue("wechat.grantType").trim());
        ThirdPartyApiLogs requestWeChatLogs = new ThirdPartyApiLogs();
        Date requestWeChatTime = new Date();
        requestWeChatLogs.setRequestTime(requestWeChatTime);
        requestWeChatLogs.setTarget("WECHAT");
        requestWeChatLogs.setRequestBody(requestUrl);
        requestWeChatLogs.setRequestMethod("getOpenIdByCode");
        requestWeChatLogs.setClientType("PC");
        JSONObject weChatResponse = null;
        try {
            //请求WeChat
            weChatResponse = SendReq.get(requestUrl, null);
            Date responseWeChatTime = new Date();
            requestWeChatLogs.setResponseBody(weChatResponse);
            requestWeChatLogs.setResponseTime(responseWeChatTime);
            requestWeChatLogs.setDuring(responseWeChatTime.getTime() - requestWeChatTime.getTime());
            if ("".equals(weChatResponse.getString("errcode")) || null == weChatResponse.getString("errcode")) {
                //将微信返回信息添加至list
                list.add(weChatResponse);
                returnData.setSuccess(true);
                returnData.setMsgCode("success");
                returnData.setResp(list);
                requestWeChatLogs.setSuccess(true);
            } else {
                returnData.setSuccess(false);
                returnData.setMsgCode(weChatResponse.getString("errcode"));
                returnData.setMsg(weChatResponse.getString("errmsg"));
                requestWeChatLogs.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("请求微信接口获取access_token、openId等信息失败", e);
            requestWeChatLogs.setSuccess(false);
            requestWeChatLogs.setExceptionMsg(e.getMessage());
            returnData.setSuccess(false);
            returnData.setMsgCode("请求微信接口获取access_token、openId等信息失败");
        }
        //保存请求WeChat接口日志信息
        messageSender.sendMsg(requestWeChatLogs);
        return returnData;
    }
}
