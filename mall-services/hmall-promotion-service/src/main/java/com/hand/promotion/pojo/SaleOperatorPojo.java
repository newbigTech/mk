package com.hand.promotion.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/25
 * @description 促销、优惠券 修改人信息
 */
@Document(collection = "SaleOperator")
public class SaleOperatorPojo {
    @Id
    private String id;

    /**
     * 操作人id
     */
    private String operator;

    /**
     * 操作描述
     */
    private String operation;

    /**
     * 修改日期
     */
    private Long changeDate;

    /**
     * 操作的数据类型（促销活动、优惠券、促销活动模板）
     */
    private String type;
    /**
     * 关联的被修改的数据主键
     */
    private String baseId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Long getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Long changeDate) {
        this.changeDate = changeDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    @Override
    public String toString() {
        return "{\"SaleOperatorPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"operator\":\"" + operator + "\""
            + ",                         \"operation\":\"" + operation + "\""
            + ",                         \"changeDate\":\"" + changeDate + "\""
            + ",                         \"type\":\"" + type + "\""
            + ",                         \"baseId\":\"" + baseId + "\""
            + "}}";
    }
}
