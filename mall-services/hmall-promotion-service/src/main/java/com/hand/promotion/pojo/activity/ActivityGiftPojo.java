package com.hand.promotion.pojo.activity;


/**
 * 促销赠品
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ActivityGiftPojo implements java.io.Serializable{
	private String productCode;
	private Integer countNumber;
	private Integer totalNumber;
	private String productName;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getCountNumber() {
		return countNumber;
	}

	public void setCountNumber(Integer countNumber) {
		this.countNumber = countNumber;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "ActivityGiftPojo{" +
				"productCode='" + productCode + '\'' +
				", countNumber=" + countNumber +
				", totalNumber=" + totalNumber +
				", productName='" + productName + '\'' +
				'}';
	}
}
