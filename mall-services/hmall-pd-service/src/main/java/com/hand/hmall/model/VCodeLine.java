package com.hand.hmall.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 马君
 * @version 0.1
 * @name VCodeLine
 * @description V码行
 * @date 2017/6/6 14:25
 */
@Table(name = "HAP_MAM_VCODE_LINES")
public class VCodeLine {
    /**
     * 表ID，主键，供其他表做外键
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HAP_MAM_VCODE_LINES_S.nextval from dual")
    private Long lineId;

    /**
     * 头表id hap_mam_vcode_header.header_id
     */
    private Long headerId;

    /**
     * 定制平台号 
     */
    private String matnr;

    /**
     * BOM节点ID HAP_MDM_SYSTEM_BOM_B.bom_id
     */
    private String bomid;

    /**
     * 工厂
     */
    private String werks;

    /**
     * 项目类别（物料单） 
     */
    private String postp;

    /**
     * BOM 项目号 
     */
    private String posnr;

    /**
     * BOM 组件
     */
    private String idnrk;

    /**
     * 组件数量
     */
    private String menge;

    /**
     * 组件计量单位
     */
    private String meins;

    /**
     * BOM 项目文本（行1） 
     */
    private String potx1;

    /**
     * BOM 项目文本 （行 2） 
     */
    private String potx2;

    /**
     * 用量
     */
    private Integer quantity;

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getBomid() {
		return bomid;
	}

	public void setBomid(String bomid) {
		this.bomid = bomid;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getPostp() {
		return postp;
	}

	public void setPostp(String postp) {
		this.postp = postp;
	}

	public String getPosnr() {
		return posnr;
	}

	public void setPosnr(String posnr) {
		this.posnr = posnr;
	}

	public String getIdnrk() {
		return idnrk;
	}

	public void setIdnrk(String idnrk) {
		this.idnrk = idnrk;
	}

	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}

	public String getMeins() {
		return meins;
	}

	public void setMeins(String meins) {
		this.meins = meins;
	}

	public String getPotx1() {
		return potx1;
	}

	public void setPotx1(String potx1) {
		this.potx1 = potx1;
	}

	public String getPotx2() {
		return potx2;
	}

	public void setPotx2(String potx2) {
		this.potx2 = potx2;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}