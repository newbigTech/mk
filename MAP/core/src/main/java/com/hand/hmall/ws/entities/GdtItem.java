package com.hand.hmall.ws.entities;/**
 * @author peng.chen
 * @version 0.1
 * @name:
 * @Description:
 * @date 2017/6/7 0007 下午 9:15
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;

/**
 * GdtItem
 *
 * @author
 * @create 2017-06-07 下午 9:15
 **/
@XmlAccessorType(XmlAccessType.FIELD)
public class GdtItem {
    @XmlElements(value = @XmlElement(name = "item"))
    private List<entryItem> items;

    public List<entryItem> getItems() {
        return items;
    }

    public void setItems(List<entryItem> items) {
        this.items = items;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class entryItem {
        @XmlElement(name = "POSNR")
        private Long lineNumber;

        @XmlElement(name = "UEPOS")
        private Long parentLine;

        @XmlElement(name = "ZZFREE")
        private String isGift;

        @XmlElement(name = "PSTYV")
        private String orderEntryType;

        @XmlElement(name = "MATNR")
        private String productCode;

        @XmlElement(name = "KWMENG")
        private Integer quantity;

        @XmlElement(name = "ZZXMBZ")
        private String note;

        @XmlElement(name = "SDABW")
        private String shippingType;

        //货源工厂
        @XmlElement(name = "ZZWERKS")
        private String ZZWERKS;

        //库存地点
        @XmlElement(name = "LGORT")
        private String LGORT;

        //拒绝原因
        @XmlElement(name = "ABGRU")
        private String rejectReason;

        @XmlElement(name = "ZZREFNO")
        private String vproductCode;

        @XmlElement(name = "ZZIHREZ")
        private String pin;

        @XmlElement(name = "KZWI2")
        private String KZWI2;

        @XmlElement(name = "ZCCRQ")
        private String ZCCRQ;

        @XmlElement(name = "ZBS")
        private String ZBS;

        //装运类型
        @XmlElement(name = "ZY04")
        private String zy04;

        //留空字段 ---------------------- begin
        @XmlElement(name = "VGBEL")
        private String VGBEL;

        @XmlElement(name = "VGPOS")
        private String VGPOS;

        @XmlElement(name = "ZZREQU")
        private String ZZREQU;

        @XmlElement(name = "ZZKFREASON1")
        private String ZZKFREASON1;

        @XmlElement(name = "ZZKFREASON2")
        private String ZZKFREASON2;

        @XmlElement(name = "ZZTHREASON1")
        private String ZZTHREASON1;

        @XmlElement(name = "ZZTHREASON2")
        private String ZZTHREASON2;


        //留空字段 --------------------------end

        //产品尺寸
        @XmlElement(name = "ZY01")
        private String ZY01;

        //包装尺寸
        @XmlElement(name = "ZY02")
        private String ZY02;

        //定制信息
        @XmlElement(name = "ZY03")
        private String ZY03;

        public String getZY01() {
            return ZY01;
        }

        public void setZY01(String ZY01) {
            this.ZY01 = ZY01;
        }

        public String getZY02() {
            return ZY02;
        }

        public void setZY02(String ZY02) {
            this.ZY02 = ZY02;
        }

        public String getZY03() {
            return ZY03;
        }

        public void setZY03(String ZY03) {
            this.ZY03 = ZY03;
        }

        public String getVGBEL() {
            return VGBEL;
        }

        public void setVGBEL(String VGBEL) {
            this.VGBEL = VGBEL;
        }

        public String getZy04() {
            return zy04;
        }

        public void setZy04(String zy04) {
            this.zy04 = zy04;
        }

        public String getVGPOS() {
            return VGPOS;
        }

        public void setVGPOS(String VGPOS) {
            this.VGPOS = VGPOS;
        }

        public String getZZREQU() {
            return ZZREQU;
        }

        public void setZZREQU(String ZZREQU) {
            this.ZZREQU = ZZREQU;
        }

        public String getZZKFREASON1() {
            return ZZKFREASON1;
        }

        public void setZZKFREASON1(String ZZKFREASON1) {
            this.ZZKFREASON1 = ZZKFREASON1;
        }

        public String getZZKFREASON2() {
            return ZZKFREASON2;
        }

        public void setZZKFREASON2(String ZZKFREASON2) {
            this.ZZKFREASON2 = ZZKFREASON2;
        }

        public String getZZTHREASON1() {
            return ZZTHREASON1;
        }

        public void setZZTHREASON1(String ZZTHREASON1) {
            this.ZZTHREASON1 = ZZTHREASON1;
        }

        public String getZZTHREASON2() {
            return ZZTHREASON2;
        }

        public void setZZTHREASON2(String ZZTHREASON2) {
            this.ZZTHREASON2 = ZZTHREASON2;
        }

        public String getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }

        public String getZCCRQ() {
            return ZCCRQ;
        }

        public void setZCCRQ(String ZCCRQ) {
            this.ZCCRQ = ZCCRQ;
        }

        public String getZBS() {
            return ZBS;
        }

        public void setZBS(String ZBS) {
            this.ZBS = ZBS;
        }

        public String getKZWI2() {
            return KZWI2;
        }

        public void setKZWI2(String KZWI2) {
            this.KZWI2 = KZWI2;
        }

        public Long getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(Long lineNumber) {
            this.lineNumber = lineNumber;
        }

        public Long getParentLine() {
            return parentLine;
        }

        public void setParentLine(Long parentLine) {
            this.parentLine = parentLine;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getIsGift() {
            return isGift;
        }

        public void setIsGift(String isGift) {
            this.isGift = isGift;
        }

        public String getOrderEntryType() {
            return orderEntryType;
        }

        public void setOrderEntryType(String orderEntryType) {
            this.orderEntryType = orderEntryType;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getShippingType() {
            return shippingType;
        }

        public void setShippingType(String shippingType) {
            this.shippingType = shippingType;
        }

        public String getZZWERKS() {
            return ZZWERKS;
        }

        public void setZZWERKS(String ZZWERKS) {
            this.ZZWERKS = ZZWERKS;
        }

        public String getLGORT() {
            return LGORT;
        }

        public void setLGORT(String LGORT) {
            this.LGORT = LGORT;
        }

        public String getVproductCode() {
            return vproductCode;
        }

        public void setVproductCode(String vproductCode) {
            this.vproductCode = vproductCode;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }
    }

}
