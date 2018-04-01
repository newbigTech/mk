package com.hand.hmall.logistics.trans;

import java.io.Serializable;

/**
 * @author chenzhigang
 * @version 0.1
 * @name WmsLogisticsInfo
 * @description 索勤物流信息实体
 * @date 2017/12/12
 */
public class WmsLogisticsInfo implements Serializable {

    // 	索勤产生的唯一序列号
    private String serialNum;
    // 	DN 单号
    private String dnOrderId;
    // 	DN 行号
    private String dnLineId;
    // 	销售订单号
    private String soOrderId;
    // 	销售订单行号
    private String soLineId;
    // 	产品号
    private String productCode;
    // 	发货数量
    private Integer sentAmount;
    // 	行有效标识
    private boolean vaild;
    // 	发货日期
    private String sentDate;
    // 	物流公司代码
    private String transCode;
    // 	物流单号
    private String transOrderCode;

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getDnOrderId() {
        return dnOrderId;
    }

    public void setDnOrderId(String dnOrderId) {
        this.dnOrderId = dnOrderId;
    }

    public String getDnLineId() {
        return dnLineId;
    }

    public void setDnLineId(String dnLineId) {
        this.dnLineId = dnLineId;
    }

    public String getSoOrderId() {
        return soOrderId;
    }

    public void setSoOrderId(String soOrderId) {
        this.soOrderId = soOrderId;
    }

    public String getSoLineId() {
        return soLineId;
    }

    public void setSoLineId(String soLineId) {
        this.soLineId = soLineId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getSentAmount() {
        return sentAmount;
    }

    public void setSentAmount(Integer sentAmount) {
        this.sentAmount = sentAmount;
    }

    public boolean isVaild() {
        return vaild;
    }

    public void setVaild(boolean vaild) {
        this.vaild = vaild;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getTransOrderCode() {
        return transOrderCode;
    }

    public void setTransOrderCode(String transOrderCode) {
        this.transOrderCode = transOrderCode;
    }
}
