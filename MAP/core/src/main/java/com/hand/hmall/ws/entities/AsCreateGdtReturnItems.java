package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * author: zhangzilong
 * name: AsCreateGdtReturnItems.java
 * discription:
 * date: 2017/8/29
 * version: 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AsCreateGdtReturnItems {

    @XmlElement(name = "TYPE")
    private String type;

    @XmlElement(name = "ID")
    private String id;

    @XmlElement(name = "NUMBER")
    private String number;

    @XmlElement(name = "MESSAGE")
    private String message;

    @XmlElement(name = "LOG_NO")
    private String logNo;

    @XmlElement(name = "LOG_MSG_NO")
    private String logMesNo;

    @XmlElement(name = "MESSAGE_V1")
    private String messageV1;

    @XmlElement(name = "MESSAGE_V2")
    private String messageV2;

    @XmlElement(name = "MESSAGE_V3")
    private String messageV3;

    @XmlElement(name = "MESSAGE_V4")
    private String messageV4;

    @XmlElement(name = "PARAMETER")
    private String patameter;

    @XmlElement(name = "ROW")
    private String row;

    @XmlElement(name = "FIELD")
    private String field;

    @XmlElement(name = "SYSTEM")
    private String system;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogNo() {
        return logNo;
    }

    public void setLogNo(String logNo) {
        this.logNo = logNo;
    }

    public String getLogMesNo() {
        return logMesNo;
    }

    public void setLogMesNo(String logMesNo) {
        this.logMesNo = logMesNo;
    }

    public String getMessageV1() {
        return messageV1;
    }

    public void setMessageV1(String messageV1) {
        this.messageV1 = messageV1;
    }

    public String getMessageV2() {
        return messageV2;
    }

    public void setMessageV2(String messageV2) {
        this.messageV2 = messageV2;
    }

    public String getMessageV3() {
        return messageV3;
    }

    public void setMessageV3(String messageV3) {
        this.messageV3 = messageV3;
    }

    public String getMessageV4() {
        return messageV4;
    }

    public void setMessageV4(String messageV4) {
        this.messageV4 = messageV4;
    }

    public String getPatameter() {
        return patameter;
    }

    public void setPatameter(String patameter) {
        this.patameter = patameter;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
