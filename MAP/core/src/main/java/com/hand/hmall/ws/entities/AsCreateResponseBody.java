package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author: zhangzilong
 * name: AsCreateResponseBody.java
 * discription:
 * date: 2017/8/29
 * version: 0.1
 */
@XmlRootElement(name = "ZZSD_SO_CREATEResponse", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class AsCreateResponseBody extends ResponseBody {

    @XmlElement(name = "GDE_VBELN")
    private String gdeVbeln;

    @XmlElement(name = "GDT_CONDTION")
    private AsGdtCondtions asGdtCondtions;

    @XmlElement(name = "GDT_ITEM")
    private AsGdtItems asGdtItems;

    @XmlElement(name = "GDT_RETURN")
    private AsCreateGdtReturn asCreateGdtReturn;

    public String getGdeVbeln() {
        return gdeVbeln;
    }

    public void setGdeVbeln(String gdeVbeln) {
        this.gdeVbeln = gdeVbeln;
    }

    public AsGdtCondtions getAsGdtCondtions() {
        return asGdtCondtions;
    }

    public void setAsGdtCondtions(AsGdtCondtions asGdtCondtions) {
        this.asGdtCondtions = asGdtCondtions;
    }

    public AsGdtItems getAsGdtItems() {
        return asGdtItems;
    }

    public void setAsGdtItems(AsGdtItems asGdtItems) {
        this.asGdtItems = asGdtItems;
    }

    public AsCreateGdtReturn getAsCreateGdtReturn() {
        return asCreateGdtReturn;
    }

    public void setAsCreateGdtReturn(AsCreateGdtReturn asCreateGdtReturn) {
        this.asCreateGdtReturn = asCreateGdtReturn;
    }
}
