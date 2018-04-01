package com.hand.hmall.ws.client;

import com.markor.map.framework.soapclient.annotations.SOAPCall;
import com.markor.map.framework.soapclient.annotations.SOAPClient;
import com.markor.map.framework.soapclient.annotations.SOAPRequestBody;
import com.hand.hmall.ws.entities.OrderRequestBody;
import com.hand.hmall.ws.entities.OrderResponseBody;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hmall.ws.entities.OrderUpdateRequestbody;
import com.hand.hmall.ws.entities.OrderUpdateResponseBody;

/**
 * @author peng.chen
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/6/8 0008 上午 10:47
 */
@SOAPClient
public interface IOrderPushClient {

    @SOAPCall(serviceId = "order.pushRetail")
    OrderResponseBody OrderPush(@SOAPRequestBody OrderRequestBody request) throws WSCallException;

    @SOAPCall(serviceId = "order.updateRetail")
    OrderUpdateResponseBody orderUpdate(@SOAPRequestBody OrderUpdateRequestbody request) throws WSCallException;

}


