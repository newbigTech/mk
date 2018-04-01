package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.RequestBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 订单更新推送retail请求体
 * @date 2017/8/18 15:24
 */
@XmlRootElement(name = "ZMD_SD_SO_CHANGE", namespace = "urn:sap-com:document:sap:rfc:functions")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderUpdateRequestbody extends RequestBody {

    @XmlElement(name = "L_HEADER")
    private GdsHeader gdsHeader;

    @XmlElement(name = "GDT_CONDTION")
    private GdtCondtion gdtCondtion;

    @XmlElement(name = "T_ITEM")
    private GdtItem gdtItem;

    @XmlElement(name = "T_RETURN")
    private String gdtRetuen;

    public GdsHeader getGdsHeader() {
        return gdsHeader;
    }

    public void setGdsHeader(GdsHeader gdsHeader) {
        this.gdsHeader = gdsHeader;
    }

    public GdtCondtion getGdtCondtion() {
        return gdtCondtion;
    }

    public void setGdtCondtion(GdtCondtion gdtCondtion) {
        this.gdtCondtion = gdtCondtion;
    }

    public GdtItem getGdtItem() {
        return gdtItem;
    }

    public void setGdtItem(GdtItem gdtItem) {
        this.gdtItem = gdtItem;
    }

    public String getGdtRetuen() {
        return gdtRetuen;
    }

    public void setGdtRetuen(String gdtRetuen) {
        this.gdtRetuen = gdtRetuen;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GdsHeader {

        @XmlElement(name = "VBELN")
        private String sapCode;

        @XmlElement(name = "VKORG")
        private String org;

        @XmlElement(name = "VTWEG")
        private String VTWEG;

        @XmlElement(name = "KUNNR1")
        private String KUNNR;

        @XmlElement(name = "KUNNR2")
        private String KUNNR2;

        @XmlElement(name = "SPART")
        private String brand;

        @XmlElement(name = "VKBUR")
        private String VKBUR;

        @XmlElement(name = "ZZCLUBID")
        private String customerID;

        @XmlElement(name = "NAME1")
        private String receiverName;

        @XmlElement(name = "ZZCLUBLEVEL")
        private String userLevel;

        @XmlElement(name = "TITLE")
        private String sex;

        @XmlElement(name = "TRANSPZONE")
        private String regionCode;

        @XmlElement(name = "STREET")
        private String receiverAddress;

        @XmlElement(name = "TELEPHONE")
        private String receiverMobile;

        @XmlElement(name = "TELEPHONE2")
        private String receiverPhone;

        @XmlElement(name = "ORDERNOTE")
        private String note;

        @XmlElement(name = "ZZGLQHD")
        private String ZZGLQHD;

        @XmlElement(name = "ZZEDATS")
        private String ZZEDATS;

        @XmlElement(name = "AUGRU")
        private String AUGRU;

        @XmlElement(name = "ZZMARKETID1")
        private String ZZMARKETID1;

        @XmlElement(name = "ZZMARKETID2")
        private String ZZMARKETID2;

        @XmlElement(name = "AUART")
        private String orderType;

        @XmlElement(name = "VDATU")
        private String estimateDeliveryTime;

        @XmlElement(name = "ZZYYDAT")
        private String estimateDeliveryTime2;

        @XmlElement(name = "LIFSK")
        private String payStatus;

        @XmlElement(name = "VSBED")
        private String shippingType;

        @XmlElement(name = "WERKS")
        private String pointCode;

        @XmlElement(name = "AUTLF")
        private String splitAllowed;

        @XmlElement(name = "EXSYS")
        private String EXSYS;

        @XmlElement(name = "EXNID")
        private String EXNID;

        @XmlElement(name = "EXNAM")
        private String EXNAM;

        @XmlElement(name = "EXDAT")
        private String orderCreationday;

        @XmlElement(name = "EXTIM")
        private String orderCreationtime;

        @XmlElement(name = "STORIES_NO")
        private String code;

        @XmlElement(name = "EXORD")
        private String orderNumber;

        @XmlElement(name = "EXNIDC")
        private String modifyId;

        @XmlElement(name = "EXNAMC")
        private String modifyName;

        @XmlElement(name = "EXDATC")
        private String modifyDate;

        @XmlElement(name = "EXTIMC")
        private String modifyTime;

        @XmlElement(name = "ZZHOPEDAY")
        private String ZZHOPEDAY;

        public String getSapCode() {
            return sapCode;
        }

        public void setSapCode(String sapCode) {
            this.sapCode = sapCode;
        }

        public String getZZGLQHD() {
            return ZZGLQHD;
        }

        public void setZZGLQHD(String ZZGLQHD) {
            this.ZZGLQHD = ZZGLQHD;
        }

        public String getZZEDATS() {
            return ZZEDATS;
        }

        public void setZZEDATS(String ZZEDATS) {
            this.ZZEDATS = ZZEDATS;
        }

        public String getAUGRU() {
            return AUGRU;
        }

        public void setAUGRU(String AUGRU) {
            this.AUGRU = AUGRU;
        }

        public String getZZMARKETID1() {
            return ZZMARKETID1;
        }

        public void setZZMARKETID1(String ZZMARKETID1) {
            this.ZZMARKETID1 = ZZMARKETID1;
        }

        public String getZZMARKETID2() {
            return ZZMARKETID2;
        }

        public void setZZMARKETID2(String ZZMARKETID2) {
            this.ZZMARKETID2 = ZZMARKETID2;
        }

        public String getModifyId() {
            return modifyId;
        }

        public void setModifyId(String modifyId) {
            this.modifyId = modifyId;
        }

        public String getModifyName() {
            return modifyName;
        }

        public void setModifyName(String modifyName) {
            this.modifyName = modifyName;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getZZHOPEDAY() {
            return ZZHOPEDAY;
        }

        public void setZZHOPEDAY(String ZZHOPEDAY) {
            this.ZZHOPEDAY = ZZHOPEDAY;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getVTWEG() {
            return VTWEG;
        }

        public void setVTWEG(String VTWEG) {
            this.VTWEG = VTWEG;
        }

        public String getKUNNR() {
            return KUNNR;
        }

        public void setKUNNR(String KUNNR) {
            this.KUNNR = KUNNR;
        }

        public String getKUNNR2() {
            return KUNNR2;
        }

        public void setKUNNR2(String KUNNR2) {
            this.KUNNR2 = KUNNR2;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getVKBUR() {
            return VKBUR;
        }

        public void setVKBUR(String VKBUR) {
            this.VKBUR = VKBUR;
        }

        public String getCustomerID() {
            return customerID;
        }

        public void setCustomerID(String customerID) {
            this.customerID = customerID;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(String userLevel) {
            this.userLevel = userLevel;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }

        public String getReceiverAddress() {
            return receiverAddress;
        }

        public void setReceiverAddress(String receiverAddress) {
            this.receiverAddress = receiverAddress;
        }

        public String getReceiverMobile() {
            return receiverMobile;
        }

        public void setReceiverMobile(String receiverMobile) {
            this.receiverMobile = receiverMobile;
        }

        public String getReceiverPhone() {
            return receiverPhone;
        }

        public void setReceiverPhone(String receiverPhone) {
            this.receiverPhone = receiverPhone;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getEstimateDeliveryTime() {
            return estimateDeliveryTime;
        }

        public void setEstimateDeliveryTime(String estimateDeliveryTime) {
            this.estimateDeliveryTime = estimateDeliveryTime;
        }

        public String getEstimateDeliveryTime2() {
            return estimateDeliveryTime2;
        }

        public void setEstimateDeliveryTime2(String estimateDeliveryTime2) {
            this.estimateDeliveryTime2 = estimateDeliveryTime2;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public String getShippingType() {
            return shippingType;
        }

        public void setShippingType(String shippingType) {
            this.shippingType = shippingType;
        }

        public String getPointCode() {
            return pointCode;
        }

        public void setPointCode(String pointCode) {
            this.pointCode = pointCode;
        }

        public String getSplitAllowed() {
            return splitAllowed;
        }

        public void setSplitAllowed(String splitAllowed) {
            this.splitAllowed = splitAllowed;
        }

        public String getEXSYS() {
            return EXSYS;
        }

        public void setEXSYS(String EXSYS) {
            this.EXSYS = EXSYS;
        }

        public String getEXNID() {
            return EXNID;
        }

        public void setEXNID(String EXNID) {
            this.EXNID = EXNID;
        }

        public String getEXNAM() {
            return EXNAM;
        }

        public void setEXNAM(String EXNAM) {
            this.EXNAM = EXNAM;
        }

        public String getOrderCreationday() {
            return orderCreationday;
        }

        public void setOrderCreationday(String orderCreationday) {
            this.orderCreationday = orderCreationday;
        }

        public String getOrderCreationtime() {
            return orderCreationtime;
        }

        public void setOrderCreationtime(String orderCreationtime) {
            this.orderCreationtime = orderCreationtime;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
