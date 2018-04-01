package com.hand.promotion.pojo.group;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/20
 * @description 促销分组pojo
 */
@Document(collection = "Group")
public class SaleGroupPojo {

    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组优先级
     */
    private Integer priority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {

        return "{\"SaleGroupPojo\":{"
            + "                        \"id\":\"" + id + "\""
            + ",                         \"name\":\"" + name + "\""
            + ",                         \"priority\":\"" + priority + "\""
            + "}}";
    }
}
