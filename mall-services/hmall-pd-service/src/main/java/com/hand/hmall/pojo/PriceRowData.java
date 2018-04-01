package com.hand.hmall.pojo;

/**
 * @author 马君
 * @version 0.1
 * @name PriceRowData
 * @description 价格行对象
 * @date 2017/7/7 9:53
 */
public class PriceRowData {
    /*
    * 零部件商品编码
    * */
    private String partCode;
    /*
    * 零部件数量
    * */
    private Integer quantity;

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
