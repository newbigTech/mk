package com.hand.hmall.ws.entities;

import java.io.Serializable;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name MstFabricResponseModel
 * @description
 * @date 2017/7/4
 */
public class MstFabricResponseModel implements Serializable {
    private String matnr;

    private String m;

    private String msg;

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
