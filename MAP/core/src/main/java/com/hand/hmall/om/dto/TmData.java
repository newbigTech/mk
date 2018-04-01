package com.hand.hmall.om.dto;


import com.hand.common.util.ExcelVOAttribute;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

/**
 * @author liuhongxi
 * @version 0.1
 * @name 天猫发货单导出用excel模板dto
 * @description 用于导出excel用
 * @date 2017/5/24
 */
@ExtensionAttribute(disable = true)
public class TmData {

    // 订单编号
    private Long orderId;

    // 订单编号
    @ExcelVOAttribute(name = "订单编号", column = "A", isExport = true)
    private String escOrderCode;

    // 单号
    @ExcelVOAttribute(name = "单号", column = "B", isExport = true)
    private String logisticsNumber;

    // 快递
    @ExcelVOAttribute(name = "快递", column = "C", isExport = true)
    private String logisticsCompanies;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getEscOrderCode() {
        return escOrderCode;
    }

    public void setEscOrderCode(String escOrderCode) {
        this.escOrderCode = escOrderCode;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getLogisticsCompanies() {
        return logisticsCompanies;
    }

    public void setLogisticsCompanies(String logisticsCompanies) {
        this.logisticsCompanies = logisticsCompanies;
    }

}