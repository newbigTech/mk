package com.hand.promotion.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 梅新养
 * @name:HmallOmPaymentInfo
 * @Description:HMALL_OM_PAYMENTINFO 对应的实体类
 * @version 1.0
 * @date 2017/5/24 14:39
 */
@Entity
@Table(name = "HMALL_OM_PAYMENTINFO")
public class HmallOmPaymentInfo {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_PAYMENTINFO_S.nextval from dual")
    private Long paymentinfoId;

    /**
     * 支付渠道
     */
    private String payMode;

    /**
     * 支付金额
     */
    private Double payAmount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 流水号
     */
    private String numberCode;

    /**
     * 关联订单
     */
    private Long orderId;

    /**
     * 支付状态
     */
    private String status;

    /**
     * 支付账号
     */
    private String account;

    /**
     * 支付名称
     */
    private String name;

    /**
     * 接口传输标示
     */
    private String syncflag;

    /**
     * 版本号
     */
    private Double objectVersionNumber;

    /**
     * null
     */
    private Date creationDate;

    /**
     * null
     */
    private Long createdBy;

    /**
     * null
     */
    private Long lastUpdatedBy;

    /**
     * null
     */
    private Date lastUpdateDate;

    /**
     * null
     */
    private Long lastUpdateLogin;

    /**
     * null
     */
    private Long programApplicationId;

    /**
     * null
     */
    private Long programId;

    /**
     * null
     */
    private Date programUpdateDate;

    /**
     * null
     */
    private Long requestId;

    /**
     * null
     */
    private String attributeCategory;

    /**
     * 订单外部交易号
     */
    private String outTradeNo;

    /**
     * 所购coupon关联id
     */
    private String couponId;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 主键
     *
     * @return PAYMENTINFO_ID 主键
     */
    public Long getPaymentinfoId() {
        return paymentinfoId;
    }

    /**
     * 主键
     *
     * @param paymentinfoId 主键
     */
    public void setPaymentinfoId(Long paymentinfoId) {
        this.paymentinfoId = paymentinfoId;
    }

    /**
     * 支付渠道
     *
     * @return PAY_MODE 支付渠道
     */
    public String getPayMode() {
        return payMode;
    }

    /**
     * 支付渠道
     *
     * @param payMode 支付渠道
     */
    public void setPayMode(String payMode) {
        this.payMode = payMode == null ? null : payMode.trim();
    }

    /**
     * 支付金额
     *
     * @return PAY_AMOUNT 支付金额
     */
    public Double getPayAmount() {
        return payAmount;
    }

    /**
     * 支付金额
     *
     * @param payAmount 支付金额
     */
    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * 支付时间
     *
     * @return PAY_TIME 支付时间
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 支付时间
     *
     * @param payTime 支付时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * 流水号
     *
     * @return NUMBER_CODE 流水号
     */
    public String getNumberCode() {
        return numberCode;
    }

    /**
     * 流水号
     *
     * @param numberCode 流水号
     */
    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode == null ? null : numberCode.trim();
    }

    /**
     * 关联订单
     *
     * @return ORDER_ID 关联订单
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 关联订单
     *
     * @param orderId 关联订单
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 支付状态
     *
     * @return STATUS 支付状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 支付状态
     *
     * @param status 支付状态
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 支付账号
     *
     * @return ACCOUNT 支付账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 支付账号
     *
     * @param account 支付账号
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * 支付名称
     *
     * @return NAME 支付名称
     */
    public String getName() {
        return name;
    }

    /**
     * 支付名称
     *
     * @param name 支付名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 接口传输标示
     *
     * @return SYNCFLAG 接口传输标示
     */
    public String getSyncflag() {
        return syncflag;
    }

    /**
     * 接口传输标示
     *
     * @param syncflag 接口传输标示
     */
    public void setSyncflag(String syncflag) {
        this.syncflag = syncflag == null ? null : syncflag.trim();
    }

    /**
     * 版本号
     *
     * @return OBJECT_VERSION_NUMBER 版本号
     */
    public Double getObjectVersionNumber() {
        return objectVersionNumber;
    }

    /**
     * 版本号
     *
     * @param objectVersionNumber 版本号
     */
    public void setObjectVersionNumber(Double objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    /**
     * null
     *
     * @return CREATION_DATE null
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * null
     *
     * @param creationDate null
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * null
     *
     * @return CREATED_BY null
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * null
     *
     * @param createdBy null
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * null
     *
     * @return LAST_UPDATED_BY null
     */
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * null
     *
     * @param lastUpdatedBy null
     */
    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * null
     *
     * @return LAST_UPDATE_DATE null
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * null
     *
     * @param lastUpdateDate null
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * null
     *
     * @return LAST_UPDATE_LOGIN null
     */
    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    /**
     * null
     *
     * @param lastUpdateLogin null
     */
    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    /**
     * null
     *
     * @return PROGRAM_APPLICATION_ID null
     */
    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    /**
     * null
     *
     * @param programApplicationId null
     */
    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    /**
     * null
     *
     * @return PROGRAM_ID null
     */
    public Long getProgramId() {
        return programId;
    }

    /**
     * null
     *
     * @param programId null
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    /**
     * null
     *
     * @return PROGRAM_UPDATE_DATE null
     */
    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    /**
     * null
     *
     * @param programUpdateDate null
     */
    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    /**
     * null
     *
     * @return REQUEST_ID null
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * null
     *
     * @param requestId null
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * null
     *
     * @return ATTRIBUTE_CATEGORY null
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    /**
     * null
     *
     * @param attributeCategory null
     */
    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory == null ? null : attributeCategory.trim();
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}