package com.hand.hmall.pojo;

/**
 * @author 马君
 * @version 0.1
 * @name PriceResponseRow
 * @description 价格计算返回结果行，记录每一个V码价格计算的结果
 * @date 2017/7/27 17:41
 */
public class PriceResponseRow {
    // v码计算价格是否成功
    private boolean success;
    // 识别码
    private Long cdkey;
    // vCode
    private String vCode;
    // 错误消息
    private String message;
    // 计算的价格
    private Double totalPrice;
    //折扣后价格
    private Double discountPrice;
    // 平台号
    private String productCode;
    // 频道
    private String odtype;
    //折扣价开始时间
    private String startTime;
    //折扣价开始时间
    private String endTime;
    //折扣类型
    private String discountType;


    public PriceResponseRow() {
    }

    public PriceResponseRow(boolean success, String message, PriceRequestData priceRequestData) {
        this.success = success;
        this.message = message;
        this.productCode = priceRequestData.getProductCode();
        this.vCode = priceRequestData.getvCode();
        this.odtype = priceRequestData.getOdtype();
        this.cdkey = priceRequestData.getCdkey();
    }

    public PriceResponseRow(boolean success, String message, Double totalPrice, PriceRequestData priceRequestData) {
        this.success = success;
        this.message = message;
        this.totalPrice = totalPrice;
        this.productCode = priceRequestData.getProductCode();
        this.vCode = priceRequestData.getvCode();
        this.odtype = priceRequestData.getOdtype();
        this.cdkey = priceRequestData.getCdkey();
    }

    public PriceResponseRow(boolean success, String message, Double totalPrice, PriceRequestData priceRequestData, Double discountPrice, String startTime, String endTime, String discountType) {
        this.success = success;
        this.message = message;
        this.totalPrice = totalPrice;
        this.productCode = priceRequestData.getProductCode();
        this.discountPrice = discountPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.discountType = discountType;
        this.vCode = priceRequestData.getvCode();
        this.odtype = priceRequestData.getOdtype();
        this.cdkey = priceRequestData.getCdkey();
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getCdkey() {
        return cdkey;
    }

    public void setCdkey(Long cdkey) {
        this.cdkey = cdkey;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getOdtype() {
        return odtype;
    }

    public void setOdtype(String odtype) {
        this.odtype = odtype;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }
}
