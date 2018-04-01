package com.hand.hmall.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 马君
 * @version 0.1
 * @name VCodeHeader
 * @description V码头
 * @date 2017/6/6 14:25
 */
@Table(name = "HAP_MAM_VCODE_HEADER")
public class VCodeHeader {
    /**
     * 表ID，主键，供其他表做外键
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HAP_MAM_VCODE_HEADER_S.nextval from dual")
    private Long headerId;

    /**
     * 字段编码
     */
    private String vcode;

    /**
     * 接口字段bomid拼接
     */
    private String segment;

    /**
     * 平台代码
     */
    private String platformCode;

    /**
     * D订制品  Z是主推品
     */
    private String typeCode;

    /**
     * 主推商品编码
     */
    private String ztmatnr;

    /**
     * 主推商品描述
     */
    private String zttext;

	public Long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getZtmatnr() {
		return ztmatnr;
	}

	public void setZtmatnr(String ztmatnr) {
		this.ztmatnr = ztmatnr;
	}

	public String getZttext() {
		return zttext;
	}

	public void setZttext(String zttext) {
		this.zttext = zttext;
	}
}