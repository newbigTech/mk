package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 订单更新retail中响应信息item类
 * @date 2017/8/18 16:15
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateReturnItem {

    @XmlElement
    private String VBELN;

    @XmlElement
    private String EBELN;

    @XmlElement
    private String TYPE;

    @XmlElement
    private String MSG;

    @XmlElement
    private String TYPE1;

    @XmlElement
    private String MSG1;

    public String getVBELN() {
        return VBELN;
    }

    public void setVBELN(String VBELN) {
        this.VBELN = VBELN;
    }

    public String getEBELN() {
        return EBELN;
    }

    public void setEBELN(String EBELN) {
        this.EBELN = EBELN;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    public String getTYPE1() {
        return TYPE1;
    }

    public void setTYPE1(String TYPE1) {
        this.TYPE1 = TYPE1;
    }

    public String getMSG1() {
        return MSG1;
    }

    public void setMSG1(String MSG1) {
        this.MSG1 = MSG1;
    }
}
