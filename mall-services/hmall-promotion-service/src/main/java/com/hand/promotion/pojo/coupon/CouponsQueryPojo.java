package com.hand.promotion.pojo.coupon;

/**
 * 我的优惠券查询
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class CouponsQueryPojo implements java.io.Serializable {
	private String status;
	private Integer page;
	private Integer pageSize;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
