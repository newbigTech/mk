package com.hand.hmall.services.order.dto;

import java.io.Serializable;

/**
 * 订单行DTO
 *
 * @author alaowan
 * Created at 2017/12/26 14:17
 * @description
 */
public class OrderEntry implements Serializable {

    private String code;

    private String pin;

    private String vproductCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getVproductCode() {
        return vproductCode;
    }

    public void setVproductCode(String vproductCode) {
        this.vproductCode = vproductCode;
    }
}
