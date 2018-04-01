package com.hand.promotion.pojo.enums;

/**
 * 操作节点
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public enum EndPoint {
	/**
	 * 购物车操作
	 */
	OPTION_CART("购物车操作", "OPTION_CART"),
	/**
	 * 结算切换优惠券
	 */
	OPTION_COUPON("结算切换优惠券", "OPTION_COUPON"),
	/**
	 * 结算跳支付
	 */
	USE_COUPON("结算跳支付", "USE_COUPON"),
	/**
	 * 购物车跳结算
	 */
	PRECOMPUTED("购物车跳结算", "PRECOMPUTED");

	private String name;
	private String value;

	EndPoint(String name, String value) {
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
