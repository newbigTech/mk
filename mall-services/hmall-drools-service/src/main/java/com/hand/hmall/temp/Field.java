package com.hand.hmall.temp;
//该类存储促销条件与操作的输入参数与比较操作符
public class Field{
    private String fieldId;
    private String fieldName;
    private String meaning;
    private String modelId;
    private String type;
    private String containId;
    private String operator;
    private String value;
    private String definitionId;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getFieldName() {
            return fieldName;
        }

    public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getContainId() {
        return containId;
    }

    public void setContainId(String containId) {
        this.containId = containId;
    }

    public String getOperator() {
            return operator;
        }

    public void setOperator(String operator) {
            this.operator = operator;
        }

    public String getValue() {
            return value;
        }

    public void setValue(String value) {
            this.value = value;
        }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

}