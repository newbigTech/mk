package com.hand.promotion.pojo.activity;

import java.util.List;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class GroupPojo implements java.io.Serializable{
	private String id;
	private String parentId;
	private String definitionId;
	private String meaning;
	private String operator;
	private List<ChildPojo> child;

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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<ChildPojo> getChild() {
		return child;
	}

	public void setChild(List<ChildPojo> child) {
		this.child = child;
	}
}
