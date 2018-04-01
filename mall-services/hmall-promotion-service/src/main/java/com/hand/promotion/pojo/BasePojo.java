package com.hand.promotion.pojo;

import java.util.Date;

/**
 * 接收hap参数基本pojo
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class BasePojo {
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 创建日期
	 */
	private Date creationDate;
	/**
	 * 最新更新人
	 */
	private Long lastUpdatedBy;
	/**
	 * 最新更新时间
	 */
	private Date lastUpdateDate;

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
}
