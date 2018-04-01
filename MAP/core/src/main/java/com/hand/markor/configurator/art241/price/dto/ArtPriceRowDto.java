package com.hand.markor.configurator.art241.price.dto;

import com.hand.common.util.ExcelVOAttribute;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author:zhangyanan
 * @Description: 241价格维护界面导入实体类
 * @Date:Crated in 15:27 2018/2/27
 * @Modified By:
 */
public class ArtPriceRowDto {

    public static final String DEFAULT_SHEET_NAME = "价格维护";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";

    /**
     * 商品编码
     */
    @ExcelVOAttribute(name = "商品编码", column = "A")
    private String productCode;

    /**
     * 品牌
     */
    @ExcelVOAttribute(name = "品牌", column = "B")
    private String priceBrandCode;

    /**
     * 系列
     */
    @ExcelVOAttribute(name = "系列", column = "C")
    private String priceCode;

    /**
     * 价格类型
     */
    @ExcelVOAttribute(name = "价格类型", column = "D")
    private String priceType;

    /**
     * 框架等级
     */
    @ExcelVOAttribute(name = "框架等级", column = "E")
    private String productGrade;

    /**
     * 频道
     */
    @ExcelVOAttribute(name = "频道", column = "F")
    private String odtype;

    /**
     * 价目表
     */
    @ExcelVOAttribute(name = "价目表", column = "G")
    private String priceGroup;

    /**
     * 开始时间
     */
    @ExcelVOAttribute(name = "开始时间", column = "H")
    private String enableTime;

    /**
     * 结束时间
     */
    @ExcelVOAttribute(name = "结束时间", column = "I")
    private String disableTime;

    /**
     * 是否一口价
     */
    @ExcelVOAttribute(name = "是否一口价", column = "J")
    private String isBottom;

    /**
     * 基础销售价格
     */
    @ExcelVOAttribute(name = "基础销售价格", column = "K")
    private BigDecimal basePrice;

    /**
     * 最低价
     */
    @ExcelVOAttribute(name = "最低价", column = "L")
    private BigDecimal bottomPrice;

    /**
     * 转换单位
     */
    @ExcelVOAttribute(name = "转换单位", column = "M")
    private String saleUnit;

    /**
     * 单位转换率
     */
    @ExcelVOAttribute(name = "转换单位", column = "N")
    private BigDecimal rate;

    /**
     * 是否覆盖原记录  Y/N
     */
    @ExcelVOAttribute(name = "是否覆盖原记录", column = "O")
    private String flag;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPriceBrandCode() {
        return priceBrandCode;
    }

    public void setPriceBrandCode(String priceBrandCode) {
        this.priceBrandCode = priceBrandCode;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getProductGrade() {
        return productGrade;
    }

    public void setProductGrade(String productGrade) {
        this.productGrade = productGrade;
    }

    public String getPriceGroup() {
        return priceGroup;
    }

    public void setPriceGroup(String priceGroup) {
        this.priceGroup = priceGroup;
    }

    public String getOdtype() {
        return odtype;
    }

    public void setOdtype(String odtype) {
        this.odtype = odtype;
    }

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(String enableTime) {
        this.enableTime = enableTime;
    }

    public String getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(String disableTime) {
        this.disableTime = disableTime;
    }

    public String getIsBottom() {
        return isBottom;
    }

    public void setIsBottom(String isBottom) {
        this.isBottom = isBottom;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getBottomPrice() {
        return bottomPrice;
    }

    public void setBottomPrice(BigDecimal bottomPrice) {
        this.bottomPrice = bottomPrice;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
