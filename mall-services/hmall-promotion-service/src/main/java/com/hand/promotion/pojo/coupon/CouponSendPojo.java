package com.hand.promotion.pojo.coupon;


/**
 * 优惠券-用户关联表
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class CouponSendPojo implements java.io.Serializable {
	private Long userCouponId;
	private String userId;
	private Long couponId;
	private Long acquireDate;
	private Long endDate;
	private String usedFlag;
	private String sourceType;
	private Long sourceId;
	private String activeStatus;
	private String lockStatus;

	public Long getUserCouponId() {
		return userCouponId;
	}

	public void setUserCouponId(Long userCouponId) {
		this.userCouponId = userCouponId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getAcquireDate() {
		return acquireDate;
	}

	public void setAcquireDate(Long acquireDate) {
		this.acquireDate = acquireDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getUsedFlag() {
		return usedFlag;
	}

	public void setUsedFlag(String usedFlag) {
		this.usedFlag = usedFlag;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
}

