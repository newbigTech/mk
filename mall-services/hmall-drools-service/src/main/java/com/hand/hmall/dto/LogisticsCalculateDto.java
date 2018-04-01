package com.hand.hmall.dto;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

/**
 * @author XinyangMei
 * @Title LogisticsCalculateDto
 * @Description desp
 * @date 2017/11/27 9:42
 */
public class LogisticsCalculateDto {

    //流水号
    private Long lineNumber;

    //物流单号，这个字段你别动，这是我用的，我传给你你再传回来就行
    private String logisticsNumber;

    //商品ID
    private Integer orderEntryId;

    //商品包装尺寸
    private String productPackSize;

    //城市code
    private String cityCode;

    //地区code
    private String districtCode;

    //数量
    private Integer quantity;

    //以下字段你算好set进来把这个List返回给我
    //安装费
    private Double installationFee;

    //主线运费
    private Double maincarriage;

    //支线运费
    private Double subcarriage;

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public Integer getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Integer orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    public String getProductPackSize() {
        return productPackSize;
    }

    public void setProductPackSize(String productPackSize) {
        this.productPackSize = productPackSize;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getInstallationFee() {
        return installationFee;
    }

    public void setInstallationFee(Double installationFee) {
        this.installationFee = installationFee;
    }

    public Double getMaincarriage() {
        return maincarriage;
    }

    public void setMaincarriage(Double maincarriage) {
        this.maincarriage = maincarriage;
    }

    public Double getSubcarriage() {
        return subcarriage;
    }

    public void setSubcarriage(Double subcarriage) {
        this.subcarriage = subcarriage;
    }
}
