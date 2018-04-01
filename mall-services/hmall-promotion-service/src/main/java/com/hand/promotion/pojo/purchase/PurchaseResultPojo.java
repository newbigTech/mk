package com.hand.promotion.pojo.purchase;

/**
 * 查询限购结果
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class PurchaseResultPojo implements java.io.Serializable{
	private Boolean check;
	private Integer limitNum = 0;

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public Integer getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}
}
