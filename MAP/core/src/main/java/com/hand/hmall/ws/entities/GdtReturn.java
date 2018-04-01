package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;

/**
 * @author 唐磊
 * @version 0.1
 * @name:
 * @Description: 订单推送 Hmall->retail
 * @date 2017/6/8 10:25
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GdtReturn {
    @XmlElements(value = @XmlElement(name = "item"))
    private List<ReturnItem> items;

    public List<ReturnItem> getItems() {
        return items;
    }

    public void setItems(List<ReturnItem> items) {
        this.items = items;
    }
}
