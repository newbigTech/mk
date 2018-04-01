package com.hand.markor.configurator.art241.options.dto;

import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author baihua
 * @version 0.1
 * @name MdmItemValueOptionsDto$
 * @description $END$ 选项定义Excel导出实体类
 * @date 2018/1/29$
 */
@ExtensionAttribute(disable = true)
public class ArtOptionsMappingDto {

    public static final String DEFAULT_SHEET_NAME = "选项定义模板";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";

    private Long id;

    @ExcelVOAttribute(name = "平台编码", column = "A", isExport = true)
    private String platformCode;

    @ExcelVOAttribute(name = "选项类型", column = "B", isExport = true)
    private String optionType;

    @ExcelVOAttribute(name = "选项编码", column = "C", isExport = true)
    private String optionCode;

    @ExcelVOAttribute(name = "配置包编码", column = "D", isExport = true)
    private String optionMatCode;

    @ExcelVOAttribute(name = "物料编码", column = "E", isExport = true)
    private String optionValueMatCode;

    /**
     * 导入行号 用于记录添加行编号
     */
    @Transient
    private Integer entryNum;

    /**
     * 存指纹码
     */
    private String fingerprint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
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

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Integer getEntryNum() {
        return entryNum;
    }

    public void setEntryNum(Integer entryNum) {
        this.entryNum = entryNum;
    }
}
