package com.hand.hmall.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 马君
 * @version 0.1
 * @name Odtype
 * @description 频道
 * @date 2017/6/6 14:25
 */
@Table(name = "HMALL_MST_ODTYPE")
public class Odtype {
    /**
     * 主键
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_ODTYPE_S.nextval from dual")
    private Long odtypeId;

    /**
     * 商品
     */
    private Long productId;

    /**
     * 定制来源频道
     */
    private String custChanSrc;

    /**
     * 上下架状态
     */
    private String approvalStatus;

    /**
     * 是否使用
     */
    private String isUsed;

	public Long getOdtypeId() {
		return odtypeId;
	}

	public void setOdtypeId(Long odtypeId) {
		this.odtypeId = odtypeId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getCustChanSrc() {
		return custChanSrc;
	}

	public void setCustChanSrc(String custChanSrc) {
		this.custChanSrc = custChanSrc;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
}