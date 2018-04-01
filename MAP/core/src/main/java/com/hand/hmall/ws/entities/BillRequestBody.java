package com.hand.hmall.ws.entities;


import com.markor.map.framework.soapclient.entities.RequestBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Author:zhangyanan
 * @Description:
 * @Date:Crated in 14:00 2017/9/22
 * @Modified By:
 */
@XmlRootElement(name = "ZMD_FI_PAY_SAVE", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class BillRequestBody extends RequestBody {

    @XmlElement(name = "LT_ITEM")
    private BillRequestItem billRequestItem;

    public BillRequestItem getBillRequestItem() {
        return billRequestItem;
    }

    public void setBillRequestItem(BillRequestItem billRequestItem) {
        this.billRequestItem = billRequestItem;
    }
}
