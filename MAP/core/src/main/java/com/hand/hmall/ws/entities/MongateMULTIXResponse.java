package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.ResponseBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author: zhangzilong
 * name: MongateMULTIXRequest.java
 * discription: 不同内容群发接口
 * date: 2017/11/20
 * version: 0.1
 */
@XmlRootElement(name = "MongateMULTIXSendResponse", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class MongateMULTIXResponse extends ResponseBody{

    @XmlElement
    private String MongateMULTIXSendResult;

    public String getMongateMULTIXSendResult() {
        return MongateMULTIXSendResult;
    }

    public void setMongateMULTIXSendResult(String mongateMULTIXSendResult) {
        MongateMULTIXSendResult = mongateMULTIXSendResult;
    }
}
