package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author: zhangzilong
 * name: AsChangeResponseBody.java
 * discription: 
 * date: 2017/8/29
 * version: 0.1
 */
@XmlRootElement(name = "ZMD_SD_SO_CHANGEResponse", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class AsChangeResponseBody extends ResponseBody {
    
    @XmlElement(name = "GDT_CONDTION")
    private AsGdtCondtions asGdtCondtions;

    @XmlElement(name = "T_ITEM")
    private AsGdtItems asGdtItems;

    @XmlElement(name = "T_RETURN")
    private AsChangeGdtReturn asChangeGdtReturn;

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

    public AsChangeGdtReturn getAsChangeGdtReturn() {
        return asChangeGdtReturn;
    }

    public void setAsChangeGdtReturn(AsChangeGdtReturn asChangeGdtReturn) {
        this.asChangeGdtReturn = asChangeGdtReturn;
    }
}
