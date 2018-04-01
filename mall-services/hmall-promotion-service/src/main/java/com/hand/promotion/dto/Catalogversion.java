package com.hand.promotion.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 马君
 * @version 0.1
 * @name Catalogversion
 * @description 目录版本
 * @date 2017/6/3 15:27
 */
@Entity
@Table(name = "HMALL_MST_CATALOGVERSION")
public class Catalogversion {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HMALL_MST_CATALOGVERSION_S.nextval from dual")
    private Long catalogversionId;

    /**
     * 目录
     */
    private Long catalog;

    /**
     * 版本
     */
    private String catalogversion;


    public Long getCatalogversionId() {
        return catalogversionId;
    }

    public void setCatalogversionId(Long catalogversionId) {
        this.catalogversionId = catalogversionId;
    }

    public Long getCatalog() {
        return catalog;
    }

    public void setCatalog(Long catalog) {
        this.catalog = catalog;
    }

    public String getCatalogversion() {
        return catalogversion;
    }

    public void setCatalogversion(String catalogversion) {
        this.catalogversion = catalogversion;
    }
}
