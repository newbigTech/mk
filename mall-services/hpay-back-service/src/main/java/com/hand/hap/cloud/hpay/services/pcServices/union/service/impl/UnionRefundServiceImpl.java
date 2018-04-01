package com.hand.hap.cloud.hpay.services.pcServices.union.service.impl;

import com.hand.hap.cloud.hpay.data.RefundData;
import com.hand.hap.cloud.hpay.data.ReturnData;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.hpay.mq.MessageSender;
import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.AcpService;
import com.hand.hap.cloud.hpay.services.pcServices.union.sdk.http.UnionPayHttp;
import com.hand.hap.cloud.hpay.services.pcServices.union.service.IUnionRefundService;
import com.hand.hap.cloud.hpay.utils.DateFormatUtil;
import com.hand.hap.cloud.hpay.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
public class UnionRefundServiceImpl implements IUnionRefundService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MessageSender messageSender;
    @Value("${union.notifyUrl}")
    private String unionNotifyUrl;

    /**
     * 银联退款
     *
     * @param refundData 退款信息
     * @return returnData
     */
    @Override
    public ReturnData unionRefund(RefundData refundData) {


        ReturnData responseData = new ReturnData();

        List<Map<String, String>> list = new ArrayList<>();

        HashMap<String, String> requestUnionMap = new HashMap<>();

        //版本号
        requestUnionMap.put(PARAM_VERSION, UnionPayHttp.version);
        //字符集编码 可以使用UTF-8,GBK两种方式
        requestUnionMap.put(PARAM_ENCODING, UnionPayHttp.encoding);
        //签名方法 目前只支持01-RSA方式证书加密
        requestUnionMap.put(PARAM_SIGNMETHOD, "01");
        //交易类型 04-退货
        requestUnionMap.put(PARAM_TXNTYPE, "04");
        //交易子类型  默认00
        requestUnionMap.put(PARAM_TXNSUBTYPE, "00");
        //业务类型 B2C网关支付，手机wap支付
        requestUnionMap.put(PARAM_BIZTYPE, "000201");
        //渠道类型，07-PC，08-手机
        requestUnionMap.put(PARAM_CHANNELTYPE, "07");

        //商户接入参数
        //接入类型，商户接入固定填0，不需修改
        requestUnionMap.put(PARAM_ACCESSTYPE, "0");
        //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestUnionMap.put(PARAM_TXNTIME, DateFormatUtil.getCurrentTime());
        //交易币种（境内商户一般是156 人民币）
        requestUnionMap.put(PARAM_CURRENCYCODE, "156");

        requestUnionMap.put(PARAM_ORDERID, refundData.getOutRefundNo());
        requestUnionMap.put(PARAM_MERID, PropertiesUtil.getHpayValue("union.merId"));
        requestUnionMap.put(PARAM_TXNAMT, refundData.getAmount());
        requestUnionMap.put(PARAM_BACKURL, unionNotifyUrl);
        requestUnionMap.put(PARAM_ORIGQRYID, refundData.getTradeNo());

        //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
        Map<String, String> requestUnionMapWithSign = AcpService.sign(requestUnionMap, PropertiesUtil.getHpayValue("union.certPath"), PropertiesUtil.getHpayValue("union.certPwd"), UnionPayHttp.encoding);

        // 交易请求url 从配置文件读取
        //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        String backRequestUr = getConfig().getBackRequestUrl();

        //日志
        ThirdPartyApiLogs thirdPartyApiLogs = new ThirdPartyApiLogs();
        Date requestTime = new Date();
        thirdPartyApiLogs.setRequestTime(requestTime);
        thirdPartyApiLogs.setClientType(PropertiesUtil.getPayInfoValue("mode.tradeType.pc"));
        thirdPartyApiLogs.setTarget(PropertiesUtil.getPayInfoValue("mode.unionpay"));
        thirdPartyApiLogs.setRequestMethod(PropertiesUtil.getPayInfoValue("paymentTypecode.refund"));
        thirdPartyApiLogs.setRequestBody(requestUnionMapWithSign.toString());

        //如果这里通讯读超时（30秒），需发起交易状态查询交易
        //这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> refundResponseFromUnion = UnionPayHttp.submitUrl(requestUnionMapWithSign, backRequestUr, UnionPayHttp.encoding);

        Date responseTime = new Date();
        thirdPartyApiLogs.setResponseTime(responseTime);
        thirdPartyApiLogs.setDuring(responseTime.getTime() - requestTime.getTime());

        if (!refundResponseFromUnion.isEmpty()) {

            if (AcpService.validate(refundResponseFromUnion, UnionPayHttp.encoding)) {

                logger.info("银联退款签名验证成功");

                HashMap<String, String> reqMap = new HashMap();

                String respCode = refundResponseFromUnion.get(PARAM_RESPCODE);

                if (PARAM_RESPCODE_00.equals(respCode)) {
                    //交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                    //数据组装
                    reqMap.put("mode", PropertiesUtil.getPayInfoValue("mode.unionpay"));
                    reqMap.put("tradeResultCode", refundResponseFromUnion.get(PARAM_RESPCODE));
                    reqMap.put("tradeDescription", refundResponseFromUnion.get(PARAM_RESPMSG));
                    reqMap.put("tradeStatus", "REFUND_SUCCESS");
                    reqMap.put("notifyId", refundResponseFromUnion.get(PARAM_TRACENO));
                    reqMap.put("tradeCreateTime", refundResponseFromUnion.get(PARAM_TXNTIME));
                    reqMap.put("tradePayTime", refundResponseFromUnion.get(PARAM_TXNTIME));
                    reqMap.put("totalAmount", refundResponseFromUnion.get(PARAM_TXNAMT));
                    reqMap.put("buyerPayAmount", refundResponseFromUnion.get(PARAM_TXNAMT));
                    reqMap.put("tradeNo", refundResponseFromUnion.get(PARAM_ORIGQRYID));
                    reqMap.put("outTradeNo", refundResponseFromUnion.get(PARAM_ORDERID));

                    thirdPartyApiLogs.setSuccess(true);
                    thirdPartyApiLogs.setRespMsgCode(refundResponseFromUnion.get(PARAM_RESPCODE));
                    thirdPartyApiLogs.setRespMsg("银联退款成功，" + refundResponseFromUnion.get(PARAM_RESPMSG));
                    thirdPartyApiLogs.setResponseBody(reqMap.toString());
                    messageSender.sendMsg(thirdPartyApiLogs);
                    logger.info("银联退款成功-code-" + refundResponseFromUnion.get(PARAM_RESPCODE));
                    list.add(reqMap);
                    responseData.setSuccess(true);
                    responseData.setResp(list);
                    responseData.setMsgCode(refundResponseFromUnion.get(PARAM_RESPCODE));
                    responseData.setMsg("银联退款成功，" + refundResponseFromUnion.get(PARAM_RESPMSG));
                    logger.info("银联退款成功");
                    return responseData;

                    //后续需发起交易状态查询交易确定交易状态
                } else if (PARAM_RESPCODE_03.equals(respCode) ||
                        PARAM_RESPCODE_04.equals(respCode) ||
                        PARAM_RESPCODE_05.equals(respCode)) {
                    responseData.setSuccess(false);
                    responseData.setMsgCode(refundResponseFromUnion.get(PARAM_RESPCODE));
                    responseData.setMsg("退款失败，" + refundResponseFromUnion.get(PARAM_RESPMSG));
                    thirdPartyApiLogs.setSuccess(false);
                    thirdPartyApiLogs.setRespMsgCode(refundResponseFromUnion.get(PARAM_RESPCODE));
                    thirdPartyApiLogs.setRespMsg("退款失败，" + refundResponseFromUnion.get(PARAM_RESPMSG));
                    thirdPartyApiLogs.setResponseBody(refundResponseFromUnion.toString());
                    messageSender.sendMsg(thirdPartyApiLogs);
                    logger.error("银联退款失败，后续需发起交易状态查询交易确定交易状态-errorCode-" + refundResponseFromUnion.get(PARAM_RESPCODE) + "-errorMsg+" + refundResponseFromUnion.get(PARAM_RESPMSG));
                    return responseData;

                } else {
                    //其他应答码为失败请排查原因
                    responseData.setSuccess(false);
                    responseData.setResp(list);
                    responseData.setMsgCode(refundResponseFromUnion.get(PARAM_RESPCODE));
                    responseData.setMsg("退款失败，" + refundResponseFromUnion.get(PARAM_RESPMSG));
                    logger.error("银联退款失败，其他应答码为失败请排查原因-errorCode-" + refundResponseFromUnion.get(PARAM_RESPCODE) + "-errorMsg+" + refundResponseFromUnion.get(PARAM_RESPMSG));
                    thirdPartyApiLogs.setSuccess(false);
                    thirdPartyApiLogs.setRespMsgCode(refundResponseFromUnion.get(PARAM_RESPCODE));
                    thirdPartyApiLogs.setRespMsg("退款失败，" + refundResponseFromUnion.get(PARAM_RESPMSG));
                    thirdPartyApiLogs.setResponseBody(refundResponseFromUnion.toString());
                    messageSender.sendMsg(thirdPartyApiLogs);
                    return responseData;
                }
            } else {
                responseData.setSuccess(false);
                responseData.setMsg("银联退款签名验证失败");
                thirdPartyApiLogs.setSuccess(false);
                logger.error("银联退款签名验证失败");
                thirdPartyApiLogs.setRespMsgCode(refundResponseFromUnion.get(PARAM_RESPCODE));
                thirdPartyApiLogs.setRespMsg("验证签名失败");
                thirdPartyApiLogs.setResponseBody(refundResponseFromUnion.toString());
                messageSender.sendMsg(thirdPartyApiLogs);
                return responseData;
            }

        } else {
            //未返回正确的http状态
            responseData.setSuccess(false);
            responseData.setMsg("未获取到返回报文或返回http状态码非200");
            logger.error("未获取到返回报文或返回http状态码非200");
            thirdPartyApiLogs.setSuccess(false);
            thirdPartyApiLogs.setRespMsg("未获取到返回报文或返回http状态码非200");
            messageSender.sendMsg(thirdPartyApiLogs);
            return responseData;
        }
    }
}
