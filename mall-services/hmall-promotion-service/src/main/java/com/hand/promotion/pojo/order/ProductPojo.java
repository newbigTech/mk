package com.hand.promotion.pojo.order;

import java.util.List;

/**
 * 产品信息
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ProductPojo implements java.io.Serializable {
	/**
	 * skuCode
	 */
	private String productId;
	/**
	 * spuCode
	 */
	private String productCode;
	private String approval;
	/**
	 * 促销ID
	 */
	private String activityId;
	/**
	 * 是否赠品
	 */
	private String isGift = "N";
	/**
	 * 排序？
	 */
	private Integer sort;
	private Integer lineNumber;

	private List<String> cateList;
	private Integer quantity;
	private FeesPojo fees;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getApproval() {
		return approval;
	}

	public void setApproval(String approval) {
		this.approval = approval;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getIsGift() {
		return isGift;
	}

	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public List<String> getCateList() {
		return cateList;
	}

	public void setCateList(List<String> cateList) {
		this.cateList = cateList;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public FeesPojo getFees() {
		return fees;
	}

	public void setFees(FeesPojo fees) {
		this.fees = fees;
	}
}
