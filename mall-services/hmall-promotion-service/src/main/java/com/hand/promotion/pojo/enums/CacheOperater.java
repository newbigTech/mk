package com.hand.promotion.pojo.enums;


/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2017/12/18
 * @description m
 */
public enum CacheOperater {

    insert("insert", 1),

    update("update", 2),

    delete("delete", 3);

    private String operate;
    private int opeNum;


    CacheOperater(String operate, int opeNum) {
        this.operate = operate;
        this.opeNum = opeNum;
    }

    public String getOperate() {
        return operate;
    }



    public int getOpeNum() {
        return opeNum;
    }

}
