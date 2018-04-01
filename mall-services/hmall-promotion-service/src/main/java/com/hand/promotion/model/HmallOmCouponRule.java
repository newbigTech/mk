/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.promotion.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "HMALL_OM_COUPONRULE")
public class HmallOmCouponRule {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_OM_COUPONRULE_S.nextval from dual")
    private Long couponruleId;

    /**
     * 关联订单
     */
    private Long orderId;

    /**
     * 规则Id
     */
    private String couponId;

    /**
     * 规则名称
     */
    private String couponName;

    /**
     * 版本Id
     */
    private String releaseId;

    /**
     * 优惠券生效开始时间
     */
    private Date startDate;

    /**
     * 优惠券生效结束时间
     */
    private Date endDate;

    /**
     * 促销金额
     */
    private Double benefit;

    /**
     * 优惠券编码
     */
    private String couponCode;

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
     * null
     */
    private String attribute1;

    /**
     * null
     */
    private String attribute2;

    /**
     * null
     */
    private String attribute3;

    /**
     * null
     */
    private String attribute4;

    /**
     * null
     */
    private String attribute5;

    /**
     * null
     */
    private String attribute6;

    /**
     * null
     */
    private String attribute7;

    /**
     * null
     */
    private String attribute8;

    /**
     * null
     */
    private String attribute9;

    /**
     * null
     */
    private String attribute10;

    /**
     * null
     */
    private String attribute11;

    /**
     * null
     */
    private String attribute12;

    /**
     * null
     */
    private String attribute13;

    /**
     * null
     */
    private String attribute14;

    /**
     * null
     */
    private String attribute15;

    /**
     * 主键
     * @return COUPONRULE_ID 主键
     */
    public Long getCouponruleId() {
        return couponruleId;
    }

    /**
     * 主键
     * @param couponruleId 主键
     */
    public void setCouponruleId(Long couponruleId) {
        this.couponruleId = couponruleId;
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
     * 规则Id
     * @return COUPON_ID 规则Id
     */
    public String getCouponId() {
        return couponId;
    }

    /**
     * 规则Id
     * @param couponId 规则Id
     */
    public void setCouponId(String couponId) {
        this.couponId = couponId == null ? null : couponId.trim();
    }

    /**
     * 规则名称
     * @return COUPON_NAME 规则名称
     */
    public String getCouponName() {
        return couponName;
    }

    /**
     * 规则名称
     * @param couponName 规则名称
     */
    public void setCouponName(String couponName) {
        this.couponName = couponName == null ? null : couponName.trim();
    }

    /**
     * 版本Id
     * @return RELEASE_ID 版本Id
     */
    public String getReleaseId() {
        return releaseId;
    }

    /**
     * 版本Id
     * @param releaseId 版本Id
     */
    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId == null ? null : releaseId.trim();
    }

    /**
     * 优惠券生效开始时间
     * @return START_DATE 优惠券生效开始时间
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 优惠券生效开始时间
     * @param startDate 优惠券生效开始时间
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 优惠券生效结束时间
     * @return END_DATE 优惠券生效结束时间
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 优惠券生效结束时间
     * @param endDate 优惠券生效结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 促销金额
     * @return BENEFIT 促销金额
     */
    public Double getBenefit() {
        return benefit;
    }

    /**
     * 促销金额
     * @param benefit 促销金额
     */
    public void setBenefit(Double benefit) {
        this.benefit = benefit;
    }

    /**
     * 优惠券编码
     * @return COUPON_CODE 优惠券编码
     */
    public String getCouponCode() {
        return couponCode;
    }
    /**
     * 优惠券编码
     * @return COUPON_CODE 优惠券编码
     */
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
    /**
     * 版本号
     * @return OBJECT_VERSION_NUMBER 版本号
     */
    public Double getObjectVersionNumber() {
        return objectVersionNumber;
    }

    /**
     * 版本号
     * @param objectVersionNumber 版本号
     */
    public void setObjectVersionNumber(Double objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    /**
     * null
     * @return CREATION_DATE null
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * null
     * @param creationDate null
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * null
     * @return CREATED_BY null
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * null
     * @param createdBy null
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * null
     * @return LAST_UPDATED_BY null
     */
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * null
     * @param lastUpdatedBy null
     */
    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * null
     * @return LAST_UPDATE_DATE null
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * null
     * @param lastUpdateDate null
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * null
     * @return LAST_UPDATE_LOGIN null
     */
    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    /**
     * null
     * @param lastUpdateLogin null
     */
    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    /**
     * null
     * @return PROGRAM_APPLICATION_ID null
     */
    public Long getProgramApplicationId() {
        return programApplicationId;
    }

    /**
     * null
     * @param programApplicationId null
     */
    public void setProgramApplicationId(Long programApplicationId) {
        this.programApplicationId = programApplicationId;
    }

    /**
     * null
     * @return PROGRAM_ID null
     */
    public Long getProgramId() {
        return programId;
    }

    /**
     * null
     * @param programId null
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    /**
     * null
     * @return PROGRAM_UPDATE_DATE null
     */
    public Date getProgramUpdateDate() {
        return programUpdateDate;
    }

    /**
     * null
     * @param programUpdateDate null
     */
    public void setProgramUpdateDate(Date programUpdateDate) {
        this.programUpdateDate = programUpdateDate;
    }

    /**
     * null
     * @return REQUEST_ID null
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * null
     * @param requestId null
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * null
     * @return ATTRIBUTE_CATEGORY null
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    /**
     * null
     * @param attributeCategory null
     */
    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory == null ? null : attributeCategory.trim();
    }

    /**
     * null
     * @return ATTRIBUTE1 null
     */
    public String getAttribute1() {
        return attribute1;
    }

    /**
     * null
     * @param attribute1 null
     */
    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1 == null ? null : attribute1.trim();
    }

    /**
     * null
     * @return ATTRIBUTE2 null
     */
    public String getAttribute2() {
        return attribute2;
    }

    /**
     * null
     * @param attribute2 null
     */
    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2 == null ? null : attribute2.trim();
    }

    /**
     * null
     * @return ATTRIBUTE3 null
     */
    public String getAttribute3() {
        return attribute3;
    }

    /**
     * null
     * @param attribute3 null
     */
    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3 == null ? null : attribute3.trim();
    }

    /**
     * null
     * @return ATTRIBUTE4 null
     */
    public String getAttribute4() {
        return attribute4;
    }

    /**
     * null
     * @param attribute4 null
     */
    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4 == null ? null : attribute4.trim();
    }

    /**
     * null
     * @return ATTRIBUTE5 null
     */
    public String getAttribute5() {
        return attribute5;
    }

    /**
     * null
     * @param attribute5 null
     */
    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5 == null ? null : attribute5.trim();
    }

    /**
     * null
     * @return ATTRIBUTE6 null
     */
    public String getAttribute6() {
        return attribute6;
    }

    /**
     * null
     * @param attribute6 null
     */
    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6 == null ? null : attribute6.trim();
    }

    /**
     * null
     * @return ATTRIBUTE7 null
     */
    public String getAttribute7() {
        return attribute7;
    }

    /**
     * null
     * @param attribute7 null
     */
    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7 == null ? null : attribute7.trim();
    }

    /**
     * null
     * @return ATTRIBUTE8 null
     */
    public String getAttribute8() {
        return attribute8;
    }

    /**
     * null
     * @param attribute8 null
     */
    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8 == null ? null : attribute8.trim();
    }

    /**
     * null
     * @return ATTRIBUTE9 null
     */
    public String getAttribute9() {
        return attribute9;
    }

    /**
     * null
     * @param attribute9 null
     */
    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9 == null ? null : attribute9.trim();
    }

    /**
     * null
     * @return ATTRIBUTE10 null
     */
    public String getAttribute10() {
        return attribute10;
    }

    /**
     * null
     * @param attribute10 null
     */
    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10 == null ? null : attribute10.trim();
    }

    /**
     * null
     * @return ATTRIBUTE11 null
     */
    public String getAttribute11() {
        return attribute11;
    }

    /**
     * null
     * @param attribute11 null
     */
    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11 == null ? null : attribute11.trim();
    }

    /**
     * null
     * @return ATTRIBUTE12 null
     */
    public String getAttribute12() {
        return attribute12;
    }

    /**
     * null
     * @param attribute12 null
     */
    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12 == null ? null : attribute12.trim();
    }

    /**
     * null
     * @return ATTRIBUTE13 null
     */
    public String getAttribute13() {
        return attribute13;
    }

    /**
     * null
     * @param attribute13 null
     */
    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13 == null ? null : attribute13.trim();
    }

    /**
     * null
     * @return ATTRIBUTE14 null
     */
    public String getAttribute14() {
        return attribute14;
    }

    /**
     * null
     * @param attribute14 null
     */
    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14 == null ? null : attribute14.trim();
    }

    /**
     * null
     * @return ATTRIBUTE15 null
     */
    public String getAttribute15() {
        return attribute15;
    }

    /**
     * null
     * @param attribute15 null
     */
    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15 == null ? null : attribute15.trim();
    }
}