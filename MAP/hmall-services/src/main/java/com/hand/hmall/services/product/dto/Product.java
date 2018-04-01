package com.hand.hmall.services.product.dto;

import java.io.Serializable;

/**
 * 商品DTO对象
 *
 * @author alaowan
 * Created at 2017/12/26 13:46
 * @description
 */
public class Product implements Serializable {

    private String code;

    private String name;

    private String warehouse;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
}
