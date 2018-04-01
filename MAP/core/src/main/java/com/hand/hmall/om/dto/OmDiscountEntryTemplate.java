package com.hand.hmall.om.dto;


/**
 * 价格折扣导入DTO
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import java.util.Date;

@ExtensionAttribute(disable = true)
public class OmDiscountEntryTemplate {

    public static final String DEFAULT_SHEET_NAME = "价格折扣行模板";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";

    @ExcelVOAttribute(name = "商品编号", column = "A", isExport = true)
    private String productCode;

    @ExcelVOAttribute(name = "商品V码", column = "B", isExport = true)
    private String vcode;

    @ExcelVOAttribute(name = "折扣类型", column = "C", isExport = true)
    private String discountType;

    @ExcelVOAttribute(name = "折扣", column = "D", isExport = true)
    private Double discount;

    @ExcelVOAttribute(name = "开始时间", column = "E", isExport = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ExcelVOAttribute(name = "结束时间", column = "F", isExport = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }
}
