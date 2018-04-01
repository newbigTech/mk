package com.hand.hmall.dto;

import javax.persistence.*;

@Entity
@Table(name = "HMALL_MST_MAINCARRIAGE")
public class HamllMstMainCarriage {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_MAINCARRIAGE_S.nextval from dual")
    private Long maincarriageId;

    /**
     * 承运商
     */
    private Long logisticscoId;

    /**
     * 承运商类型
     */
    private String shippingType;

    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 单位体积运费
     */
    private Double priceRate;

    /**
     * 最低运费
     */
    private Double leastCarriage;

    /**
     * 状态
     */
    private String status;

    /**
     * 版本号
     */
    private Long objectVersionNumber;
    /**
     * 始发地
     */
    private String origin;

    /**
     * 计价方式
     */
    private String priceMode;

    /**
     * 差额比例
     */
    private String difference;

    /**
     * 基础运费
     */
    private Double basicExpense;


    public Long getMaincarriageId() {
        return maincarriageId;
    }

    public void setMaincarriageId(Long maincarriageId) {
        this.maincarriageId = maincarriageId;
    }

    public Long getLogisticscoId() {
        return logisticscoId;
    }

    public void setLogisticscoId(Long logisticscoId) {
        this.logisticscoId = logisticscoId;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Double getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(Double priceRate) {
        this.priceRate = priceRate;
    }

    public Double getLeastCarriage() {
        return leastCarriage;
    }

    public void setLeastCarriage(Double leastCarriage) {
        this.leastCarriage = leastCarriage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPriceMode() {
        return priceMode;
    }

    public void setPriceMode(String priceMode) {
        this.priceMode = priceMode;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public Double getBasicExpense() {
        return basicExpense;
    }

    public void setBasicExpense(Double basicExpense) {
        this.basicExpense = basicExpense;
    }

    @Override
    public String toString() {
        return "{\"HamllMstMainCarriage\":{"
                + "                        \"maincarriageId\":\"" + maincarriageId + "\""
                + ",                         \"logisticscoId\":\"" + logisticscoId + "\""
                + ",                         \"shippingType\":\"" + shippingType + "\""
                + ",                         \"cityCode\":\"" + cityCode + "\""
                + ",                         \"priceRate\":\"" + priceRate + "\""
                + ",                         \"leastCarriage\":\"" + leastCarriage + "\""
                + ",                         \"status\":\"" + status + "\""
                + ",                         \"objectVersionNumber\":\"" + objectVersionNumber + "\""
                + "}}";
    }
}