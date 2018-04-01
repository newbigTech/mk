package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * author: zhangzilong
 * name: AsChangeGdtReturn.java
 * discription:
 * date: 2017/8/29
 * version: 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AsChangeGdtReturn {

    @XmlElement(name = "item")
    private List<AsChangeGdtReturnItems> asChangeGdtReturnItems;


    public List<AsChangeGdtReturnItems> getAsChangeGdtReturnItems() {
        return asChangeGdtReturnItems;
    }

    public void setAsChangeGdtReturnItems(List<AsChangeGdtReturnItems> asChangeGdtReturnItems) {
        this.asChangeGdtReturnItems = asChangeGdtReturnItems;
    }
}
