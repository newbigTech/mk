package com.hand.promotion.pojo.activity;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ChildPojo implements java.io.Serializable{
	private String id;
	/**
	 * 父id
	 */
	private String parentId;
	/**
	 * 条件类别
	 */
	private String definitionId;
	/**
	 * 条件名称
	 */
	private String meaning;

	/**
	 * 条件参数
	 */
	private ParameterPojo parameters;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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
