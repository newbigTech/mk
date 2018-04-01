package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * author: zhangzilong
 * name: AsGdtCondtionItem.java
 * discription:
 * date: 2017/8/29
 * version: 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AsGdtCondtionItem {

    @XmlElement(name = "POSNR")
    private String posnr;

    @XmlElement(name = "KSCHL")
    private String kschl;

    @XmlElement(name = "KBETR")
    private String kbetr;

    @XmlElement(name = "CURRENCY")
    private String currency;

    @XmlElement(name = "ZY01")
    private String zy01;

    @XmlElement(name = "ZY02")
    private String zy02;

    @XmlElement(name = "ZY03")
    private String zy03;

    @XmlElement(name = "ZY04")
    private String zy04;

    @XmlElement(name = "ZY05")
    private String zy05;

    public String getPosnr() {
        return posnr;
    }

    public void setPosnr(String posnr) {
        this.posnr = posnr;
    }

    public String getKschl() {
        return kschl;
    }

    public void setKschl(String kschl) {
        this.kschl = kschl;
    }

    public String getKbetr() {
        return kbetr;
    }

    public void setKbetr(String kbetr) {
        this.kbetr = kbetr;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
}

