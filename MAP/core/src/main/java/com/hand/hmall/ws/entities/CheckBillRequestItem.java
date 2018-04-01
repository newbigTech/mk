package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Author:zhangyanan
 * @Description:对账Item
 * @Date:Crated in 16:16 2017/10/17
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckBillRequestItem {

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
}
