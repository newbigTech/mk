package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author 唐磊
 * @version 0.1
 * @name:
 * @Description: 订单推送 Hmall->retail
 * @date 2017/6/8 10:35
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnItem {
    @XmlElement
    private String TYPE;

    @XmlElement
    private String ID;

    @XmlElement
    private String NUMBER;

    @XmlElement
    private String MESSAGE;

    @XmlElement
    private String LOG_NO;

    @XmlElement
    private String LOG_MSG_NO;

    @XmlElement
    private String MESSAGE_V1;

    @XmlElement
    private String MESSAGE_V2;

    @XmlElement
    private String MESSAGE_V3;

    @XmlElement
    private String MESSAGE_V4;

    @XmlElement
    private String PARAMETER;

    @XmlElement
    private String FIELD;

    @XmlElement
    private String SYSTEM;

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getLOG_NO() {
        return LOG_NO;
    }

    public void setLOG_NO(String LOG_NO) {
        this.LOG_NO = LOG_NO;
    }

    public String getLOG_MSG_NO() {
        return LOG_MSG_NO;
    }

    public void setLOG_MSG_NO(String LOG_MSG_NO) {
        this.LOG_MSG_NO = LOG_MSG_NO;
    }

    public String getMESSAGE_V1() {
        return MESSAGE_V1;
    }

    public void setMESSAGE_V1(String MESSAGE_V1) {
        this.MESSAGE_V1 = MESSAGE_V1;
    }

    public String getMESSAGE_V2() {
        return MESSAGE_V2;
    }

    public void setMESSAGE_V2(String MESSAGE_V2) {
        this.MESSAGE_V2 = MESSAGE_V2;
    }

    public String getMESSAGE_V3() {
        return MESSAGE_V3;
    }

    public void setMESSAGE_V3(String MESSAGE_V3) {
        this.MESSAGE_V3 = MESSAGE_V3;
    }

    public String getMESSAGE_V4() {
        return MESSAGE_V4;
    }

    public void setMESSAGE_V4(String MESSAGE_V4) {
        this.MESSAGE_V4 = MESSAGE_V4;
    }

    public String getPARAMETER() {
        return PARAMETER;
    }

    public void setPARAMETER(String PARAMETER) {
        this.PARAMETER = PARAMETER;
    }

    public String getFIELD() {
        return FIELD;
    }

    public void setFIELD(String FIELD) {
        this.FIELD = FIELD;
    }

    public String getSYSTEM() {
        return SYSTEM;
    }

    public void setSYSTEM(String SYSTEM) {
        this.SYSTEM = SYSTEM;
    }
}
