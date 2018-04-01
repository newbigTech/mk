package com.hand.hmall.as.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author zhangmeng
 * @version 0.1
 * @name SwapOrder
 * @description 换货单
 * @date 2017/7/21
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_AS_RECEIPT")
public class SwapOrder extends BaseDTO {

    //	主键
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    private Long receiptOrderId;
    //	单号
    @Column
    private String code;
    //	单据类型（区分各种售后单据）
    @Column
    private String receiptType;
    //	单据状态
    @Column
    private String status;
    //	服务类别1
    @Column
    private String svCategory1;
    //	服务类别2
    @Column
    private String svCategory2;
    //	订单号
    @Column
    private String salesCode;
    //	原订单配送方式
    @Column
    private String shippingType;
    //	投诉内容
    @Column
    private String complaint;
    //	备注
    @Column
    private String note;
    //	用户
    @Column
    private String userId;
    //	手机号
    @Column
    private String mobile;
    //	客户地址
    @Column
    private String address;
    //	受理客服
    @Column
    private String cs;
    //	完结时间
    @Column
    private Date finishTime;
    //	同步retail标记
    @Column
    private String syncflag;
    //	客服预约执行日期
    @Column
    private Date appointmentDate;
    //	实际执行日期
    @Column
    private Date executionDate;
    //	服务技师姓名
    @Column
    private String technicianName;
    //	是否收费
    @Column
    private String isCharge;
    //	服务单单号
    @Column
    private String linksCode;
    //	支付状态
    @Column
    private String payStatus;
    //	运费
    @Column
    private Double transFee;
    //	再存储费
    @Column
    private Double restoreFee;
    //	维修费
    @Column
    private Double repairFee;
    //	应付金额合计
    @Column
    private Double shouldPay;
    //	实付金额合计
    @Column
    private Double realPay;
    //	用户名称
    @Column
    private String name;
    //物流单号
    @Column
    private String logisticsNumber;
    //实际返回客户金额
    @Column
    private Long returnFee;
    //换货原因
    @Column
    private String changeReason;
    //换货OA单号
    @Column
    private String oaChangeCode;
    //原订单商品总价格
    @Column
    private Long reorderFee;
    //换货后总价格
    @Column
    private Long afterChangeFee;
    //退货行
    @Transient
    private List<ServiceorderEntry> returnOrderEntries;

    //换货行
    @Transient
    private List<ServiceorderEntry> changeOrderEntries;

    public Long getReorderFee() {
        return reorderFee;
    }

    public void setReorderFee(Long reorderFee) {
        this.reorderFee = reorderFee;
    }

    public Long getAfterChangeFee() {
        return afterChangeFee;
    }

    public void setAfterChangeFee(Long afterChangeFee) {
        this.afterChangeFee = afterChangeFee;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getOaChangeCode() {
        return oaChangeCode;
    }

    public void setOaChangeCode(String oaChangeCode) {
        this.oaChangeCode = oaChangeCode;
    }

    public List<ServiceorderEntry> getChangeOrderEntries() {
        return changeOrderEntries;
    }

    public void setChangeOrderEntries(List<ServiceorderEntry> changeOrderEntries) {
        this.changeOrderEntries = changeOrderEntries;
    }

    public List<ServiceorderEntry> getReturnOrderEntries() {
        return returnOrderEntries;
    }

    public void setReturnOrderEntries(List<ServiceorderEntry> returnOrderEntries) {
        this.returnOrderEntries = returnOrderEntries;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public Long getReturnFee() {
        return returnFee;
    }

    public void setReturnFee(Long returnFee) {
        this.returnFee = returnFee;
    }

    public Long getReceiptOrderId() {
        return receiptOrderId;
    }

    public void setReceiptOrderId(Long receiptOrderId) {
        this.receiptOrderId = receiptOrderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSvCategory1() {
        return svCategory1;
    }

    public void setSvCategory1(String svCategory1) {
        this.svCategory1 = svCategory1;
    }

    public String getSvCategory2() {
        return svCategory2;
    }

    public void setSvCategory2(String svCategory2) {
        this.svCategory2 = svCategory2;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getSyncflag() {
        return syncflag;
    }

    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getIsCharge() {
        return isCharge;
    }

    public void setIsCharge(String isCharge) {
        this.isCharge = isCharge;
    }

    public String getLinksCode() {
        return linksCode;
    }

    public void setLinksCode(String linksCode) {
        this.linksCode = linksCode;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Double getTransFee() {
        return transFee;
    }

    public void setTransFee(Double transFee) {
        this.transFee = transFee;
    }

    public Double getRestoreFee() {
        return restoreFee;
    }

    public void setRestoreFee(Double restoreFee) {
        this.restoreFee = restoreFee;
    }

    public Double getRepairFee() {
        return repairFee;
    }

    public void setRepairFee(Double repairFee) {
        this.repairFee = repairFee;
    }

    public Double getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(Double shouldPay) {
        this.shouldPay = shouldPay;
    }

    public Double getRealPay() {
        return realPay;
    }

    public void setRealPay(Double realPay) {
        this.realPay = realPay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
