package com.hand.hmall.ws.client;

import com.markor.map.framework.soapclient.annotations.SOAPCall;
import com.markor.map.framework.soapclient.annotations.SOAPClient;
import com.markor.map.framework.soapclient.annotations.SOAPRequestBody;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hmall.ws.entities.CheckBillRequestBody;
import com.hand.hmall.ws.entities.CheckBillResponseBody;

/**
 * @Author:zhangyanan
 * @Description:对账信息推送Retail
 * @Date:Crated in 14:11 2017/10/17
 * @Modified By:
 */
@SOAPClient
public interface ICheckBillPushClient {

    @SOAPCall(serviceId = "checkBill.toRetail")
    CheckBillResponseBody checkBillPush(@SOAPRequestBody CheckBillRequestBody checkBillRequestBody) throws WSCallException;

}
