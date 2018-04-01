package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by qinzhipeng on 2017/11/13.
 */
@XmlRootElement(name = "ZMD_SD_001Response", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderToEccResponseBody extends ResponseBody {
}
