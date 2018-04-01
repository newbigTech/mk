package com.hand.promotion.pojo.enums;

/**
 * 优惠券状态
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public enum CouponStatus {
	/**
	 * 可使用
	 */
	STATUS_01("可使用", "STATUS_01"),
	/**
	 * 已使用
	 */
	STATUS_02("已使用", "STATUS_02"),
	/**
	 * 失效
	 */
	STATUS_03("失效", "STATUS_03");

	private String name;
	private String value;

	CouponStatus() {
	}

	CouponStatus(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}


	public String getValue() {
		return value;
	}


	public static boolean contains(String type) {
		for (CouponStatus status : CouponStatus.values()) {
			if (status.value.equals(type)) {
				return true;
			}
		}
		return false;
	}
}
