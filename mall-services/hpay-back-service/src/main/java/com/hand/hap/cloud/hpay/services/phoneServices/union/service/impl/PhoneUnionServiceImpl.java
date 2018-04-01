package com.hand.hap.cloud.hpay.services.phoneServices.union.service.impl;

import com.hand.hap.cloud.hpay.data.OrderData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.phoneServices.union.sdk.http.UnionPayHttp;
import com.hand.hap.cloud.hpay.services.phoneServices.union.service.IPhoneUnionService;
import com.hand.hap.cloud.hpay.utils.DateFormatUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hand.hap.cloud.hpay.services.phoneServices.union.sdk.SDKConfig.getConfig;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.hpay.services.phoneServices.alipay.impl
 * @Description
 * @date 2017/8/3
 */
@Service
public class PhoneUnionServiceImpl implements IPhoneUnionService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${union.notifyUrl}")
    private String unionNotifyUrl;

    @Autowired
    private MessageSender messageSender;

    /**
     * 银联h5支付
     *
     * @param request   request
     * @param resp      resp
     * @param orderData 订单数据
     * @return string
     */
    @Override
    public String phoneH5Unionpay(HttpServletRequest request, HttpServletResponse resp, OrderData orderData) {

        //加载配置文件
        HashMap<String, String> requestUnionMap = new HashMap<>();
        // 版本号
        requestUnionMap.put("version", UnionPayHttp.version);
        // 编码方式
        requestUnionMap.put("encoding", UnionPayHttp.encoding);
        // 签名方法 01 RSA
        requestUnionMap.put("signMethod", "01");
        // 交易类型 01-消费
        requestUnionMap.put("txnType", "01");
        // 交易子类型 01:自助消费 02:订购 03:分期付款
        requestUnionMap.put("txnSubType", "01");
        // 业务类型
        requestUnionMap.put("bizType", "000201");
        // 渠道类型，07-PC，08-手机
        requestUnionMap.put("channelType", "08");
        // 前台通知地址 ，控件接入方式无作用
        requestUnionMap.put("frontUrl", orderData.getReturnUrl());
        // 后台通知地址
        requestUnionMap.put("backUrl", unionNotifyUrl);
        // 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
        requestUnionMap.put("accessType", "0");
        // 商户号码，请改成自己的商户号
        requestUnionMap.put("merId", PropertiesUtil.getHpayValue("union.merId"));
        // 订单发送时间，取系统时间
        requestUnionMap.put("txnTime", DateFormatUtil.getCurrentTime());
        // 商户订单号，8-40位数字字母  (区分测试环境和正式环境订单号)
        requestUnionMap.put("orderId", orderData.getOutTradeNo());
        // 交易金额，单位分
        requestUnionMap.put("txnAmt", orderData.getAmount());
        // 交易币种
        requestUnionMap.put("currencyCode", "156");
        //透传字段
        requestUnionMap.put("reqReserved", orderData.getTradeType());
        //多密钥签名，存日志
        Map<String, String> submitFromData = com.hand.hap.cloud.hpay.services.pcServices.union.sdk.AcpService.sign(requestUnionMap, PropertiesUtil.getHpayValue("union.certPath"), PropertiesUtil.getHpayValue("union.certPwd"), com.hand.hap.cloud.hpay.services.pcServices.union.sdk.http.UnionPayHttp.encoding);
        // 交易请求url 从配置文件读取
        String requestFrontUrl = getConfig().getFrontRequestUrl();
        //封装
        String formData = UnionPayHttp.createHtml(requestFrontUrl, submitFromData);
        ThirdPartyApiLogs logs = new ThirdPartyApiLogs();
        logs.setRequestTime(new Date());
        logs.setRequestMethod("PAY");
        logs.setTarget("UNION");
        logs.setRequestBody(formData);
        logs.setClientType("MOBILE");
        logger.info("打印请求HTML，此为请求报文，为联调排查问题的依据：" + formData);
        return formData;
    }
}
