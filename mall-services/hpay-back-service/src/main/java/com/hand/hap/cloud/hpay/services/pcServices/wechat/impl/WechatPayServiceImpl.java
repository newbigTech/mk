package com.hand.hap.cloud.hpay.services.pcServices.wechat.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.OutBoundLogs;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.pcServices.wechat.IPCWechatPayService;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.HttpUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.PayCommonUtil;
import com.hand.hap.cloud.hpay.services.phoneServices.wechat.util.WXPayUtil;
import com.hand.hap.cloud.hpay.services.toZmall.IPostTargetSystemService;
import com.hand.hap.cloud.hpay.utils.DateFormatUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import com.hand.hap.cloud.hpay.utils.Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static com.hand.hap.cloud.hpay.utils.XmlUtil.xml2map;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.pcServices.wechat.impl
 * @Description
 * @date 2017/8/21
 */
@Service
public class WechatPayServiceImpl implements IPCWechatPayService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MessageSender messageSender;
    @Value("${wechat.notifyUrl}")
    private String wechatNotifyUrl;
    @Value("${zmall.notifyUrl}")
    private String zmallNotifyUrl;
    @Value("${hmall.notifyUrl}")
    private String hmallNotifyUrl;
    @Autowired
    private IPostTargetSystemService postZmallService;

    /**
     * wechatPC支付
     *
     * @param request   request
     * @param resp      resp
     * @param orderData orderData
     * @return returnData
     * @throws Exception exception
     */
    @Override
    public ReturnData pcWechat(HttpServletRequest request, HttpServletResponse resp, OrderData orderData) throws Exception {

        // 账号信息
        // appid
        String appId = PropertiesUtil.getHpayValue("wechat.appId");
        // 商业号
        String mchId = PropertiesUtil.getHpayValue("wechat.mchId");
        // key
        String key = PropertiesUtil.getHpayValue("wechat.key");
        //随机串
        String nonceStr = Util.getRandomStringByLength(32);
        // 订单金额
        String amount = orderData.getAmount();
        //获取IP
        String ip = Util.getIpAddress(request);
        // 商品名称
        String body = orderData.getDescription();
        // 订单号
        String outTradeNo = orderData.getOutTradeNo();

        // 回调接口
        String notifyUrl = wechatNotifyUrl;
        String tradeType = orderData.getTradeType();
        SortedMap<String, String> requestPayMapToWX;
        requestPayMapToWX = new TreeMap<>();

        requestPayMapToWX.put("appid", appId);
        requestPayMapToWX.put("mch_id", mchId);
        requestPayMapToWX.put("nonce_str", nonceStr);
        requestPayMapToWX.put("body", body);
        requestPayMapToWX.put("out_trade_no", outTradeNo);
        requestPayMapToWX.put("total_fee", amount);
        requestPayMapToWX.put("spbill_create_ip", ip);
        requestPayMapToWX.put("notify_url", notifyUrl);
        requestPayMapToWX.put("trade_type", tradeType);

        //签名
        String sign = WXPayUtil.generateSignature(requestPayMapToWX, key);
        requestPayMapToWX.put("sign", sign);
        logger.info("微信request-requestMap==》" + requestPayMapToWX.toString());

        //请求参数
        String requestPayXMLToWX = PayCommonUtil.getRequestXml(requestPayMapToWX);
        logger.info("微信请求参数==>" + requestPayXMLToWX);

        //记录日志 请求
        ThirdPartyApiLogs logs = new ThirdPartyApiLogs();
        Date requestTime = new Date();
        logs.setRequestTime(requestTime);
        logs.setTarget("WECHAT");
        logs.setRequestBody(requestPayXMLToWX);
        logs.setRequestMethod("PAY");
        logs.setClientType("PC");

        //返回参数
        String responsePayXmlFromWX = HttpUtil.postData(PropertiesUtil.getHpayValue("wechat.unifiedPayApi"), requestPayXMLToWX);
        logger.info("微信response-responseXml" + responsePayXmlFromWX);

        Date responseTime = new Date();
        logs.setResponseTime(responseTime);
        logs.setResponseBody(responsePayXmlFromWX);
        logs.setDuring(responseTime.getTime() - requestTime.getTime());


        //转换为map
        Map<String, String> responsePayMapFromWX = xml2map(responsePayXmlFromWX, false);
        logger.info("微信response-map" + responsePayMapFromWX);

        //签名验证
        if (!WXPayUtil.isSignatureValid(responsePayMapFromWX, key)) {
            logs.setSuccess(false);
            logs.setRespMsg("签名失败");
            messageSender.sendMsg(logs);
            return new ReturnData(PropertiesUtil.getPayInfoValue("fail"), "签名失败", false);
        }

        if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responsePayMapFromWX.get("return_code"))) {
            if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(responsePayMapFromWX.get("result_code"))) {

                String url = responsePayMapFromWX.get("code_url");
                if (null != url && !"".equals(url)) {
                    logger.info("微信PC支付请求成功：" + url);
                    logs.setSuccess(true);
                    logs.setRespMsgCode(responsePayMapFromWX.get("return_code"));
                    messageSender.sendMsg(logs);

                    //开发环境临时支付
                    String testMode = System.getProperty("testmode");
                    if ("true".equals(testMode)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    temporaryWeChatPayment(orderData);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    return new ReturnData(responsePayMapFromWX.get("return_code"), url, true);
                }
                logs.setRespMsg(responsePayMapFromWX.get("err_code_des"));
                logs.setRespMsgCode(responsePayMapFromWX.get("err_code"));
                logs.setSuccess(false);
                messageSender.sendMsg(logs);
                logger.info("(url为空)：" + responsePayMapFromWX.get("err_code") + "-err_code_des-" + responsePayMapFromWX.get("err_code_des"));
                return new ReturnData(responsePayMapFromWX.get("err_code"), responsePayMapFromWX.get("err_code_des"), false);
            }
            logs.setSuccess(false);
            logs.setRespMsg(responsePayMapFromWX.get("err_code_des"));
            logs.setRespMsgCode(responsePayMapFromWX.get("err_code"));
            messageSender.sendMsg(logs);
            logger.info("微信PC支付请求失败(业务失败)：" + responsePayMapFromWX.get("err_code") + "-err_code_des-" + responsePayMapFromWX.get("err_code_des"));
            return new ReturnData(responsePayMapFromWX.get("err_code"), responsePayMapFromWX.get("err_code_des"), false);
        }
        logs.setSuccess(false);
        logs.setRespMsg(responsePayMapFromWX.get("return_msg"));
        logs.setRespMsgCode(responsePayMapFromWX.get("return_code"));
        messageSender.sendMsg(logs);
        logger.info("微信PC支付请求失败(请求信息有误)" + responsePayMapFromWX.get("return_code") + "-err_code_des-" + responsePayMapFromWX.get("return_msg"));
        return new ReturnData(responsePayMapFromWX.get("return_code"), responsePayMapFromWX.get("return_msg"), false);
    }


    /**
     * 开发环境临时支付回调
     * <p>
     * 为解决开发环境创造测试数据问题,下单无需扫码立即返回支付结果
     * 模拟微信支付回调功能,跳过签名校验,完成支付
     *
     * @param orderData
     */
    private void temporaryWeChatPayment(OrderData orderData) {
        OutBoundLogs logs = new OutBoundLogs();
        logs.setRequestTime(new Date());
        logs.setUsage("WECHAT-PAY");

        logger.info("模拟微信支付异步回调开始");
        // 读取参数
        Map<String, Object> requestZmallMap = new HashMap<>();
        // 账号信息
        String key = PropertiesUtil.getHpayValue("wechat.key"); // key
        // 判断签名是否正确
        String responseWechatXML;
        // 处理业务开始
        if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase("success")) {
            requestZmallMap.put("tradeResultCode", PropertiesUtil.getPayInfoValue("success"));
            requestZmallMap.put("tradeDescription", "SUCCESS");
            requestZmallMap.put("mode", PropertiesUtil.getPayInfoValue("mode.wechatpay"));
            requestZmallMap.put("tradeType", "JSAPI");
            //微信支付订单号 随机生成
            requestZmallMap.put("notifyId", "4200000030" + DateFormatUtil.getCurrentTime() + (int) ((Math.random() * 9 + 1) * 1000));
            requestZmallMap.put("notifyTime", DateFormatUtil.getCurrentTime());
            requestZmallMap.put("tradeCreateTime", DateFormatUtil.getCurrentTime());
            requestZmallMap.put("tradePayTime", DateFormatUtil.getCurrentTime());
            requestZmallMap.put("totalAmount", orderData.getAmount());
            requestZmallMap.put("buyerPayAmount", orderData.getAmount());
            //微信支付订单号 随机生成
            requestZmallMap.put("tradeNo", "4200000030" + DateFormatUtil.getCurrentTime() + (int) ((Math.random() * 9 + 1) * 1000));
            requestZmallMap.put("outTradeNo", orderData.getOutTradeNo());
            logger.info("支付成功-请求zmall-" + requestZmallMap);
            // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
            logs.setMessage("支付成功-请求zmall-" + requestZmallMap);
            responseWechatXML = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
        }
        String url = "";
        try {
            //服务销售单支付信息回调给hmall
            if (orderData.getOutTradeNo().startsWith("SV")) {
                url = hmallNotifyUrl;
                String content = JSON.toJSONString(requestZmallMap);
                logs.setTargetSystem("HMALL");
                logs.setRequestBody(content);
                logs.setRequestAddr(hmallNotifyUrl);
                JSONObject responseFromHmall = postZmallService.postToTargetSystem(hmallNotifyUrl, content, null);
                logs.setResponseBody(JSONObject.toJSONString(responseFromHmall));
                Date responseTime = new Date();
                logs.setResponseTime(responseTime);
                logs.setDuring(responseTime.getTime() - logs.getRequestTime().getTime());
                if (!(Boolean) responseFromHmall.get("success")) {
//                        logs.setSuccess(false);
                    logger.error("请求hmall失败，业务失败-" + responseFromHmall.getString("msg"));
//                        logs.setMessage("请求hmall失败，业务失败-" + responseFromHmall.getString("msg"));
//                        messageSender.sendMsg(logs);
//                        responseWechatXML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[推送hmall失败(业务失败)]]></return_msg>" + "</xml> ";
                } else {
                    logger.info("请求hmall成功，业务成功");
                }
            } else {
                //订单支付信息回调给zmall
                url = zmallNotifyUrl;
                String content = JSON.toJSONString(requestZmallMap);
//                    logs.setRequestBody(content);
//                    logs.setTargetSystem("ZMALL");
//                    logs.setRequestAddr(zmallNotifyUrl);
                JSONObject responseFromZmall = postZmallService.postToTargetSystem(url, content, null);
//                    logs.setResponseBody(JSONObject.toJSONString(responseFromZmall));
                Date responseTime = new Date();
//                    logs.setResponseTime(responseTime);
//                    logs.setDuring(responseTime.getTime() - logs.getRequestTime().getTime());
                if (StringUtils.isEmpty(responseFromZmall.getString("code")) || !"1".equals(responseFromZmall.getString("code"))) {
//                        logs.setSuccess(false);
//                        logs.setMessage(logs.getMessage() + "  推送zmall失败(业务失败)-errorCode-" + responseFromZmall.getString("code") + "-errorMsg-" + responseFromZmall.getString("msg"));
//                        messageSender.sendMsg(logs);
//                        responseWechatXML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[推送zmall失败(业务失败)]]></return_msg>" + "</xml> ";
                    logger.error("推送zmall失败(业务失败)-errorCode-", responseFromZmall.getString("code") + "-errorMsg-" + responseFromZmall.getString("msg"));
                } else {
                    logger.info("推送zmall成功(业务成功) 微信支付异步回调结束");
                }
            }
        } catch (Exception e) {
            responseWechatXML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[推送目标系统" + url + "失败(请求失败)]]></return_msg></xml> ";
//                logs.setMessage(logs.getMessage() + "微信支付成功，推送目标系统" + url + "失败(异常)");
//                logs.setExceptionStack(e.getStackTrace());
//                logs.setSuccess(false);
//                messageSender.sendMsg(logs);
            logger.error("微信支付失败，推送目标系统" + url + "失败(异常)", e);
        }
//            logs.setSuccess(true);
//            messageSender.sendMsg(logs);
    }

    /**
     * 微信异步回调请求，做参数验证
     *
     * @param request  request
     * @param response response
     */
    @Override
    public void weChatNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        OutBoundLogs logs = new OutBoundLogs();
        logs.setRequestTime(new Date());
        logs.setUsage("WECHAT-PAY");

        logger.info("微信支付异步回调开始");
        response.setContentType("text/xml; charset=utf-8");
        // 读取参数
        Map<String, Object> requestZmallMap = new HashMap<>();
        InputStream inputStream;
        StringBuilder sb = new StringBuilder();
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, String> responseNotifyMap = xml2map(sb.toString(), false);

        // 过滤空 设置 TreeMap
        SortedMap<String, String> respNotifyMap = new TreeMap<>();
        Iterator it = responseNotifyMap.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = responseNotifyMap.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            respNotifyMap.put(parameter, v);
        }
        // 账号信息
        String key = PropertiesUtil.getHpayValue("wechat.key"); // key
        // 判断签名是否正确
        String responseWechatXML;
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        if (WXPayUtil.isSignatureValid(respNotifyMap, key)) {
            // 处理业务开始
            if (PropertiesUtil.getPayInfoValue("success").equalsIgnoreCase(respNotifyMap.get("result_code"))) {
                requestZmallMap.put("tradeResultCode", PropertiesUtil.getPayInfoValue("success"));
                requestZmallMap.put("tradeDescription", respNotifyMap.get("result_code"));
                requestZmallMap.put("mode", PropertiesUtil.getPayInfoValue("mode.wechatpay"));
                requestZmallMap.put("tradeType", respNotifyMap.get("trade_type"));
                requestZmallMap.put("notifyId", respNotifyMap.get("transaction_id"));
                requestZmallMap.put("notifyTime", DateFormatUtil.getCurrentTime());
                requestZmallMap.put("tradeCreateTime", respNotifyMap.get("time_end"));
                requestZmallMap.put("tradePayTime", respNotifyMap.get("time_end"));
                requestZmallMap.put("totalAmount", respNotifyMap.get("total_fee"));
                requestZmallMap.put("buyerPayAmount", respNotifyMap.get("total_fee"));
                requestZmallMap.put("tradeNo", respNotifyMap.get("transaction_id"));
                requestZmallMap.put("outTradeNo", respNotifyMap.get("out_trade_no"));
                logger.info("支付成功-请求zmall-" + requestZmallMap);
                // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                logs.setMessage("支付成功-请求zmall-" + requestZmallMap);
                responseWechatXML = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
            } else {
                requestZmallMap.put("tradeResultCode", PropertiesUtil.getPayInfoValue("failed"));
                requestZmallMap.put("tradeDescription", respNotifyMap.get("err_code_des"));
                requestZmallMap.put("mode", PropertiesUtil.getPayInfoValue("mode.wechatpay"));
                requestZmallMap.put("tradeType", respNotifyMap.get("trade_type"));
                requestZmallMap.put("notifyId", respNotifyMap.get("transaction_id"));
                requestZmallMap.put("notifyTime", DateFormatUtil.getCurrentTime());
                requestZmallMap.put("tradeCreateTime", respNotifyMap.get("time_end"));
                requestZmallMap.put("tradePayTime", respNotifyMap.get("time_end"));
                requestZmallMap.put("totalAmount", respNotifyMap.get("total_fee"));
                requestZmallMap.put("buyerPayAmount", respNotifyMap.get("total_fee"));
                requestZmallMap.put("tradeNo", respNotifyMap.get("transaction_id"));
                requestZmallMap.put("outTradeNo", respNotifyMap.get("out_trade_no"));
                logger.error("支付失败,错误信息：" + respNotifyMap.get("err_code"));
                logs.setMessage("支付失败,错误信息：" + respNotifyMap.get("err_code"));
                responseWechatXML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
                out.write(responseWechatXML.getBytes());
                out.flush();
                out.close();
                return;
            }
            String url = "";
            try {
                //服务销售单支付信息回调给hmall
                if (respNotifyMap.get("out_trade_no").startsWith("SV")) {
                    url = hmallNotifyUrl;
                    String content = JSON.toJSONString(requestZmallMap);
                    logs.setTargetSystem("HMALL");
                    logs.setRequestBody(content);
                    logs.setRequestAddr(hmallNotifyUrl);
                    JSONObject responseFromHmall = postZmallService.postToTargetSystem(hmallNotifyUrl, content, null);
                    logs.setResponseBody(JSONObject.toJSONString(responseFromHmall));
                    Date responseTime = new Date();
                    logs.setResponseTime(responseTime);
                    logs.setDuring(responseTime.getTime() - logs.getRequestTime().getTime());
                    if (!(Boolean) responseFromHmall.get("success")) {
                        logs.setSuccess(false);
                        logger.error("请求hmall失败，业务失败-" + responseFromHmall.getString("msg"));
                        logs.setMessage("请求hmall失败，业务失败-" + responseFromHmall.getString("msg"));
                        messageSender.sendMsg(logs);
                        responseWechatXML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[推送hmall失败(业务失败)]]></return_msg>" + "</xml> ";
                    }
                } else {
                    //订单支付信息回调给zmall
                    url = zmallNotifyUrl;
                    String content = JSON.toJSONString(requestZmallMap);
                    logs.setRequestBody(content);
                    logs.setTargetSystem("ZMALL");
                    logs.setRequestAddr(zmallNotifyUrl);
                    JSONObject responseFromZmall = postZmallService.postToTargetSystem(zmallNotifyUrl, content, null);
                    logs.setResponseBody(JSONObject.toJSONString(responseFromZmall));
                    Date responseTime = new Date();
                    logs.setResponseTime(responseTime);
                    logs.setDuring(responseTime.getTime() - logs.getRequestTime().getTime());
                    if (StringUtils.isEmpty(responseFromZmall.getString("code")) || !"1".equals(responseFromZmall.getString("code"))) {
                        logs.setSuccess(false);
                        logs.setMessage(logs.getMessage() + "  推送zmall失败(业务失败)-errorCode-" + responseFromZmall.getString("code") + "-errorMsg-" + responseFromZmall.getString("msg"));
                        messageSender.sendMsg(logs);
                        responseWechatXML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[推送zmall失败(业务失败)]]></return_msg>" + "</xml> ";
                        logger.error("推送zmall失败(业务失败)-errorCode-", responseFromZmall.getString("code") + "-errorMsg-" + responseFromZmall.getString("msg"));
                    }
                }
            } catch (Exception e) {
                responseWechatXML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[推送目标系统" + url + "失败(请求失败)]]></return_msg></xml> ";
                logs.setMessage(logs.getMessage() + "微信支付成功，推送目标系统" + url + "失败(异常)");
                logs.setExceptionStack(e.getStackTrace());
                logs.setSuccess(false);
                messageSender.sendMsg(logs);
                logger.error("微信支付成功，推送目标系统" + url + "失败(异常)", e);
            }
            logger.info("微信支付异步回调结束");
            logs.setSuccess(true);
            messageSender.sendMsg(logs);
        } else {
            responseWechatXML = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名失败]]></return_msg></xml> ";
            logs.setSuccess(false);
            logs.setMessage("回调验证签名失败");
            messageSender.sendMsg(logs);
            logger.error("回调验证签名失败");
        }
        // 处理业务完毕
        logger.info(responseWechatXML);
        out.write(responseWechatXML.getBytes());
        out.flush();
        out.close();
    }
}
