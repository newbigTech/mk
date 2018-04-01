package com.hand.hmall.ws.client;

import com.markor.map.framework.soapclient.annotations.SOAPCall;
import com.markor.map.framework.soapclient.annotations.SOAPClient;
import com.markor.map.framework.soapclient.annotations.SOAPRequestBody;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hmall.ws.entities.PaymentRequestBody;
import com.hand.hmall.ws.entities.PaymentResponseBody;

/**
 * @author 马君
 * @version 0.1
 * @name IPaymentPushClient
 * @description 支付信息推送Retail
 * @date 2017/8/21 9:04
 */
@SOAPClient
public interface IPaymentPushClient {

    @SOAPCall(serviceId = "payment.toRetail")
    PaymentResponseBody paymentPush(@SOAPRequestBody PaymentRequestBody paymentRequestBody) throws WSCallException;
}
