package com.hand.hmall.as.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name SvsalesTemplate
 * @description 服务销售单导入模板
 * @date 2017/7/17
 */
@ExtensionAttribute(disable = true)
public class SvsalesTemplate {
    public static final String DEFAULT_SHEET_NAME = "线下服务销售单导入模板";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";

    @ExcelVOAttribute(name = "服务销售单序号(必填)", column = "A", isExport = true)
    private String svsalesNum;
    @ExcelVOAttribute(name = "TM订单编号(必填)", column = "B", isExport = true)
    private String tmOrderCode;
    @ExcelVOAttribute(name = "服务单单号(必填)", column = "C", isExport = true)
    private String serviceOrderCode;
    @ExcelVOAttribute(name = "备注", column = "D", isExport = true)
    private String note;
    @ExcelVOAttribute(name = "实付金额合计(必填)", column = "E", isExport = true)
    private Double realPaySum;
    @ExcelVOAttribute(name = "受理客服(必填)", column = "F", isExport = true)
    private String cs;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelVOAttribute(name = "完结时间（必填）", column = "G", isExport = true)
    private Date finishTime;
    @ExcelVOAttribute(name = "收费项目(必填）", column = "H", isExport = true)
    private String payProject;
    @ExcelVOAttribute(name = "实际价格(必填)", column = "I", isExport = true)
    private Double amtPrice;
    @ExcelVOAttribute(name = "行备注", column = "J", isExport = true)
    private String lineNote;

    public String getTmOrderCode() {
        return tmOrderCode;
    }

    public void setTmOrderCode(String tmOrderCode) {
        this.tmOrderCode = tmOrderCode;
    }

    public String getServiceOrderCode() {
        return serviceOrderCode;
    }

    public void setServiceOrderCode(String serviceOrderCode) {
        this.serviceOrderCode = serviceOrderCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getRealPaySum() {
        return realPaySum;
    }

    public void setRealPaySum(Double realPaySum) {
        this.realPaySum = realPaySum;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getPayProject() {
        return payProject;
    }

    public void setPayProject(String payProject) {
        this.payProject = payProject;
    }

    public Double getAmtPrice() {
        return amtPrice;
    }

    public void setAmtPrice(Double amtPrice) {
        this.amtPrice = amtPrice;
    }

    public String getLineNote() {
        return lineNote;
    }

    public void setLineNote(String lineNote) {
        this.lineNote = lineNote;
    }

    public String getSvsalesNum() {
        return svsalesNum;
    }

    public void setSvsalesNum(String svsalesNum) {
        this.svsalesNum = svsalesNum;
    }
}
