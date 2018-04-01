package com.hand.hmall.ws.client;

import com.markor.map.framework.soapclient.annotations.SOAPCall;
import com.markor.map.framework.soapclient.annotations.SOAPClient;
import com.markor.map.framework.soapclient.annotations.SOAPRequestBody;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hmall.ws.entities.MongateMULTIXRequest;
import com.hand.hmall.ws.entities.MongateMULTIXResponse;

/**
 * author: zhangzilong
 * name: IDreamPlatformClient
 * discription: 梦网短信平台
 * date: 2017/11/20
 * version: 0.1
 */
@SOAPClient
public interface IDreamPlatformClient {

    @SOAPCall(serviceId = "dream.pushMultixMsg")
    MongateMULTIXResponse sendMultixMsg(@SOAPRequestBody MongateMULTIXRequest request) throws WSCallException;

}
