package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * author: zhangzilong
 * name: AsChangeGdtReturnItems.java
 * discription:
 * date: 2017/8/29
 * version: 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AsChangeGdtReturnItems {

    @XmlElement(name = "EBELN")
    private String ebeln;

    @XmlElement(name = "VBELN")
    private String vbeln;

    @XmlElement(name = "MSG")
    private String msg;

    @XmlElement(name = "TYPE1")
    private String type1;

    @XmlElement(name = "MSG1")
    private String msg1;

    @XmlElement(name = "TYPE")
    private String type;

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public String getVbeln() {
        return vbeln;
    }

    public void setVbeln(String vbeln) {
        this.vbeln = vbeln;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getMsg1() {
        return msg1;
    }

    public void setMsg1(String msg1) {
        this.msg1 = msg1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
