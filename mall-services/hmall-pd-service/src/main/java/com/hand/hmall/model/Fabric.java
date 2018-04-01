package com.hand.hmall.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 马君
 * @version 0.1
 * @name Fabric
 * @description 面料
 * @date 2017/6/6 14:25
 */
@Table(name = "HMALL_MST_FABRIC")
public class Fabric {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_FABRIC_S.nextval from dual")
    private Long fabricId;

    /**
     * 面料编码
     */
    private String fabricCode;

    /**
     * 面料等级
     */
    private String fabricLevel;

	public Long getFabricId() {
		return fabricId;
	}

	public void setFabricId(Long fabricId) {
		this.fabricId = fabricId;
	}

	public String getFabricCode() {
		return fabricCode;
	}

	public void setFabricCode(String fabricCode) {
		this.fabricCode = fabricCode;
	}

	public String getFabricLevel() {
		return fabricLevel;
	}

	public void setFabricLevel(String fabricLevel) {
		this.fabricLevel = fabricLevel;
	}

    
}