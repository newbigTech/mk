package com.hand.hmall.ws.client;

import com.markor.map.framework.soapclient.annotations.SOAPCall;
import com.markor.map.framework.soapclient.annotations.SOAPClient;
import com.markor.map.framework.soapclient.annotations.SOAPRequestBody;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hmall.ws.entities.BillRequestBody;
import com.hand.hmall.ws.entities.BillResponseBody;

/**
 * @Author:zhangyanan
 * @Description:
 * @Date:Crated in 13:57 2017/9/22
 * @Modified By:
 */
@SOAPClient
public interface IBillPushClient {

    @SOAPCall(serviceId = "payment.toRetail")
    BillResponseBody billPush(@SOAPRequestBody BillRequestBody request) throws WSCallException;


}
