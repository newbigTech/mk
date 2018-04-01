package com.hand.hmall.ws.entities;

import java.io.Serializable;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name MstFabricModel
 * @description
 * @date 2017/7/3
 */
public class MstFabricModel implements Serializable {

    //面料编码
    private String fabricCode;
    //面料等级
    private String fabricLevel;

    public void setFabricCode(String fabricCode) {
        this.fabricCode = fabricCode;
    }

    public String getFabricCode() {
        return fabricCode;
    }

    public void setFabricLevel(String fabricLevel) {
        this.fabricLevel = fabricLevel;
    }

    public String getFabricLevel() {
        return fabricLevel;
    }

}
