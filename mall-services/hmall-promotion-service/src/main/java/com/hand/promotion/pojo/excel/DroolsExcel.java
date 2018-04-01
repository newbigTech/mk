package com.hand.promotion.pojo.excel;

import java.io.Serializable;

/**
 * 导入商品Excel表
 * Created by darkdog on 2018/2/5.
 */
public class DroolsExcel implements Serializable{

    private String id;

    /**
     * 导入Excel的文件名
     */
    private String excelName;

    /**
     * 标识excel存储的数据类型，目前只有商品类型的数据
     */
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
