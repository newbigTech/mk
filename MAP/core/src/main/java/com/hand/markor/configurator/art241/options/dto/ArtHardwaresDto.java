package com.hand.markor.configurator.art241.options.dto;

import com.hand.common.util.ExcelVOAttribute;

/**
 * @Author:zhangyanan
 * @Description: 五金选项实体类(供Excel导入使用)
 * @Date:Crated in 15:40 2018/1/29
 * @Modified By:
 */
public class ArtHardwaresDto {

    public static final String DEFAULT_SHEET_NAME = "五金选项";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";

    /**
     * 平台编码
     */
    @ExcelVOAttribute(name = "平台编码", column = "A", isExport = true)
    private String platformCode;

    /**
     * 拉手组合
     */
    @ExcelVOAttribute(name = "拉手组合", column = "B", isExport = true)
    private String handlesComCode;

    /**
     * 拉手颜色
     */
    @ExcelVOAttribute(name = "拉手颜色", column = "C", isExport = true)
    private String handlesColor;

    /**
     * 配置包编码
     */
    @ExcelVOAttribute(name = "配置包编码", column = "D", isExport = true)
    private String optionMatCode;

    /**
     * 材料编码
     */
    @ExcelVOAttribute(name = "材料编码", column = "E", isExport = true)
    private String optionValueMatCode;

    public static String getDefaultSheetName() {
        return DEFAULT_SHEET_NAME;
    }

    public static String getDefaultExcelFileName() {
        return DEFAULT_EXCEL_FILE_NAME;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getHandlesComCode() {
        return handlesComCode;
    }

    public void setHandlesComCode(String handlesComCode) {
        this.handlesComCode = handlesComCode;
    }

    public String getHandlesColor() {
        return handlesColor;
    }

    public void setHandlesColor(String handlesColor) {
        this.handlesColor = handlesColor;
    }

    public String getOptionMatCode() {
        return optionMatCode;
    }

    public void setOptionMatCode(String optionMatCode) {
        this.optionMatCode = optionMatCode;
    }

    public String getOptionValueMatCode() {
        return optionValueMatCode;
    }

    public void setOptionValueMatCode(String optionValueMatCode) {
        this.optionValueMatCode = optionValueMatCode;
    }
}
