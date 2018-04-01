package com.hand.promotion.pojo.activity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/20
 * @description 可选择条件、结果pojo
 */
@Document(collection = "SelectConditionAction")
public class SelectConditionActionPojo {

    @Id
    private String id;
    /**
     * 条件是基础条件（ADD_CONDITION）、还是组内可选条件（ADD_GROUP）、还是容器（ADD_CONTAINER）可选条件
     */
    private String code;

    /**
     * 条件用于促销层级还是商品层级
     */
    private String level;

    /**
     * 条件结果描述信息
     */
    private String meaning;

    /**
     * 用于优惠券还是促销
     */
    private String type;
    /**
     * 优先级，用于显示排序
     */
    private Integer priority;
    /**
     * 版本号
     */
    private String version;
    /**
     * 条件、结果的业务识别id
     */
    private String definitionId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    @Override
    public String toString() {
        return "{\"SelectConditionActionPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"code\":\"" + code + "\""
            + ",                         \"level\":\"" + level + "\""
            + ",                         \"meaning\":\"" + meaning + "\""
            + ",                         \"type\":\"" + type + "\""
            + ",                         \"priority\":\"" + priority + "\""
            + ",                         \"version\":\"" + version + "\""
            + ",                         \"definitionId\":\"" + definitionId + "\""
            + "}}";
    }
}
