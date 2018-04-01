package com.hand.hmall.ws.client;

import com.markor.map.framework.soapclient.annotations.SOAPCall;
import com.markor.map.framework.soapclient.annotations.SOAPClient;
import com.markor.map.framework.soapclient.annotations.SOAPRequestBody;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hmall.ws.entities.*;

/**
 * @author qinzhipeng
 * @version 0.1
 * @name:
 * @Description:
 * @date
 */
@SOAPClient
public interface IAsOrderPushClient {

    @SOAPCall(serviceId = "order.pushRetail")
    AsCreateResponseBody orderPush(@SOAPRequestBody AsCreateRequestBody request) throws WSCallException;

    @SOAPCall(serviceId = "order.updateRetail")
    AsChangeResponseBody orderUpdate(@SOAPRequestBody AsChangeRequestBody request) throws WSCallException;

}


