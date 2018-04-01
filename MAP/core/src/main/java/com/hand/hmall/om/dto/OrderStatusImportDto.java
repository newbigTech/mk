package com.hand.hmall.om.dto;

import com.hand.common.util.ExcelVOAttribute;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name OrderStatusImportDto
 * @description 天猫订单状态导入DTO
 * @date 2017/9/25
 */
public class OrderStatusImportDto {
    public static final String DEFAULT_SHEET_NAME = "天猫订单状态";
    public static final String DEFAULT_EXCEL_FILE_NAME = DEFAULT_SHEET_NAME + ".xlsx";
    @ExcelVOAttribute(name = "平台订单号", column = "A", isExport = true)
    private String escOrderCode;
    @ExcelVOAttribute(name = "成交时间", column = "B", isExport = true)
    private String tradeFinishTime;
    @ExcelVOAttribute(name = "订单状态", column = "C", isExport = true)
    private String orderStatus;

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getTradeFinishTime() {
        return tradeFinishTime;
    }

    public void setTradeFinishTime(String tradeFinishTime) {
        this.tradeFinishTime = tradeFinishTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
