package com.hand.hmall.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author 马君
 * @version 0.1
 * @name: PatchLineData
 * @description 补件关系
 * @date 2017/6/2 14:42
 */
@XmlRootElement
public class PatchLineData {

    /*
    * 补件商品编码
    * */
    private String patchLineCode;

    @XmlElement(name = "patchLineCode", required = true)
    public String getPatchLineCode() {
        return patchLineCode;
    }

    public void setPatchLineCode(String patchLineCode) {
        this.patchLineCode = patchLineCode;
    }
}
