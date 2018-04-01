package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PaymentResponseBody
 * @description 支付信息推送返回对象
 * @date 2017/8/21 9:05
 */
@XmlRootElement(name = "ZMD_FI_PAY_SAVEResponse", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentResponseBody extends ResponseBody {

    @XmlElementWrapper(name = "LT_ITEM")
    @XmlElements(value = @XmlElement(name = "item"))
    private List<PaymentRequestItem> itemList;

    @XmlElement(name = "L_MSG")
    private String msg;

    public List<PaymentRequestItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<PaymentRequestItem> itemList) {
        this.itemList = itemList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
