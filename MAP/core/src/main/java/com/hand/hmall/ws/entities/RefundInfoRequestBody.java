package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.RequestBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Author: shoupeng.wei@hand-china.com
 * @Created at: 2017年8月21日22:24:06
 * @Description:退款单明细Job对应请求体
 */
@XmlRootElement(name="ZMD_FI_PAY_SAVE", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class RefundInfoRequestBody extends RequestBody {

    @XmlElement(name = "LT_ITEM")
    private ltItem ltItem;

    public com.hand.hmall.ws.entities.ltItem getLtItem() {
        return ltItem;
    }

    public void setLtItem(com.hand.hmall.ws.entities.ltItem ltItem) {
        this.ltItem = ltItem;
    }
}
