package com.hand.hmall.services.as.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * @author liuhongxi
 * @version 0.1
 * @name AsReturnBack
 * @description retail推送退货单接口的返回Dto
 * @date 2017/8/23
 */
@XmlAccessorType(XmlAccessType.NONE)
public class AsReturnBack implements Serializable{

    @XmlElement
    private String sapCode;

    @XmlElement
    private String status;

    @XmlElement
    private String note;

    @XmlElement
    private String code;

    @XmlElement
    private String codeMsg;

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(String codeMsg) {
        this.codeMsg = codeMsg;
    }
}
