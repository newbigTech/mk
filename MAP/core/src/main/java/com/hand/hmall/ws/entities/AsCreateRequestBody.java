package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.RequestBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author: zhangzilong
 * name: AsCreateRequestBody.java
 * discription: 售后单据同步Retail实体类
 * date: 2017/8/29
 * version: 0.1
 */
@XmlRootElement(name = "ZZSD_SO_CREATE", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class AsCreateRequestBody extends RequestBody {

    @XmlElement(name = "GDS_HEADER")
    private AsCreateGdtHeader asCreateGdtHeader;

    @XmlElement(name = "GDT_CONDTION")
    private AsGdtCondtions asGdtCondtions;

    @XmlElement(name = "GDT_ITEM")
    private AsGdtItems asGdtItems;

    @XmlElement(name = "LV_SO")
    private String lvSo;

    @XmlElement(name = "GDT_RETURN")
    private AsCreateGdtReturn asCreateGdtReturn;

    public AsCreateGdtHeader getAsCreateGdtHeader() {
        return asCreateGdtHeader;
    }

    public void setAsCreateGdtHeader(AsCreateGdtHeader asCreateGdtHeader) {
        this.asCreateGdtHeader = asCreateGdtHeader;
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

    public String getLvSo() {
        return lvSo;
    }

    public void setLvSo(String lvSo) {
        this.lvSo = lvSo;
    }

    public AsCreateGdtReturn getAsCreateGdtReturn() {
        return asCreateGdtReturn;
    }

    public void setAsCreateGdtReturn(AsCreateGdtReturn asCreateGdtReturn) {
        this.asCreateGdtReturn = asCreateGdtReturn;
    }
}



