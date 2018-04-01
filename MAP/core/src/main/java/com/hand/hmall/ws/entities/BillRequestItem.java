package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:zhangyanan
 * @Description:
 * @Date:Crated in 14:32 2017/9/22
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BillRequestItem {

    @XmlElements(value = @XmlElement(name = "item"))
    private List<BillEntryItem> items = new ArrayList<>();

    public List<BillEntryItem> getItems() {
        return items;
    }

    public void setItems(List<BillEntryItem> items) {
        this.items = items;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BillEntryItem {
        /**
         * 第三方业务单据号
         */
        @XmlElement(name = "ZSHDDH")
        private String zshddh;

        /**
         * HMALL单据号
         */
        @XmlElement(name = "ZCKH")
        private String zckh;

        /**
         * 账务类型
         */
        @XmlElement(name = "ZYWLX")
        private String zywlx;

        /**
         * 支付方式
         */
        @XmlElement(name = "ZZFLX")
        private String zzflx;

        /**
         * 单据类型（目前预留）
         */
        @XmlElement(name = "ZY01")
        private String zy01;

        /**
         * 预留
         */
        @XmlElement(name = "ZY02")
        private String zy02;

        /**
         * 公司代码
         */
        @XmlElement(name = "BUKRS")
        private String bukrs;

        /**
         * 用户代码
         */
        @XmlElement(name = "ZKUNNR")
        private String zkunnr;

        /**
         * 商城订单号
         */
        @XmlElement(name = "EXORD")
        private String exord;

        /**
         * 预留
         */
        @XmlElement(name = "ZMATNR")
        private String zmatnr;

        /**
         * 交易日期
         */
        @XmlElement(name = "ZJYRQ")
        private String zjyrq;

        /**
         * 交易时间
         */
        @XmlElement(name = "ZJYSJ")
        private String zjysj;

        /**
         * 记账金额
         */
        @XmlElement(name = "ZZJE")
        private String zzje;

        /**
         * 预留
         */
        @XmlElement(name = "ZJYMS")
        private String zjyms;

        /**
         * 预留
         */
        @XmlElement(name = "ZY09")
        private String zy09;

        /**
         * 商城拼接号
         */
        @XmlElement(name = "ZY06")
        private String zy06;

        /**
         * 金额预留
         */
        @XmlElement(name = "ZY03")
        private String zy03;

        /**
         * 日期预留
         */
        @XmlElement(name = "ZY04")
        private String zy04;

        /**
         * 时间预留
         */
        @XmlElement(name = "ZY05")
        private String zy05;

        /**
         * 备注
         */
        @XmlElement(name = "ZY07")
        private String zy07;

        /**
         * 账单流水号
         */
        @XmlElement(name = "ZY08")
        private String zy08;

        /**
         * 预留
         */
        @XmlElement(name = "ZY10")
        private String zy10;

        /**
         * 预留
         */
        @XmlElement(name = "ZY11")
        private String zy11;

        /**
         * 预留
         */
        @XmlElement(name = "ZY12")
        private String zy12;

        public String getZshddh() {
            return zshddh;
        }

        public void setZshddh(String zshddh) {
            this.zshddh = zshddh;
        }

        public String getZckh() {
            return zckh;
        }

        public void setZckh(String zckh) {
            this.zckh = zckh;
        }

        public String getZywlx() {
            return zywlx;
        }

        public void setZywlx(String zywlx) {
            this.zywlx = zywlx;
        }

        public String getZzflx() {
            return zzflx;
        }

        public void setZzflx(String zzflx) {
            this.zzflx = zzflx;
        }

        public String getZy01() {
            return zy01;
        }

        public void setZy01(String zy01) {
            this.zy01 = zy01;
        }

        public String getZy02() {
            return zy02;
        }

        public void setZy02(String zy02) {
            this.zy02 = zy02;
        }

        public String getBukrs() {
            return bukrs;
        }

        public void setBukrs(String bukrs) {
            this.bukrs = bukrs;
        }

        public String getZkunnr() {
            return zkunnr;
        }

        public void setZkunnr(String zkunnr) {
            this.zkunnr = zkunnr;
        }

        public String getExord() {
            return exord;
        }

        public void setExord(String exord) {
            this.exord = exord;
        }

        public String getZmatnr() {
            return zmatnr;
        }

        public void setZmatnr(String zmatnr) {
            this.zmatnr = zmatnr;
        }

        public String getZjyrq() {
            return zjyrq;
        }

        public void setZjyrq(String zjyrq) {
            this.zjyrq = zjyrq;
        }

        public String getZjysj() {
            return zjysj;
        }

        public void setZjysj(String zjysj) {
            this.zjysj = zjysj;
        }

        public String getZzje() {
            return zzje;
        }

        public void setZzje(String zzje) {
            this.zzje = zzje;
        }

        public String getZjyms() {
            return zjyms;
        }

        public void setZjyms(String zjyms) {
            this.zjyms = zjyms;
        }

        public String getZy09() {
            return zy09;
        }

        public void setZy09(String zy09) {
            this.zy09 = zy09;
        }

        public String getZy06() {
            return zy06;
        }

        public void setZy06(String zy06) {
            this.zy06 = zy06;
        }

        public String getZy03() {
            return zy03;
        }

        public void setZy03(String zy03) {
            this.zy03 = zy03;
        }

        public String getZy04() {
            return zy04;
        }

        public void setZy04(String zy04) {
            this.zy04 = zy04;
        }

        public String getZy05() {
            return zy05;
        }

        public void setZy05(String zy05) {
            this.zy05 = zy05;
        }

        public String getZy07() {
            return zy07;
        }

        public void setZy07(String zy07) {
            this.zy07 = zy07;
        }

        public String getZy08() {
            return zy08;
        }

        public void setZy08(String zy08) {
            this.zy08 = zy08;
        }

        public String getZy10() {
            return zy10;
        }

        public void setZy10(String zy10) {
            this.zy10 = zy10;
        }

        public String getZy11() {
            return zy11;
        }

        public void setZy11(String zy11) {
            this.zy11 = zy11;
        }

        public String getZy12() {
            return zy12;
        }

        public void setZy12(String zy12) {
            this.zy12 = zy12;
        }
    }
}
