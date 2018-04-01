package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 订单更新retail响应报文实体
 * @date 2017/8/18 16:05
 */
@XmlRootElement(name = "ZZSD_SO_CHANGEResponse", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderUpdateResponseBody extends ResponseBody {

    @XmlElement(name = "GDT_CONDITION")
    private String gdtCondition;

    @XmlElement(name = "T_ITEM")
    private String tItem;

    @XmlElement(name = "T_RETURN")
    private TReturn tReturn;

    public String getGdtCondition() {
        return gdtCondition;
    }

    public void setGdtCondition(String gdtCondition) {
        this.gdtCondition = gdtCondition;
    }

    public String gettItem() {
        return tItem;
    }

    public void settItem(String tItem) {
        this.tItem = tItem;
    }

    public TReturn gettReturn() {
        return tReturn;
    }

    public void settReturn(TReturn tReturn) {
        this.tReturn = tReturn;
    }

}
