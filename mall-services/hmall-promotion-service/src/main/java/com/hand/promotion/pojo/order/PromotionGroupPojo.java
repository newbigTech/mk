package com.hand.promotion.pojo.order;

import com.hand.promotion.pojo.BasePojo;

/**
 * 接收hap的规则组的传参对象
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class PromotionGroupPojo extends BasePojo implements java.io.Serializable{
	/**
	 * 规则组ID
	 */
	private Long groupId;

	/**
	 * 规则组名称
	 */
	private String groupName;

	/**
	 * 规则组优先级
	 */
	private Integer priority;

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getPriority() {
		return priority;
	}

}
