package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.RequestBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author: zhangzilong
 * name: AsChangeRequestBody.java
 * discription: 售后单据同步Retail
 * date: 2017/8/29
 * version: 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ZMD_SD_SO_CHANGE", namespace = "urn:sap-com:document:sap:rfc:functions")
public class AsChangeRequestBody extends RequestBody {

    @XmlElement(name = "L_HEADER")
    private AsChangeGdtHeader asChangeGdtHeader;

    @XmlElement(name = "GDT_CONDTION")
    private AsGdtCondtions asGdtCondtions;

    @XmlElement(name = "T_ITEM")
    private AsGdtItems asGdtItems;

    @XmlElement(name = "T_RETURN")
    private AsChangeGdtReturn asChangeGdtReturn;

    public AsChangeGdtHeader getAsChangeGdtHeader() {
        return asChangeGdtHeader;
    }

    public void setAsChangeGdtHeader(AsChangeGdtHeader asChangeGdtHeader) {
        this.asChangeGdtHeader = asChangeGdtHeader;
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

    public AsChangeGdtReturn getAsChangeGdtReturn() {
        return asChangeGdtReturn;
    }

    public void setAsChangeGdtReturn(AsChangeGdtReturn asChangeGdtReturn) {
        this.asChangeGdtReturn = asChangeGdtReturn;
    }
}
