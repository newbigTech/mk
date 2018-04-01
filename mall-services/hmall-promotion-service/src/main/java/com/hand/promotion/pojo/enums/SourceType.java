package com.hand.promotion.pojo.enums;

/**
 * 来源类别
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public enum SourceType {
	/**
	 * 促销赠送
	 */
	PROMOTION("促销赠送", "PROMOTION"),
	/**
	 * 抽奖获取
	 */
	DRAW("抽奖获取", "DRAW"),
	/**
	 * 优惠码兑换
	 */
	CODE("优惠码兑换", "CODE"),
	/**
	 * 用户领取
	 */
	USER("用户领取", "USER"),
	/**
	 * 系统发放
	 */
	SYSTEM("系统发放", "SYSTEM");

	private String name;
	private String value;

	SourceType(String name, String value) {
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
