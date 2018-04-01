package com.hand.hmall.ws.entities;

import com.markor.map.framework.soapclient.entities.RequestBody;

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
@XmlRootElement(name = "MongateMULTIXSend", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class MongateMULTIXRequest extends RequestBody{

    @XmlElement
    private String userId;
    
    @XmlElement
    private String password;
    
    @XmlElement
    private String multixmt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMultixmt() {
        return multixmt;
    }

    public void setMultixmt(String multixmt) {
        this.multixmt = multixmt;
    }
}
