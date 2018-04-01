package com.hand.hmall.ws.client;

import com.markor.map.framework.soapclient.annotations.SOAPCall;
import com.markor.map.framework.soapclient.annotations.SOAPClient;
import com.markor.map.framework.soapclient.annotations.SOAPRequestBody;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hmall.ws.entities.OrderToEccRequestBody;
import com.hand.hmall.ws.entities.OrderToEccResponseBody;

/**
 * Created by qinzhipeng on 2017/11/13.
 */
@SOAPClient
public interface IOrderToEccPushClient {
    @SOAPCall(serviceId = "order.toEcc")
    OrderToEccResponseBody OrderToEccPush(@SOAPRequestBody OrderToEccRequestBody request) throws WSCallException;

}
