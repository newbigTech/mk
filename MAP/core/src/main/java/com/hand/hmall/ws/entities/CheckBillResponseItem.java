package com.hand.hmall.ws.entities;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Author:zhangyanan
 * @Description:
 * @Date:Crated in 16:36 2017/10/17
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckBillResponseItem {

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
     * 商城拼接号
     */
    @XmlElement(name = "ZY06")
    private String zy06;

    /**
     * 账单流水号
     */
    @XmlElement(name = "ZY08")
    private String zy08;

    /**
     * 会计凭证号
     */
    @XmlElement(name = "BELNR")
    private String belnr;

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

    public String getZy06() {
        return zy06;
    }

    public void setZy06(String zy06) {
        this.zy06 = zy06;
    }

    public String getZy08() {
        return zy08;
    }

    public void setZy08(String zy08) {
        this.zy08 = zy08;
    }

    public String getBelnr() {
        return belnr;
    }

    public void setBelnr(String belnr) {
        this.belnr = belnr;
    }
}
