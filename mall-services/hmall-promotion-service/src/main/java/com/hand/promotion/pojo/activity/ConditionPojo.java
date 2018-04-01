package com.hand.promotion.pojo.activity;

import org.springframework.data.annotation.Id;

/**
 * @author zhuweifeng
 * @date 2017/9/5
 */
public class ConditionPojo implements java.io.Serializable {

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
    /**
     * 优先级
     */
    private Integer priority;
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

    public ParameterPojo getParameters() {
        return parameters;
    }

    public void setParameters(ParameterPojo parameters) {
        this.parameters = parameters;
    }


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "{\"ConditionPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"definitionId\":\"" + definitionId + "\""
            + ",                         \"meaning\":\"" + meaning + "\""
            + ",                         \"priority\":\"" + priority + "\""
            + ",                         \"parameters\":" + parameters
            + "}}";
    }
}
