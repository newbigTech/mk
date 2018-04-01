package com.hand.promotion.pojo.activity;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class TargetValuePojo implements java.io.Serializable{
	private String id;
	private String meaning;
	private String countNumber;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getCountNumber() {
		return countNumber;
	}

	public void setCountNumber(String countNumber) {
		this.countNumber = countNumber;
	}
}
