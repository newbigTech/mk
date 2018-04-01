package com.hand.hmall.model;

import javax.persistence.*;

/**
 * @author 马君
 * @version 0.1
 * @description  目录实体类
 * @date 2017/6/2 16:18
 */
@Entity
@Table(name = "HMALL_MST_CATALOGS")
public class Catalogs {
    /**
     * 主键
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_CATALOGS_S.nextval from dual")
    private Long catalogsId;

    /**
     * 目录编码
     */
    private String code;

    /**
     * 目录名称
     */
    private String nameZh;

	public Long getCatalogsId() {
		return catalogsId;
	}

	public void setCatalogsId(Long catalogsId) {
		this.catalogsId = catalogsId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

    
}