package com.hand.promotion.pojo.order;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class GiftPojo implements java.io.Serializable {
    private String productId;
    private Integer quantity;
    private Double basePrice;
    private String vProductCode;
    private String defaultDelivery;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getvProductCode() {
        return vProductCode;
    }

    public void setvProductCode(String vProductCode) {
        this.vProductCode = vProductCode;
    }

    public String getDefaultDelivery() {
        return defaultDelivery;
    }

    public void setDefaultDelivery(String defaultDelivery) {
        this.defaultDelivery = defaultDelivery;
    }
}
