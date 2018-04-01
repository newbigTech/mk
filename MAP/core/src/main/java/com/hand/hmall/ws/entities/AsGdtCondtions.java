package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * author: zhangzilong
 * name: AsGdtCondtions.java
 * discription:
 * date: 2017/8/29
 * version: 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AsGdtCondtions {

    @XmlElement(name = "item")
    private List<AsGdtCondtionItem> items;

    public List<AsGdtCondtionItem> getItems() {
        return items;
    }

    public void setItems(List<AsGdtCondtionItem> items) {
        this.items = items;
    }
}
