package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Author: shoupeng.wei@hand-china.com
 * @Created at: 2017年8月22日9:20:16
 * @Description:退款单明细Job对应请求体
 */

@XmlRootElement(name="ZMD_FI_PAY_SAVE", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class RefundInfoResponseBody extends ResponseBody {

    @XmlElement(name = "LT_ITEM")
    private ltItem ltItem;

    @XmlElement(name = "L_MSG")
    private String lMsg;

    public com.hand.hmall.ws.entities.ltItem getLtItem() {
        return ltItem;
    }

    public void setLtItem(com.hand.hmall.ws.entities.ltItem ltItem) {
        this.ltItem = ltItem;
    }

    public String getlMsg() {
        return lMsg;
    }

    public void setlMsg(String lMsg) {
        this.lMsg = lMsg;
    }
}
