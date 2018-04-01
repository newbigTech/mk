package com.hand.hmall.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @version 1.0
 * @name RefundOrderEntry
 * @Describe 退款单行
 * @Author chenzhigang
 * @Date 2017/7/23
 */
@Table(name = "HMALL_AS_REFUND_ENTRY")
public class RefundOrderEntry {

    // 主键
    @Id
    @GeneratedValue
    @Column
    private Long asRefundEntryId;

    // 退款单ID
    @Column
    private Long asRefundId;

    // 行序号
    @Column
    private String lineNumber;

    // 退款渠道
    @Column
    private String payMode;

    // 退款账号
    @Column
    private String account;

    // 退款金额
    @Column
    private Double payAmount;

    // 退款名称
    @Column
    private String name;

    // 版本号
    @Column
    private Long objectVersionNumber;

    @Column
    private Date creationDate;

    @Column
    private Long createdBy;

    @Column
    private Long lastUpdatedBy;

    @Column
    private Date lastUpdateDate;

    @Column
    private Long lastUpdateLogin;

    @Column
    private Long programApplicationId;

    @Column
    private Long programId;

    @Column
    private Date programUpdateDate;

    @Column
    private Long requestId;

    @Column
    private String attributeCategory;

    // 退款状态
    @Column
    private String payStatus;

    // 退款单行号
    @Column
    private String code;

    // 可退款金额
    @Column
    private Double couldAmount;

    public Long getAsRefundEntryId() {
        return asRefundEntryId;
    }

    public void setAsRefundEntryId(Long asRefundEntryId) {
        this.asRefundEntryId = asRefundEntryId;
    }

    public Long getAsRefundId() {
        return asRefundId;
    }

    public void setAsRefundId(Long asRefundId) {
        this.asRefundId = asRefundId;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getCouldAmount() {
        return couldAmount;
    }

    public void setCouldAmount(Double couldAmount) {
        this.couldAmount = couldAmount;
    }
}
