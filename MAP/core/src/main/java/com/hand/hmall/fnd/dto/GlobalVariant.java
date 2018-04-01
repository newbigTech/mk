package com.hand.hmall.fnd.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@ExtensionAttribute(disable = true)
@Table(name = "HMALL_FND_GLOBALVARIANT")
public class GlobalVariant {
    /**
     * 主键
     */
	@Id
	@GeneratedValue
    private Long globalvariantId;

    /**
     * 全局唯一的code
     */
    private String code;

    /**
     * value
     */
    private String value;

    /**
     * 描述
     */
    private String description;

	public Long getGlobalvariantId() {
		return globalvariantId;
	}

	public void setGlobalvariantId(Long globalvariantId) {
		this.globalvariantId = globalvariantId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    
}