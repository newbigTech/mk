package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.RequestBody;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PaymentRequestBody
 * @description 支付信息同步retial请求体
 * @date 2017/8/18 16:37
 */
@XmlRootElement(name = "ZMD_FI_PAY_SAVE", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentRequestBody extends RequestBody {

    @XmlElementWrapper(name = "LT_ITEM")
    @XmlElements(value = @XmlElement(name = "item"))
    private List<PaymentRequestItem> itemList;

    public List<PaymentRequestItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<PaymentRequestItem> itemList) {
        this.itemList = itemList;
    }



}
