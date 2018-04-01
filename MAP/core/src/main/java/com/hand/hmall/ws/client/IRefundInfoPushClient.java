package com.hand.hmall.ws.client;

import com.markor.map.framework.soapclient.annotations.SOAPCall;
import com.markor.map.framework.soapclient.annotations.SOAPClient;
import com.markor.map.framework.soapclient.annotations.SOAPRequestBody;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hmall.ws.entities.RefundInfoRequestBody;
import com.hand.hmall.ws.entities.RefundInfoResponseBody;

/**
 * Created by hand on 2017/8/22.
 */
@SOAPClient
public interface IRefundInfoPushClient {

    @SOAPCall(serviceId = "payment.toRetail")
    RefundInfoResponseBody paymentPush(@SOAPRequestBody RefundInfoRequestBody refundInfoRequestBody) throws WSCallException;
}
