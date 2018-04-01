package com.hand.hmall.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 马君
 * @version 0.1
 * @name Pricerow
 * @description 价格行
 * @date 2017/6/6 14:25
 */
@Table(name = "HMALL_MST_PRICEROW")
public class Pricerow {
    /**
     * 主键
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_PRICEROW_S.nextval from dual")
    private Long pricerowId;

    /**
     * 商品
     */
    private Long productId;

    /**
     * 客户
     */
    private Long userId;

    /**
     * 用户组
     */
    private Long userGroupId;

    /**
     * 基础销售价格
     */
    private Double basePrice;

    /**
     * 最低价
     */
    private Double bottomPrice;

    /**
     * 销售时使用单位
     */
    private String saleUnit;

    /**
     * 单位转换率
     */
    private Double rate;

    /**
     * 接口传输标示
     */
    private String syncflag;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 价格类型
	 */
	private String priceType;

	/**
	 * 框架等级
	 */
	private String productGrade;

	/**
	 * 是否一口价(Y/N)
	 */
	private String isBottom;

	/**
	 * 价目表
	 */
	private String priceGroup;

	/*
	* 创建
	* */
	private Date creationDate;

	/*
	* 频道
	* */
	private String odtype;

	public Long getPricerowId() {
		return pricerowId;
	}

	public void setPricerowId(Long pricerowId) {
		this.pricerowId = pricerowId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Double getBottomPrice() {
		return bottomPrice;
	}

	public void setBottomPrice(Double bottomPrice) {
		this.bottomPrice = bottomPrice;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getSyncflag() {
		return syncflag;
	}

	public void setSyncflag(String syncflag) {
		this.syncflag = syncflag;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getProductGrade() {
		return productGrade;
	}

	public void setProductGrade(String productGrade) {
		this.productGrade = productGrade;
	}

	public String getIsBottom() {
		return isBottom;
	}

	public void setIsBottom(String isBottom) {
		this.isBottom = isBottom;
	}

	public String getPriceGroup() {
		return priceGroup;
	}

	public void setPriceGroup(String priceGroup) {
		this.priceGroup = priceGroup;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getOdtype() {
		return odtype;
	}

	public void setOdtype(String odtype) {
		this.odtype = odtype;
	}
}