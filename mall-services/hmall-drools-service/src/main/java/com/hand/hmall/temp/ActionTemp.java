package com.hand.hmall.temp;

/**
 * Created by hand on 2017/1/19.
 * 促销结果对应DTO
 */
public class ActionTemp {
    //促销结果id
    private String actionId;
    //结果关联的FACT对象的id
    private String modelId;
    //优惠处理逻辑所在的类，方法。如actionService.orderDiscount(30);
    private String actionCode;
    //描述
    private String meaning;
    //对应的结果id
    private String definitionId;
    private String modelVariable;
    private String dataVariable;
    private String actionModel;
    private String actionData;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getModelVariable() {
        return modelVariable;
    }

    public void setModelVariable(String modelVariable) {
        this.modelVariable = modelVariable;
    }

    public String getDataVariable() {
        return dataVariable;
    }

    public void setDataVariable(String dataVariable) {
        this.dataVariable = dataVariable;
    }

    public String getActionModel() {
        return actionModel;
    }

    public void setActionModel(String actionModel) {
        this.actionModel = actionModel;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }
}
