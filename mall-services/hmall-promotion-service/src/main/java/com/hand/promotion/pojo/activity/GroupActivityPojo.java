package com.hand.promotion.pojo.activity;




import com.hand.promotion.pojo.order.PromotionGroupPojo;

import java.util.List;

/**
 * 规则组-促销规则组合pojo
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class GroupActivityPojo implements java.io.Serializable{
	/**
	 * 组里的促销规则
	 */
	private List<PromotionActivitiesPojo> activities;

	private PromotionGroupPojo groupInfo;

	public List<PromotionActivitiesPojo> getActivities() {
		return activities;
	}

	public void setActivities(List<PromotionActivitiesPojo> activities) {
		this.activities = activities;
	}

	public PromotionGroupPojo getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(PromotionGroupPojo groupInfo) {
		this.groupInfo = groupInfo;
	}
}
