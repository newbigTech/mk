package com.hand.promotion.pojo.coupon;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class CouponsSimplePojo implements java.io.Serializable{
	private Long id;
	private String countNumber;
	private String couponName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountNumber() {
		return countNumber;
	}

	public void setCountNumber(String countNumber) {
		this.countNumber = countNumber;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

}
