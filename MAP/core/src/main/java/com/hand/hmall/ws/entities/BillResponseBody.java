package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Author:zhangyanan
 * @Description:
 * @Date:Crated in 14:02 2017/9/22
 * @Modified By:
 */
@XmlRootElement(name = "ZMD_FI_PAY_SAVEResponse", namespace = "rn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class BillResponseBody extends ResponseBody {

    @XmlElement(name = "LT_ITEM")
    private BillRequestItem billRequestItem;

    @XmlElement(name = "L_MSG")
    private String msg;

    public BillRequestItem getBillRequestItem() {
        return billRequestItem;
    }

    public void setBillRequestItem(BillRequestItem billRequestItem) {
        this.billRequestItem = billRequestItem;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
