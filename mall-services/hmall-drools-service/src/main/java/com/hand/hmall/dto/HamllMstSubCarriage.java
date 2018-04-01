package com.hand.hmall.dto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HMALL_MST_SUBCARRIAGE")
public class HamllMstSubCarriage {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_SUBCARRIAGE_S.nextval from dual")
    private Long subcarriageId;

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
    private String districtCode;

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

    public Long getSubcarriageId() {
        return subcarriageId;
    }

    public void setSubcarriageId(Long subcarriageId) {
        this.subcarriageId = subcarriageId;
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

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
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


}