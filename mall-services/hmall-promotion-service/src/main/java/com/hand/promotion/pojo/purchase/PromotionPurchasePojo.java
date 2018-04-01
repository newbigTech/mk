package com.hand.promotion.pojo.purchase;

import java.util.List;

/**
 * 限购
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class PromotionPurchasePojo implements java.io.Serializable {
	/**
	 * 限购规则ID
	 */
	private Long purchaseId;
	/**
	 * 限购规则名称
	 */
	private String purchaseName;
	/**
	 * 商品范围（sku）
	 */
	private List<PurchaseProductRangePojo> productRange;
	/**
	 * 限购数量
	 */
	private Integer purchaseNumber;
	/**
	 * 限购开始时间
	 */
	private Long startDate;
	/**
	 * 限购结束时间
	 */
	private Long endDate;
	/**
	 * 限购状态
	 */
	private String purchaseStatus;

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getPurchaseName() {
		return purchaseName;
	}

	public void setPurchaseName(String purchaseName) {
		this.purchaseName = purchaseName;
	}

	public List<PurchaseProductRangePojo> getProductRange() {
		return productRange;
	}

	public void setProductRange(List<PurchaseProductRangePojo> productRange) {
		this.productRange = productRange;
	}

	public Integer getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(Integer purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getPurchaseStatus() {
		return purchaseStatus;
	}

	public void setPurchaseStatus(String purchaseStatus) {
		this.purchaseStatus = purchaseStatus;
	}
}
