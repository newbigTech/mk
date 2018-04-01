package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;
import com.hand.hmall.ws.entities.GdtReturn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 唐磊
 * @version 0.1
 * @name:
 * @Description:  订单推送 Hmall->retail
 * @date 2017/6/8 10:22
 */
@XmlRootElement(name = "ZZSD_SO_CREATEResponse", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderResponseBody extends ResponseBody {

    @XmlElement(name = "GDE_EBELN")
    private String gdeEbeln;

    @XmlElement(name = "GDE_VBELN")
    private String gdeVbeln;

    @XmlElement(name = "GDT_CONDTION")
    private String gdtCondtion;

    @XmlElement(name = "GDT_ITEM")
    private String gdtItem;

    @XmlElement(name = "GDT_RETURN")
    private GdtReturn gdtReturn;


    public String getGdeEbeln() {
        return gdeEbeln;
    }

    public void setGdeEbeln(String gdeEbeln) {
        this.gdeEbeln = gdeEbeln;
    }

    public String getGdeVbeln() {
        return gdeVbeln;
    }

    public void setGdeVbeln(String gdeVbeln) {
        this.gdeVbeln = gdeVbeln;
    }

    public String getGdtCondtion() {
        return gdtCondtion;
    }

    public void setGdtCondtion(String gdtCondtion) {
        this.gdtCondtion = gdtCondtion;
    }

    public String getGdtItem() {
        return gdtItem;
    }

    public void setGdtItem(String gdtItem) {
        this.gdtItem = gdtItem;
    }

    public GdtReturn getGdtReturn() {
        return gdtReturn;
    }

    public void setGdtReturn(GdtReturn gdtReturn) {
        this.gdtReturn = gdtReturn;
    }
}
