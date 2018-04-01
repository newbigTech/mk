package com.hand.promotion.pojo.enums;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 * 优惠券使用渠道
 */
public enum CouponRange {
	/**
	 * 店铺通用
	 */
	BOTH("店铺通用", "BOTH"),
	/**
	 * 快递配送
	 */
	EXPRESS("快递配送", "EXPRESS"),
	/**
	 * 门店自提
	 */
	PICKUP("门店自提", "PICKUP");

	private String name;
	private String value;

	CouponRange(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}



	public String getName() {
		return name;
	}

}
