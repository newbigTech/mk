package com.hand.hap.cloud.hpay.services.pcServices.union.service.impl;

import com.hand.hap.cloud.hpay.data.QueryData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.AcpService;
import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.http.UnionPayHttp;
import com.hand.hap.cloud.hpay.services.pcServices.union.service.IUnionQueryService;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.hand.hap.cloud.hpay.services.constants.Constants.*;
import static com.hand.hap.cloud.hpay.services.constants.UnionConstants.*;
import static com.hand.hap.cloud.hpay.services.pcServices.union.sdk.SDKConfig.getConfig;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description
 * @date 2017/7/6
 */
@Service
public class UnionQueryServiceImpl implements IUnionQueryService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSender messageSender;

    /**
     * UNION查询
     *
     * @param queryData 查询数据
     * @return returnData
     */
    @Override
    public ReturnData unionQuery(QueryData queryData) {

        ReturnData respData = new ReturnData();

        Map<String, String> requestUnionMap = new HashMap<>();

        //版本号
        requestUnionMap.put(PARAM_VERSION, UnionPayHttp.version);
        //字符集编码 可以使用UTF-8,GBK两种方式
        requestUnionMap.put(PARAM_ENCODING, UnionPayHttp.encoding);
        //签名方法 目前只支持01-RSA方式证书加密
        requestUnionMap.put(PARAM_SIGNMETHOD, "01");
        //交易类型 04-退货
        requestUnionMap.put(PARAM_TXNTYPE, "00");
        //交易子类型  默认00
        requestUnionMap.put(PARAM_TXNSUBTYPE, "00");
        //产品类型 B2C网关支付，手机wap支付
        requestUnionMap.put(PARAM_BIZTYPE, "000201");
        //商户id
        requestUnionMap.put(PARAM_MERID, PropertiesUtil.getHpayValue("union.merId"));
        //接入类型，商户接入固定填0，不需修改
        requestUnionMap.put(PARAM_ACCESSTYPE, "0");
        //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
        if (PropertiesUtil.getPayInfoValue("paymentTypeCode.pay").equalsIgnoreCase(queryData.getPaymentTypeCode())) {
            requestUnionMap.put(PARAM_ORDERID, queryData.getOutTradeNo());
        } else {
            requestUnionMap.put(PARAM_ORDERID, queryData.getOutRefundNo());
        }
        //订单发送时间，格式为YYYYMMDDhhmmss
        requestUnionMap.put(PARAM_TXNTIME, queryData.getTxnTime());
        //保留域
        requestUnionMap.put(PARAM_TXNAMT, "100");
        //请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文
        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。union.certPath union.certPwd
        Map<String, String> requestUnionMapWithSign = AcpService.sign(requestUnionMap, PropertiesUtil.getHpayValue("union.certPath"), PropertiesUtil.getHpayValue("union.certPwd"), UnionPayHttp.encoding);
        logger.info("请求银联查询参数", requestUnionMapWithSign);
        //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
        String url = getConfig().getSingleQueryUrl();

        //日志
        ThirdPartyApiLogs thirdPartyApiLogs = new ThirdPartyApiLogs();
        Date requestTime = new Date();
        thirdPartyApiLogs.setRequestTime(requestTime);
        thirdPartyApiLogs.setClientType(PropertiesUtil.getPayInfoValue("mode.tradeType.pc"));
        thirdPartyApiLogs.setTarget(PropertiesUtil.getPayInfoValue("mode.unionpay"));
        thirdPartyApiLogs.setRequestMethod(PropertiesUtil.getPayInfoValue("pamentType.query"));
        thirdPartyApiLogs.setRequestBody(requestUnionMapWithSign.toString());

        //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，
        //调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> responseFromUnionMap = AcpService.post(requestUnionMapWithSign, url, UnionPayHttp.encoding);

        Date responseTime = new Date();
        thirdPartyApiLogs.setResponseTime(responseTime);
        thirdPartyApiLogs.setDuring(responseTime.getTime() - requestTime.getTime());

        List<Map<String, String>> responseQueryList = new ArrayList<>();
        Map<String, String> responseQueryMap = new HashMap<>();

        if (!responseFromUnionMap.isEmpty()) {
            if (AcpService.validate(responseFromUnionMap, UnionPayHttp.encoding)) {
                logger.info("UnionQueryServiceImpl-unionQuery-validateSign-success");
                String respCode = responseFromUnionMap.get(PARAM_RESPCODE);
                String respMsg = responseFromUnionMap.get(PARAM_RESPMSG);
                String origRespCode = responseFromUnionMap.get(PARAM_ORIGRESPCODE);
                String origRespMsg = responseFromUnionMap.get(PARAM_ORIGRESPMSG);
                if (PARAM_RESPCODE_00.equals(respCode)) {
                    //交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                    if (PARAM_RESPCODE_00.equals(origRespCode) || PARAM_RESPCODE_A6.equals(origRespCode)) {
                        responseQueryMap.put(PARAM_MODE, PropertiesUtil.getPayInfoValue("mode.unionpay"));
                        responseQueryMap.put(PARAM_TRADE_TYPE, queryData.getTradeType());
                        responseQueryMap.put(PARAM_PAYMENT_TYPE_CODE, queryData.getPaymentTypeCode());
                        responseQueryMap.put(PARAM_TRADE_RESULT_CODE, origRespCode);
                        responseQueryMap.put(PARAM_TRADE_DESCRIPTION, respMsg);
                        responseQueryMap.put(PARAM_TRADE_STATUS, respMsg);
                        responseQueryMap.put(PARAM_TRADE_NO, responseFromUnionMap.get(PARAM_QUERYID));
                        responseQueryMap.put(PARAM_OUT_TRADE_NO, responseFromUnionMap.get(PARAM_QUERYID));
                        responseQueryMap.put(PARAM_TOTAL_AMOUNT, responseFromUnionMap.get(PARAM_TXNAMT));
                        responseQueryMap.put(PARAM_RECEIP_AMOUNT, responseFromUnionMap.get(PARAM_TXNAMT));
                        responseQueryMap.put(PARAM_SETTLE_DATE, responseFromUnionMap.get(PARAM_SETTLE_DATE));
                        respData.setSuccess(true);
                        respData.setMsgCode(origRespCode);
                        respData.setMsg(origRespMsg);
                        logger.info("UnionQueryServiceImpl-unionQuery-respCode_00||A6-querySuccess" + responseFromUnionMap);
                        thirdPartyApiLogs.setSuccess(true);
                        thirdPartyApiLogs.setRespMsgCode(origRespCode);
                        thirdPartyApiLogs.setRespMsg(origRespCode);
                        thirdPartyApiLogs.setResponseBody(responseFromUnionMap.toString());
                        responseQueryList.add(responseQueryMap);
                        respData.setResp(responseQueryList);
                        //后续需发起交易状态查询交易确定交易状态
                    } else if (PARAM_RESPCODE_03.equals(origRespCode) || PARAM_RESPCODE_05.equals(origRespCode)) {
                        respData.setSuccess(false);
                        respData.setMsgCode(origRespCode);
                        respData.setMsg(origRespMsg);
                        logger.error("UnionQueryServiceImpl-unionQuery-respCode_03||05-fail-errorCode-" + origRespCode + "-origRespMsg-" + origRespMsg);
                        thirdPartyApiLogs.setSuccess(false);
                        thirdPartyApiLogs.setRespMsgCode(origRespCode);
                        thirdPartyApiLogs.setResponseBody(responseFromUnionMap.toString());
                        thirdPartyApiLogs.setRespMsg(origRespCode);
                        //交易失败
                    } else {
                        respData.setSuccess(false);
                        respData.setMsgCode(origRespCode);
                        respData.setMsg(origRespMsg);
                        logger.error("UnionQueryServiceImpl-unionQuery-respCode_00_其他应答码-原交易失败-errorCode-" + origRespCode + "-origRespMsg-" + origRespMsg);
                        thirdPartyApiLogs.setSuccess(false);
                        thirdPartyApiLogs.setResponseBody(responseFromUnionMap.toString());
                        thirdPartyApiLogs.setRespMsgCode(origRespCode);
                        thirdPartyApiLogs.setRespMsg(origRespCode);
                    }
                } else if (PARAM_RESPCODE_34.equals(respCode)) {
                    respData.setSuccess(false);
                    respData.setMsgCode(respCode);
                    respData.setMsg(respMsg);
                    logger.error("UnionQueryServiceImpl-unionQuery-respCode_34-errorCode-" + respCode + "-origRespMsg-" + respMsg);
                    thirdPartyApiLogs.setResponseBody(responseFromUnionMap.toString());
                    thirdPartyApiLogs.setSuccess(false);
                    thirdPartyApiLogs.setRespMsgCode(respCode);
                    thirdPartyApiLogs.setRespMsg(respMsg);
                } else {
                    respData.setSuccess(false);
                    respData.setMsgCode(respCode);
                    respData.setMsg(respMsg);
                    logger.error("UnionQueryServiceImpl-unionQuery-respCode_34-errorCode-" + respCode + "-origRespMsg-" + respMsg);
                    thirdPartyApiLogs.setSuccess(false);
                    thirdPartyApiLogs.setRespMsgCode(respCode);
                    thirdPartyApiLogs.setRespMsg(respMsg);
                    thirdPartyApiLogs.setResponseBody(responseFromUnionMap.toString());
                }
            } else {
                logger.error("验证签名失败");
                respData.setSuccess(false);
                respData.setMsgCode(responseFromUnionMap.get("respCode"));
                respData.setMsg("验证签名失败" + responseFromUnionMap.get("respMsg"));
                logger.error("UnionQueryServiceImpl-unionQuery-验证签名失败");
                thirdPartyApiLogs.setSuccess(false);
                thirdPartyApiLogs.setRespMsgCode(responseFromUnionMap.get("respCode"));
                thirdPartyApiLogs.setRespMsg("验证签名失败" + responseFromUnionMap.get("respMsg"));
                thirdPartyApiLogs.setResponseBody(responseFromUnionMap.toString());
            }

        } else {
            //未返回正确的http状态
            logger.error("未获取到返回报文或返回http状态码非200");
            respData.setSuccess(false);
            respData.setMsg("未获取到返回报文或返回http状态码非200");
            thirdPartyApiLogs.setSuccess(false);
            thirdPartyApiLogs.setRespMsg("未获取到返回报文或返回http状态码非200");
        }
        messageSender.sendMsg(thirdPartyApiLogs);
        return respData;
    }
}