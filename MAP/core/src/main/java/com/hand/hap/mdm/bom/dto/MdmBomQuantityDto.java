package com.hand.hap.mdm.bom.dto;

import com.hand.common.util.ExcelVOAttribute;

/**
 * @Author:zhangyanan
 * @Description:实现选配值卷算用量Excel导出实体类
 * @Date:Crated in 15:28 2017/12/27
 * @Modified By:
 */
public class MdmBomQuantityDto {

    @ExcelVOAttribute(name = "选配值bomid", column = "A", isExport = true)
    private Long bomId;

    @ExcelVOAttribute(name = "选配值id", column = "B", isExport = true)
    private Long itemId;

    @ExcelVOAttribute(name = "选配值物料编码", column = "C", isExport = true)
    private String itemCode1;

    @ExcelVOAttribute(name = "选配值物料名称", column = "D", isExport = true)
    private String itemName1;

    @ExcelVOAttribute(name = "卷算用量", column = "E", isExport = true)
    private Double quantity;

    @ExcelVOAttribute(name = "单位", column = "F", isExport = true)
    private String unit;

    @ExcelVOAttribute(name = "平台id", column = "G", isExport = true)
    private Long platformId;

    @ExcelVOAttribute(name = "平台编码", column = "H", isExport = true)
    private String itemCode2;

    @ExcelVOAttribute(name = "平台名称", column = "I", isExport = true)
    private String itemName2;

    @ExcelVOAttribute(name = "选配项id", column = "J", isExport = true)
    private Long chooseItemId;

    @ExcelVOAttribute(name = "选配项物料编码", column = "K", isExport = true)
    private String itemCode3;

    @ExcelVOAttribute(name = "选配项物料名称", column = "L", isExport = true)
    private String itemName3;

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public Long getChooseItemId() {
        return chooseItemId;
    }

    public void setChooseItemId(Long chooseItemId) {
        this.chooseItemId = chooseItemId;
    }

    public Long getBomId() {
        return bomId;
    }

    public void setBomId(Long bomId) {
        this.bomId = bomId;
    }

    public String getItemCode1() {
        return itemCode1;
    }

    public void setItemCode1(String itemCode1) {
        this.itemCode1 = itemCode1;
    }

    public String getItemName1() {
        return itemName1;
    }

    public void setItemName1(String itemName1) {
        this.itemName1 = itemName1;
    }

    public String getItemCode2() {
        return itemCode2;
    }

    public void setItemCode2(String itemCode2) {
        this.itemCode2 = itemCode2;
    }

    public String getItemName2() {
        return itemName2;
    }

    public void setItemName2(String itemName2) {
        this.itemName2 = itemName2;
    }

    public String getItemCode3() {
        return itemCode3;
    }

    public void setItemCode3(String itemCode3) {
        this.itemCode3 = itemCode3;
    }

    public String getItemName3() {
        return itemName3;
    }

    public void setItemName3(String itemName3) {
        this.itemName3 = itemName3;
    }
}
