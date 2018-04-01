package com.hand.promotion.pojo.purchase;


/**
 * 用户限购记录
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class UserPurchasePojo implements java.io.Serializable {
	private Long lineId;
	private String userId;
	private Long purchaseId;
	private Integer userPurchaseCount;

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Integer getUserPurchaseCount() {
		return userPurchaseCount;
	}

	public void setUserPurchaseCount(Integer userPurchaseCount) {
		this.userPurchaseCount = userPurchaseCount;
	}
}
