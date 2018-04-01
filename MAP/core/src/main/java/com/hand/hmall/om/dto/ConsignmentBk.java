package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name Consignment
 * @description 发货单备份dto
 * @date 2017年8月4日13:54:47
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_CONSIGNMENT_BK")
public class ConsignmentBk extends BaseDTO {
    /**
     * 主键
     */
    @Id
    @GeneratedValue
    private Long consignmentId;

    /**
     * 发货单号
     */
    private String code;

    /**
     * SAP订单号
     */
    private String sapCode;

    /**
     * 发货单状态
     */
    private String status;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 关联订单
     */
    private Long orderId;

    /**
     * 快递公司
     */
    private Long logisticsCompanies;

    /**
     * 快递单号
     */
    private String logisticsNumber;

    /**
     * 发货仓库/门店
     */
    private Long pointOfServiceId;

    /**
     * 配送方式
     */
    private String shippingType;

    /**
     * 发货时间
     */
    private Date shippingDate;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人国家
     */
    private String receiverCountry;

    /**
     * 收货人省
     */
    private String receiverState;

    /**
     * 收货人市
     */
    private String receiverCity;

    /**
     * 收货人区
     */
    private String receiverDistrict;

    /**
     * 收货人街道
     */
    private String receiverTown;

    /**
     * 收货人地址
     */
    private String receiverAddress;

    /**
     * 收货人邮编
     */
    private String receiverZip;

    /**
     * 收货人手机号
     */
    private String receiverMobile;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 发货单备注
     */
    private String note;

    /**
     * 预计交货时间
     */
    private Date estimateDeliveryTime;

    /**
     * 是否允许分批发运
     */
    private String splitAllowed;

    /**
     * 接口同步标记
     */
    private String syncflag;


    /**
     * 员工
     */
    private Long approvedBy;

    /**
     * 审核时间
     */
    private Date approvedDate;

    /**
     * 审核次数
     */
    private Integer approvedTimes;

    /**
     * 拆单原因
     */
    private String splitReason;

    /**
     * 异常原因
     */
    private String abnormalReason;

    /**
     * 客服已审核
     */
    @Column
    private String csApproved;

    /**
     * 发货单状态名称
     */
    @Transient
    private String consignmentStatus;

    /**
     * 订单创建时间
     */
    @Transient
    private Date orderCreationtime;

    /**
     * 订单类型
     */
    @Transient
    private String orderType;

    /**
     * 发运方式名称
     */
    @Transient
    private String shippingName;

    /**
     * 服务点
     */
    @Transient
    private String pointCode;

    private String pause;

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }

    public String getPointCode() {
        return pointCode;
    }

    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Date getOrderCreationtime() {
        return orderCreationtime;
    }

    public void setOrderCreationtime(Date orderCreationtime) {
        this.orderCreationtime = orderCreationtime;
    }

    public String getConsignmentStatus() {
        return consignmentStatus;
    }

    public void setConsignmentStatus(String consignmentStatus) {
        this.consignmentStatus = consignmentStatus;
    }

    /**
     * 主键
     * @return CONSIGNMENT_ID 主键
     */
    public Long getConsignmentId() {
        return consignmentId;
    }

    /**
     * 主键
     * @param consignmentId 主键
     */
    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }

    /**
     * 发货单号
     * @return CODE 发货单号
     */
    public String getCode() {
        return code;
    }

    /**
     * 发货单号
     * @param code 发货单号
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * SAP订单号
     * @return SAP_CODE SAP订单号
     */
    public String getSapCode() {
        return sapCode;
    }

    /**
     * SAP订单号
     * @param sapCode SAP订单号
     */
    public void setSapCode(String sapCode) {
        this.sapCode = sapCode == null ? null : sapCode.trim();
    }

    /**
     * 发货单状态
     * @return STATUS 发货单状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 发货单状态
     * @param status 发货单状态
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 品牌
     * @return BRAND 品牌
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 品牌
     * @param brand 品牌
     */
    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    /**
     * 关联订单
     * @return ORDER_ID 关联订单
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 关联订单
     * @param orderId 关联订单
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 快递公司
     * @return LOGISTICS_COMPANIES 快递公司
     */
    public Long getLogisticsCompanies() {
        return logisticsCompanies;
    }

    /**
     * 快递公司
     * @param logisticsCompanies 快递公司
     */
    public void setLogisticsCompanies(Long logisticsCompanies) {
        this.logisticsCompanies = logisticsCompanies;
    }

    /**
     * 快递单号
     * @return LOGISTICS_NUMBER 快递单号
     */
    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    /**
     * 快递单号
     * @param logisticsNumber 快递单号
     */
    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber == null ? null : logisticsNumber.trim();
    }

    /**
     * 发货仓库/门店
     * @return POINT_OF_SERVICE_ID 发货仓库/门店
     */
    public Long getPointOfServiceId() {
        return pointOfServiceId;
    }

    /**
     * 发货仓库/门店
     * @param pointOfServiceId 发货仓库/门店
     */
    public void setPointOfServiceId(Long pointOfServiceId) {
        this.pointOfServiceId = pointOfServiceId;
    }

    /**
     * 配送方式
     * @return SHIPPING_TYPE 配送方式
     */
    public String getShippingType() {
        return shippingType;
    }

    /**
     * 配送方式
     * @param shippingType 配送方式
     */
    public void setShippingType(String shippingType) {
        this.shippingType = shippingType == null ? null : shippingType.trim();
    }

    /**
     * 发货时间
     * @return SHIPPING_DATE 发货时间
     */
    public Date getShippingDate() {
        return shippingDate;
    }

    /**
     * 发货时间
     * @param shippingDate 发货时间
     */
    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    /**
     * 收货人姓名
     * @return RECEIVER_NAME 收货人姓名
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 收货人姓名
     * @param receiverName 收货人姓名
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    /**
     * 收货人国家
     * @return RECEIVER_COUNTRY 收货人国家
     */
    public String getReceiverCountry() {
        return receiverCountry;
    }

    /**
     * 收货人国家
     * @param receiverCountry 收货人国家
     */
    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry == null ? null : receiverCountry.trim();
    }

    /**
     * 收货人省
     * @return RECEIVER_STATE 收货人省
     */
    public String getReceiverState() {
        return receiverState;
    }

    /**
     * 收货人省
     * @param receiverState 收货人省
     */
    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    /**
     * 收货人市
     * @return RECEIVER_CITY 收货人市
     */
    public String getReceiverCity() {
        return receiverCity;
    }

    /**
     * 收货人市
     * @param receiverCity 收货人市
     */
    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    /**
     * 收货人区
     * @return RECEIVER_DISTRICT 收货人区
     */
    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    /**
     * 收货人区
     * @param receiverDistrict 收货人区
     */
    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    /**
     * 收货人街道
     * @return RECEIVER_TOWN 收货人街道
     */
    public String getReceiverTown() {
        return receiverTown;
    }

    /**
     * 收货人街道
     * @param receiverTown 收货人街道
     */
    public void setReceiverTown(String receiverTown) {
        this.receiverTown = receiverTown == null ? null : receiverTown.trim();
    }

    /**
     * 收货人地址
     * @return RECEIVER_ADDRESS 收货人地址
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     * 收货人地址
     * @param receiverAddress 收货人地址
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
    }

    /**
     * 收货人邮编
     * @return RECEIVER_ZIP 收货人邮编
     */
    public String getReceiverZip() {
        return receiverZip;
    }

    /**
     * 收货人邮编
     * @param receiverZip 收货人邮编
     */
    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip == null ? null : receiverZip.trim();
    }

    /**
     * 收货人手机号
     * @return RECEIVER_MOBILE 收货人手机号
     */
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     * 收货人手机号
     * @param receiverMobile 收货人手机号
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile == null ? null : receiverMobile.trim();
    }

    /**
     * 收货人电话
     * @return RECEIVER_PHONE 收货人电话
     */
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * 收货人电话
     * @param receiverPhone 收货人电话
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    /**
     * 发货单备注
     * @return NOTE 发货单备注
     */
    public String getNote() {
        return note;
    }

    /**
     * 发货单备注
     * @param note 发货单备注
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * 预计交货时间
     * @return ESTIMATE_DELIVERY_TIME 预计交货时间
     */
    public Date getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    /**
     * 预计交货时间
     * @param estimateDeliveryTime 预计交货时间
     */
    public void setEstimateDeliveryTime(Date estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    /**
     * 是否允许分批发运
     * @return SPLIT_ALLOWED 是否允许分批发运
     */
    public String getSplitAllowed() {
        return splitAllowed;
    }

    /**
     * 是否允许分批发运
     * @param splitAllowed 是否允许分批发运
     */
    public void setSplitAllowed(String splitAllowed) {
        this.splitAllowed = splitAllowed == null ? null : splitAllowed.trim();
    }

    /**
     * 接口同步标记
     * @return SYNCFLAG 接口同步标记
     */
    public String getSyncflag() {
        return syncflag;
    }

    /**
     * 接口同步标记
     * @param syncflag 接口同步标记
     */
    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag == null ? null : syncflag.trim();
    }


    /**
     * 员工
     * @return APPROVED_BY 员工
     */
    public Long getApprovedBy() {
        return approvedBy;
    }

    /**
     * 员工
     * @param approvedBy 员工
     */
    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    /**
     * 审核时间
     * @return APPROVED_DATE 审核时间
     */
    public Date getApprovedDate() {
        return approvedDate;
    }

    /**
     * 审核时间
     * @param approvedDate 审核时间
     */
    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    /**
     * 审核次数
     * @return APPROVED_TIMES 审核次数
     */
    public Integer getApprovedTimes() {
        return approvedTimes;
    }

    /**
     * 审核次数
     * @param approvedTimes 审核次数
     */
    public void setApprovedTimes(Integer approvedTimes) {
        this.approvedTimes = approvedTimes;
    }

    /**
     * 拆单原因
     * @return SPLIT_REASON 拆单原因
     */
    public String getSplitReason() {
        return splitReason;
    }

    /**
     * 拆单原因
     * @param splitReason 拆单原因
     */
    public void setSplitReason(String splitReason) {
        this.splitReason = splitReason == null ? null : splitReason.trim();
    }

    /**
     * 异常原因
     * @return ABNORMAL_REASON 异常原因
     */
    public String getAbnormalReason() {
        return abnormalReason;
    }

    /**
     * 异常原因
     * @param abnormalReason 异常原因
     */
    public void setAbnormalReason(String abnormalReason) {
        this.abnormalReason = abnormalReason == null ? null : abnormalReason.trim();
    }

    /**
     * 客服已审核
     * @return CS_APPROVED 客服已审核
     */
    public String getCsApproved() {
        return csApproved;
    }

    /**
     * 客服已审核
     * @param csApproved 客服已审核
     */
    public void setCsApproved(String csApproved) {
        this.csApproved = csApproved == null ? null : csApproved.trim();
    }

}