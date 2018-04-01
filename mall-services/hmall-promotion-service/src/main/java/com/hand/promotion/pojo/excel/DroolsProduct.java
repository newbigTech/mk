package com.hand.promotion.pojo.excel;

import java.io.Serializable;

/**
 * Excel导入商品数据对应实体类
 * Created by darkdog on 2018/2/5.
 */
public class DroolsProduct implements Serializable{

    private String id;
    /**
     *  关联商品主键
     */
    private String productId;
    /**
     * 商品编码
     */
    private String productCode;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 关联Excel表的ID
     */
    private String excelId;
    /**
     * 是否导入成功
     */
    private String isSuccess;

    /**
     * 导入时间
     */
    private String importDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExcelId() {
        return excelId;
    }

    public void setExcelId(String excelId) {
        this.excelId = excelId;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }
}
