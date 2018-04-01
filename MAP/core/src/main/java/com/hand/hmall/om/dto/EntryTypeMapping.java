package com.hand.hmall.om.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 马君
 * @version 0.1
 * @name EntryTypeMapping
 * @description 订单行类型映射
 * @date 2017/7/30 17:58
 */
@ExtensionAttribute(disable = true)
@Table(name = "HMALL_OM_ENTRYTYPEMAPPING")
public class EntryTypeMapping {
    /**
     * 主键
     */
	@Id
	@GeneratedValue
    private Long mappingId;

    /**
     * 商品类别
     */
    private String productType;

    /**
     * 行类别
     */
    private String entryType;

    /**
     * 启动备货金额比例
     */
    private Double stockupPercent;

	public Long getMappingId() {
		return mappingId;
	}

	public void setMappingId(Long mappingId) {
		this.mappingId = mappingId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public Double getStockupPercent() {
		return stockupPercent;
	}

	public void setStockupPercent(Double stockupPercent) {
		this.stockupPercent = stockupPercent;
	}

    
}