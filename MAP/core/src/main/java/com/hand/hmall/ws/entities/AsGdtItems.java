package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * author: zhangzilong
 * name: AsGdtItems.java
 * discription: 
 * date: 2017/8/29
 * version: 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AsGdtItems {

    @XmlElement(name = "item")
    private List<AsGdtItemsItem> items;

    public List<AsGdtItemsItem> getItems() {
        return items;
    }

    public void setItems(List<AsGdtItemsItem> items) {
        this.items = items;
    }
}
