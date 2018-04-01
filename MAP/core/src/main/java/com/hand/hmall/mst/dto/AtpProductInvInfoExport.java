package com.hand.hmall.mst.dto;

/**
 * @author zhangmeng
 * @version 0.1
 * @name AtpProductInvInfoExport
 * @description 导出库存实体类
 * @date 2017/9/25
 */

import com.hand.common.util.ExcelVOAttribute;

public class AtpProductInvInfoExport {

    @ExcelVOAttribute(name = "上架商品编码", column = "A", isExport = true)
    private String matnr;

    private Double availableQuantity;

    @ExcelVOAttribute(name = "商品库存", column = "C", isExport = true)
    private Integer quantity;

    @ExcelVOAttribute(name = "商品名称", column = "B", isExport = true)
    private String name;

    private String productId;

    private String code;

    private String isSuit;

    public String getIsSuit() {
        return isSuit;
    }

    public void setIsSuit(String isSuit) {
        this.isSuit = isSuit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public Double getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Double availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
