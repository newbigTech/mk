package com.hand.hmall.mst.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 支线运费对象dto
 * @date 2017/7/10 14:37
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_MST_SUBCARRIAGE")
public class Subcarriage extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column
    private Long subcarriageId;

    @Column
    private Long logisticscoId;

    @Column
    @ExcelVOAttribute(name = "承运商类型", column = "B", isExport = true)
    private String shippingType;

    @Column
    @ExcelVOAttribute(name = "区编码", column = "C", isExport = true)
    private String districtCode;

    @Column
    @ExcelVOAttribute(name = "单位体积运费", column = "D", isExport = true)
    private BigDecimal priceRate;

    @Column
    @ExcelVOAttribute(name = "最低运费", column = "E", isExport = true)
    private BigDecimal leastCarriage;

    @Column
    @ExcelVOAttribute(name = "状态", column = "J", isExport = true)
    private String status;

    @Transient
    private String logisticscoName;//承运商名称

    @Transient
    private String cityName;//市名称

    @Transient
    @ExcelVOAttribute(name = "承运商编码", column = "A", isExport = true)
    private String logisticscoCode;//承运商编码

    @ExcelVOAttribute(name = "始发地", column = "G", isExport = true)
    private String origin;

    @ExcelVOAttribute(name = "计价方式", column = "H", isExport = true)
    private String priceMode;

    @ExcelVOAttribute(name = "差额比例", column = "I", isExport = true)
    private String difference;

    @ExcelVOAttribute(name = "基础运费", column = "F", isExport = true)
    private BigDecimal basicExpense;

    @Transient
    private String originName; //始发地名称

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
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

    public BigDecimal getBasicExpense() {
        return basicExpense;
    }

    public void setBasicExpense(BigDecimal basicExpense) {
        this.basicExpense = basicExpense;
    }

    public String getLogisticscoCode() {
        return logisticscoCode;
    }

    public void setLogisticscoCode(String logisticscoCode) {
        this.logisticscoCode = logisticscoCode;
    }

    public String getLogisticscoName() {
        return logisticscoName;
    }

    public void setLogisticscoName(String logisticscoName) {
        this.logisticscoName = logisticscoName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setSubcarriageId(Long subcarriageId) {
        this.subcarriageId = subcarriageId;
    }

    public Long getSubcarriageId() {
        return subcarriageId;
    }

    public void setLogisticscoId(Long logisticscoId) {
        this.logisticscoId = logisticscoId;
    }

    public Long getLogisticscoId() {
        return logisticscoId;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public BigDecimal getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(BigDecimal priceRate) {
        this.priceRate = priceRate;
    }

    public BigDecimal getLeastCarriage() {
        return leastCarriage;
    }

    public void setLeastCarriage(BigDecimal leastCarriage) {
        this.leastCarriage = leastCarriage;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
