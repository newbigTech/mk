package com.hand.hmall.om.dto;

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name VirtualOrderImportDto
 * @description 订单页面虚拟订单导出Excel模板对应的dto
 * @date 2017/8/8
 */
@ExtensionAttribute(disable = true)
public class VirtualOrderImportDto {

    public static final String DEFAULT_SHEET_NAME = "虚拟订单";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";

    @ExcelVOAttribute(name = "上架商品编码", column = "A", isExport = true)
    private String tmallProductCode;

    @ExcelVOAttribute(name = "商品库存", column = "B", isExport = true)
    private String tmallProductInventory;

    public String getTmallProductCode() {
        return tmallProductCode;
    }

    public void setTmallProductCode(String tmallProductCode) {
        this.tmallProductCode = tmallProductCode;
    }

    public String getTmallProductInventory() {
        return tmallProductInventory;
    }

    public void setTmallProductInventory(String tmallProductInventory) {
        this.tmallProductInventory = tmallProductInventory;
    }
}
