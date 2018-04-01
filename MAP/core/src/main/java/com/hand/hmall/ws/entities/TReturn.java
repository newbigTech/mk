package com.hand.hmall.ws.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 订单更新retail，响应报文中T_RETURN实体类
 * @date 2017/8/18 16:19
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TReturn {

    @XmlElements(value = @XmlElement(name = "item"))
    private List<UpdateReturnItem> items;

    public List<UpdateReturnItem> getItems() {
        return items;
    }

    public void setItems(List<UpdateReturnItem> items) {
        this.items = items;
    }
}
