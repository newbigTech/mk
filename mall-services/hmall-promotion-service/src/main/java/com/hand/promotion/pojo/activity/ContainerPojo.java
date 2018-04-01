package com.hand.promotion.pojo.activity;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ContainerPojo implements java.io.Serializable{
	@Id
	private String id;
	/**
	 * 条件类别
	 */
	private String definitionId;
	/**
	 * 条件名称
	 */
	private String meaning;

	private Integer flag;

	private List<ChildPojo> child;
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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public ParameterPojo getParameters() {
		return parameters;
	}

	public void setParameters(ParameterPojo parameters) {
		this.parameters = parameters;
	}

	public List<ChildPojo> getChild() {
		return child;
	}

	public void setChild(List<ChildPojo> child) {
		this.child = child;
	}
}
