package com.hand.hap.cloud.hpay.services.pcServices.union.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.entities.OutBoundLogs;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.AcpService;
import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.http.UnionPayHttp;
import com.hand.hap.cloud.hpay.services.pcServices.union.service.IUnionPayService;
import com.hand.hap.cloud.hpay.services.toZmall.IPostTargetSystemService;
import com.hand.hap.cloud.hpay.utils.DateFormatUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.hand.hap.cloud.hpay.services.constants.UnionConstants.*;
import static com.hand.hap.cloud.hpay.services.pcServices.union.sdk.SDKConfig.getConfig;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
@Service
public class UnionPayServiceImpl implements IUnionPayService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${union.notifyUrl}")
    private String unionNotifyUrl;

    @Value("${zmall.notifyUrl}")
    private String zmallNotifyUrl;

    @Value("${hmall.notifyUrl}")
    private String hmallNotifyUrl;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private IPostTargetSystemService postZmallService;

    /**
     * 银联pc支付
     *
     * @param orderData 支付信息
     * @return string
     */
    @Override
    public String pcUnion(OrderData orderData) {

        //加载配置文件
        Map<String, String> requestUnionMap = new HashMap<>();
        // 版本号
        requestUnionMap.put(PARAM_VERSION, UnionPayHttp.version);
        // 编码方式
        requestUnionMap.put(PARAM_ENCODING, UnionPayHttp.encoding);
        // 签名方法 01 RSA
        requestUnionMap.put(PARAM_SIGNMETHOD, "01");
        // 交易类型 01-消费
        requestUnionMap.put(PARAM_TXNTYPE, "01");
        // 交易子类型 01:自助消费 02:订购 03:分期付款
        requestUnionMap.put(PARAM_TXNSUBTYPE, "01");
        // 业务类型
        requestUnionMap.put(PARAM_BIZTYPE, "000201");
        // 渠道类型，07-PC，08-手机
        requestUnionMap.put(PARAM_CHANNELTYPE, "07");
        // 前台通知地址 ，控件接入方式无作用
        requestUnionMap.put(PARAM_FRONTURL, orderData.getReturnUrl());
        // 后台通知地址
        requestUnionMap.put(PARAM_BACKURL, unionNotifyUrl);
        // 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
        requestUnionMap.put(PARAM_ACCESSTYPE, "0");
        // 商户号码，请改成自己的商户号
        requestUnionMap.put(PARAM_MERID, PropertiesUtil.getHpayValue("union.merId"));
        // 订单发送时间，取系统时间
        requestUnionMap.put(PARAM_TXNTIME, DateFormatUtil.getCurrentTime());
        // 商户订单号，8-40位数字字母  (区分测试环境和正式环境订单号)
        requestUnionMap.put(PARAM_ORDERID, orderData.getOutTradeNo());
        // 交易金额，单位分
        requestUnionMap.put(PARAM_TXNAMT, orderData.getAmount());
        // 交易币种
        requestUnionMap.put(PARAM_CURRENCYCODE, "156");
        //透传字段
        requestUnionMap.put(PARAM_REQRESERVED, orderData.getTradeType());
        //多密钥签名，存日志
        Map<String, String> submitData = AcpService.sign(
                requestUnionMap,
                PropertiesUtil.getHpayValue("union.certPath"),
                PropertiesUtil.getHpayValue("union.certPwd"),
                UnionPayHttp.encoding);
        // 交易请求url 从配置文件读取
        String requestFrontUrl = getConfig().getFrontRequestUrl();
        logger.info("请求银联支付参数：" + submitData);
        //创建html
        String responseHtmlFromUnion = UnionPayHttp.createHtml(requestFrontUrl, submitData);
        ThirdPartyApiLogs thirdPartyApiLogs = new ThirdPartyApiLogs();
        thirdPartyApiLogs.setRequestTime(new Date());
        thirdPartyApiLogs.setRequestMethod(PropertiesUtil.getPayInfoValue("paymentTypeCode.pay"));
        thirdPartyApiLogs.setTarget(PropertiesUtil.getPayInfoValue("mode.unionpay"));
        thirdPartyApiLogs.setClientType(PropertiesUtil.getPayInfoValue("mode.tradeType.pc"));
        thirdPartyApiLogs.setRequestBody(responseHtmlFromUnion);
        messageSender.sendMsg(thirdPartyApiLogs);
        logger.info("银联支付表单", responseHtmlFromUnion);
        return responseHtmlFromUnion;
    }

    /**
     * 银联回调
     *
     * @param request  request
     * @param response response
     */
    @Override
    public void unionNotify(HttpServletRequest request, HttpServletResponse response) {

        OutBoundLogs logs = new OutBoundLogs();
        logs.setRequestTime(new Date());
        logs.setUsage("UNION-PAY");

        //判断回调参数是否为空
        if (request.getParameterMap() == null) {
            logs.setMessage("银联回调参数为空");
            logs.setSuccess(false);
            logger.info("银联回调参数为空");
            try {
                response.getWriter().print("fail");
            } catch (IOException e) {
                logger.error("输出至银联失败", e);
            }
            return;
        }
        //取出后台参数
        String encoding = request.getParameter(PARAM_ENCODING);

        Map<String, String> notifyMap = getAllRequestParam(request);

        Map<String, String> valideNotifyData = null;

        if (null != notifyMap && !notifyMap.isEmpty()) {
            Iterator<Map.Entry<String, String>> it = notifyMap.entrySet().iterator();
            valideNotifyData = new HashMap<>(notifyMap.size());
            while (it.hasNext()) {
                Map.Entry<String, String> e = it.next();
                String key = e.getKey();
                String value = e.getValue();
                try {
                    value = new String(value.getBytes(encoding), encoding);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                valideNotifyData.put(key, value);
            }
        }
        //处理通知参数，进行签名验证
        boolean signResult = AcpService.validate(valideNotifyData, encoding);

        logger.info("银联回调签名验证结果：" + signResult);

        HashMap<String, Object> requestZmallMap = new HashMap();
        //判断签名是否成功

        if (signResult) {
            if (PARAM_RESPCODE_00.equals(notifyMap.get(PARAM_RESPCODE)) && null != notifyMap.get(PARAM_RESPCODE)) {
                requestZmallMap.put("mode", PropertiesUtil.getPayInfoValue("mode.unionpay"));
                requestZmallMap.put("tradeType", valideNotifyData.get(PARAM_REQRESERVED));
                requestZmallMap.put("tradeResultCode", notifyMap.get(PARAM_RESPCODE));
                requestZmallMap.put("tradeDescription", valideNotifyData.get(PARAM_RESPMSG));
                requestZmallMap.put("notifyId", valideNotifyData.get(PARAM_TRACENO));
                requestZmallMap.put("notifyTime", DateFormatUtil.getCurrentTime());
                requestZmallMap.put("tradeCreateTime", valideNotifyData.get(PARAM_TXNTIME));
                requestZmallMap.put("tradePayTime", DateFormatUtil.getCurrentTime());
                requestZmallMap.put("totalAmount", valideNotifyData.get(PARAM_TXNAMT));
                requestZmallMap.put("buyerPayAmount", valideNotifyData.get(PARAM_TXNAMT));
                requestZmallMap.put("tradeNo", valideNotifyData.get(PARAM_QUERYID));
                requestZmallMap.put("outTradeNo", valideNotifyData.get(PARAM_ORDERID));
                logger.info("银联支付成功-", requestZmallMap);
                logs.setMessage("银联支付成功");
            } else {
                requestZmallMap.put("tradeResultCode", PropertiesUtil.getPayInfoValue("failed"));
                requestZmallMap.put("tradeDescription", valideNotifyData.get(PARAM_RESPMSG));
                requestZmallMap.put("mode", PropertiesUtil.getPayInfoValue("mode.unionpay"));
                requestZmallMap.put("tradeType", valideNotifyData.get(PARAM_REQRESERVED));
                requestZmallMap.put("notifyId", valideNotifyData.get(PARAM_TRACENO));
                requestZmallMap.put("notifyTime", DateFormatUtil.getCurrentTime());
                requestZmallMap.put("tradeCreateTime", valideNotifyData.get(PARAM_TXNTIME));
                requestZmallMap.put("tradePayTime", DateFormatUtil.getCurrentTime());
                requestZmallMap.put("totalAmount", valideNotifyData.get(PARAM_TXNAMT));
                requestZmallMap.put("buyerPayAmount", valideNotifyData.get(PARAM_TXNAMT));
                requestZmallMap.put("tradeNo", valideNotifyData.get(PARAM_QUERYID));
                requestZmallMap.put("outTradeNo", valideNotifyData.get(PARAM_ORDERID));
                logs.setMessage("银联支付失败-errorCode-" + notifyMap.get(PARAM_RESPCODE) + "-errorMsg-" + valideNotifyData.get(PARAM_RESPMSG));
                logger.info("银联支付失败-errorCode-", notifyMap.get(PARAM_RESPCODE) + "-errorMsg-" + valideNotifyData.get(PARAM_RESPMSG));
            }
            String url = "";
            try {
                //服务销售单支付信息回调给hmall
                if (valideNotifyData.get(PARAM_ORDERID).startsWith("SV")) {
                    url = hmallNotifyUrl;
                    String content = JSON.toJSONString(requestZmallMap);
                    logs.setTargetSystem("HMALL");
                    logs.setRequestBody(content);
                    logs.setRequestAddr(hmallNotifyUrl);
                    JSONObject responseFromHmall = postZmallService.postToTargetSystem(hmallNotifyUrl, content, null);
                    logs.setResponseBody(JSON.toJSONString(responseFromHmall));
                    Date responseTime = new Date();
                    logs.setResponseTime(responseTime);
                    logs.setDuring(responseTime.getTime() - logs.getRequestTime().getTime());
                    //记录日志
                    if (!(Boolean) responseFromHmall.get("success")) {
                        logs.setSuccess(false);
                        logger.error("请求hmall失败，业务失败-" + responseFromHmall.getString("msg"));
                        logs.setMessage("请求hmall失败，业务失败-" + responseFromHmall.getString("msg"));
                        messageSender.sendMsg(logs);
                        response.getWriter().print("fail");
                        return;
                    }
                } else {
                    url = zmallNotifyUrl;
                    //订单支付信息回调给zmall
                    String content = JSON.toJSONString(requestZmallMap);
                    logs.setTargetSystem("ZMALL");
                    logs.setRequestBody(content);
                    logs.setRequestAddr(zmallNotifyUrl);
                    JSONObject responseFromZmall = postZmallService.postToTargetSystem(zmallNotifyUrl, content, null);
                    logs.setResponseBody(JSON.toJSONString(responseFromZmall));
                    Date responseTime = new Date();
                    logs.setResponseTime(responseTime);
                    logs.setDuring(responseTime.getTime() - logs.getRequestTime().getTime());
                    //记录日志
                    if (StringUtils.isEmpty(responseFromZmall.getString("code")) || !"1".equals(responseFromZmall.getString("code"))) {
                        logs.setSuccess(false);
                        logs.setMessage(logs.getMessage() + "银联支付成功，推送zmall失败(数据有误)-errorCode-" + responseFromZmall.getString("code") + "errorMsg-" + responseFromZmall.getString("message"));
                        messageSender.sendMsg(logs);
                        response.getWriter().print("fail");
                        logger.error("银联支付成功，推送zmall失败(数据有误)-errorCode-" + responseFromZmall.getString("code") + "errorMsg-" + responseFromZmall.getString("message"));
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("银联支付成功，推送目标系统" + url + "失败(异常)", e);
                logs.setMessage(logs.getMessage() + " 银联支付成功，推送目标系统" + url + "失败(异常)");
                logs.setExceptionStack(e.getStackTrace());
                logs.setSuccess(false);
                messageSender.sendMsg(logs);
                try {
                    response.getWriter().print("fail");
                } catch (IOException e1) {
                    logger.error("输出至银联失败", e1);
                }
                return;
            }
            logs.setSuccess(true);
            messageSender.sendMsg(logs);
            try {
                response.getWriter().print("ok");
            } catch (IOException e) {
                logger.error("输出至银联失败", e);
            }
        } else {
            try {
                response.getWriter().print("fail");
            } catch (IOException e) {
                logger.error("输出至银联失败", e);
            }
            logs.setSuccess(false);
            logs.setMessage("银联回调签名验证失败");
            messageSender.sendMsg(logs);
            logger.error("银联回调签名验证失败");
        }
    }

    /**
     * 银联通知参数进行遍历，封装成Map<String,String>[key=value]形式
     *
     * @param request 银联通知参数
     * @return map
     */
    @Override
    public Map<String, String> getAllRequestParam(HttpServletRequest request) {
        Map<String, String> res = new HashMap<>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                //在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                //System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }
}
