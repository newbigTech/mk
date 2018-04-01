package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;

/**
 * @Author: shoupeng.wei@hand-china.com
 * @Created at: 2017年8月21日22:24:06
 * @Description:退款单明细Job对应请求体
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ltItem {

    @XmlElements(value = @XmlElement(name = "item"))
    private List<refundEntryItem> items;

    public List<refundEntryItem> getItems() {
        return items;
    }

    public void setItems(List<refundEntryItem> items) {
        this.items = items;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class refundEntryItem{

        @XmlElement(name = "ZSHDDH")
        private String ZSHDDH;

        @XmlElement(name = "ZCKH")
        private Long ZCKH;

        @XmlElement(name = "ZYWLX")
        private String ZYWLX;

        @XmlElement(name = "ZZFLX")
        private String ZZFLX;

        @XmlElement(name = "ZY01")
        private String ZY01;

        @XmlElement(name = "BUKRS")
        private String BUKRS;

        @XmlElement(name = "ZKUNNR")
        private String ZKUNNR;

        @XmlElement(name = "EXORD")
        private String EXORD;

        @XmlElement(name = "ZMATNR")
        private String ZMATNR;

        @XmlElement(name = "ZJYRQ")
        private String ZJYRQ;

        @XmlElement(name = "ZJYSJ")
        private String ZJYSJ;

        @XmlElement(name = "ZZJE")
        private String ZZJE;

        @XmlElement(name = "ZJYMS")
        private String ZJYMS;

        @XmlElement(name = "ZY03")
        private String ZY03;

        @XmlElement(name = "ZY04")
        private String ZY04;

        @XmlElement(name = "ZY05")
        private String ZY05;

        @XmlElement(name = "ZY06")
        private String ZY06;

        @XmlElement(name = "ZY07")
        private String ZY07;

        @XmlElement(name = "ZY08")
        private String ZY08;

        @XmlElement(name = "ZY09")
        private String ZY09;

        @XmlElement(name = "ZY10")
        private String ZY10;

        @XmlElement(name = "ZY11")
        private String ZY11;

        @XmlElement(name = "ZY12")
        private String ZY12;

        public String getZSHDDH() {
            return ZSHDDH;
        }

        public void setZSHDDH(String ZSHDDH) {
            this.ZSHDDH = ZSHDDH;
        }

        public Long getZCKH() {
            return ZCKH;
        }

        public void setZCKH(Long ZCKH) {
            this.ZCKH = ZCKH;
        }

        public String getZYWLX() {
            return ZYWLX;
        }

        public void setZYWLX(String ZYWLX) {
            this.ZYWLX = ZYWLX;
        }

        public String getZZFLX() {
            return ZZFLX;
        }

        public void setZZFLX(String ZZFLX) {
            this.ZZFLX = ZZFLX;
        }

        public String getZY01() {
            return ZY01;
        }

        public void setZY01(String ZY01) {
            this.ZY01 = ZY01;
        }

        public String getBUKRS() {
            return BUKRS;
        }

        public void setBUKRS(String BUKRS) {
            this.BUKRS = BUKRS;
        }

        public String getZKUNNR() {
            return ZKUNNR;
        }

        public void setZKUNNR(String ZKUNNR) {
            this.ZKUNNR = ZKUNNR;
        }

        public String getEXORD() {
            return EXORD;
        }

        public void setEXORD(String EXORD) {
            this.EXORD = EXORD;
        }

        public String getZMATNR() {
            return ZMATNR;
        }

        public void setZMATNR(String ZMATNR) {
            this.ZMATNR = ZMATNR;
        }

        public String getZJYRQ() {
            return ZJYRQ;
        }

        public void setZJYRQ(String ZJYRQ) {
            this.ZJYRQ = ZJYRQ;
        }

        public String getZJYSJ() {
            return ZJYSJ;
        }

        public void setZJYSJ(String ZJYSJ) {
            this.ZJYSJ = ZJYSJ;
        }

        public String getZZJE() {
            return ZZJE;
        }

        public void setZZJE(String ZZJE) {
            this.ZZJE = ZZJE;
        }

        public String getZJYMS() {
            return ZJYMS;
        }

        public void setZJYMS(String ZJYMS) {
            this.ZJYMS = ZJYMS;
        }

        public String getZY03() {
            return ZY03;
        }

        public void setZY03(String ZY03) {
            this.ZY03 = ZY03;
        }

        public String getZY04() {
            return ZY04;
        }

        public void setZY04(String ZY04) {
            this.ZY04 = ZY04;
        }

        public String getZY05() {
            return ZY05;
        }

        public void setZY05(String ZY05) {
            this.ZY05 = ZY05;
        }

        public String getZY06() {
            return ZY06;
        }

        public void setZY06(String ZY06) {
            this.ZY06 = ZY06;
        }

        public String getZY07() {
            return ZY07;
        }

        public void setZY07(String ZY07) {
            this.ZY07 = ZY07;
        }

        public String getZY08() {
            return ZY08;
        }

        public void setZY08(String ZY08) {
            this.ZY08 = ZY08;
        }

        public String getZY09() {
            return ZY09;
        }

        public void setZY09(String ZY09) {
            this.ZY09 = ZY09;
        }

        public String getZY10() {
            return ZY10;
        }

        public void setZY10(String ZY10) {
            this.ZY10 = ZY10;
        }

        public String getZY11() {
            return ZY11;
        }

        public void setZY11(String ZY11) {
            this.ZY11 = ZY11;
        }

        public String getZY12() {
            return ZY12;
        }

        public void setZY12(String ZY12) {
            this.ZY12 = ZY12;
        }
    }
}
