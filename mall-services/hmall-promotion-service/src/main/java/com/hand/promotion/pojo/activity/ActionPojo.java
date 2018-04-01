package com.hand.promotion.pojo.activity;

import org.springframework.data.annotation.Id;

/**
 * 结果
 *
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ActionPojo implements java.io.Serializable{
	@Id
	private String id;
	/**
	 * 结果类别
	 */
	private String definitionId;
	/**
	 * 结果名称
	 */
	private String meaning;
	/**
	 * 结果参数
	 */
	private ParameterPojo parameters;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public ParameterPojo getParameters() {
		return parameters;
	}

	public void setParameters(ParameterPojo parameters) {
		this.parameters = parameters;
	}
}
