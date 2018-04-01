package com.hand.hmall.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @Title PointOfServiceData
 * @Description 服务点基础数据Dto，非mybatis实体类
 * @Author majun
 * @Date 2017/05/25 18:57
 */

@XmlRootElement
@XmlType(propOrder = {"code", "displayname", "type",
        "zone1", "contactNumber", "address", "shopstatus", "zzwerks", "bukrs"})
public class PointOfServiceData {

    private String code;
    private String displayname;
    private String type;
    private String zone1;
    private String contactNumber;
    private String address;
    private String shopstatus;
    private String zzwerks;
    private String bukrs;

    @XmlElement(name = "code", required = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(name = "displayname", required = true)
    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    @XmlElement(name = "type", required = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZone1() {
        return zone1;
    }

    public void setZone1(String zone1) {
        this.zone1 = zone1;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopstatus() {
        return shopstatus;
    }

    public void setShopstatus(String shopstatus) {
        this.shopstatus = shopstatus;
    }

    public String getZzwerks() {
        return zzwerks;
    }

    public void setZzwerks(String zzwerks) {
        this.zzwerks = zzwerks;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

}