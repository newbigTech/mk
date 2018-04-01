package com.hand.promotion.pojo.enums;

/**
 * 订单渠道
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public enum SaleChannel {
	/**
	 * PC端
	 */
	PC("PC端", "PC"),
	/**
	 * 移动
	 */
	H5("移动", "H5"),
	/**
	 * 微信
	 */
	WECHAT("微信", "WECHAT"),
	/**
	 * APP
	 */
	APP("APP", "APP");
	private String name;
	private String value;

	public String getName() {
		return name;
	}


	public String getValue() {
		return value;
	}


	SaleChannel(String name, String value) {
		this.name = name;
		this.value = value;
	}
}
