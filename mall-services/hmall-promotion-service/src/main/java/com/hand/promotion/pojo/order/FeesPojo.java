package com.hand.promotion.pojo.order;

/**
 * 费用信息
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class FeesPojo implements java.io.Serializable {
	/**
	 * 活动后单价
	 */
	private Double unitPrice;
	/**
	 * 优惠的价格
	 */
	private Double discountFee = 0d;
	/**
	 * 活动后总价
	 */
	private Double totalFee;
	/**
	 * 单件减免价格
	 */
	private Double discountUnitFee;
	private Double adjustFee = 0d;

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(Double discountFee) {
		this.discountFee = discountFee;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Double getDiscountUnitFee() {
		return discountUnitFee;
	}

	public void setDiscountUnitFee(Double discountUnitFee) {
		this.discountUnitFee = discountUnitFee;
	}

	public Double getAdjustFee() {
		return adjustFee;
	}

	public void setAdjustFee(Double adjustFee) {
		this.adjustFee = adjustFee;
	}

}
