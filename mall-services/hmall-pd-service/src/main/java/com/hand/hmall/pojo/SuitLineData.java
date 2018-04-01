package com.hand.hmall.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 马君
 * @version 0.1
 * @name SuitLineData
 * @description 套件关系
 * @date 2017/6/2 14:34
 */

@XmlRootElement
public class SuitLineData {

    /*
    * 组件商品编码
    * */
    private String componentCode;

    /*
    * 组件商品数量
    * */
    private Long quantity;

    @XmlElement(name = "componentCode", required = true)
    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    @XmlElement(name = "quantity", required = true)
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
