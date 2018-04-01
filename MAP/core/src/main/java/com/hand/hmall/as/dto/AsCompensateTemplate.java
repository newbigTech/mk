package com.hand.hmall.as.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name AsCompensateTemplate
 * @description 销售赔付单导入模板DTO
 * @date 2017/10/11
 */
@ExtensionAttribute(disable = true)
public class AsCompensateTemplate {
    public static final String DEFAULT_SHEET_NAME = "销售赔付单导入模板";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";
    @ExcelVOAttribute(name = "销售赔付单序号(必填)", column = "A", isExport = true)
    private String compensateNum;
    @ExcelVOAttribute(name = "订单编号(必填)", column = "B", isExport = true)
    private String orderCode;
    @ExcelVOAttribute(name = "服务单单号(必填)", column = "C", isExport = true)
    private String serviceOrderCode;
    @ExcelVOAttribute(name = "备注", column = "D", isExport = true)
    private String note;
    @ExcelVOAttribute(name = "受理客服(必填)", column = "E", isExport = true)
    private String cs;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelVOAttribute(name = "完结时间（必填）", column = "F", isExport = true)
    private Date finishTime;
    @ExcelVOAttribute(name = "赔付金额（必填）", column = "G", isExport = true)
    private Double payFee;
    @ExcelVOAttribute(name = "行备注", column = "H", isExport = true)
    private String lineNote;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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

    public Double getPayFee() {
        return payFee;
    }

    public void setPayFee(Double payFee) {
        this.payFee = payFee;
    }

    public String getLineNote() {
        return lineNote;
    }

    public void setLineNote(String lineNote) {
        this.lineNote = lineNote;
    }

    public String getCompensateNum() {
        return compensateNum;
    }

    public void setCompensateNum(String compensateNum) {
        this.compensateNum = compensateNum;
    }
}
