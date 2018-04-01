package com.hand.hmall.process.consignment.pojo;

import com.hand.hmall.om.dto.OrderEntry;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name SplitRow
 * @description 拆单行
 * @date 2017/6/22 15:00
 */
public class SplitRow {
    /*
    * 订单行id
    * */
    private List<OrderEntry> orderEntries;

    /*
    * 发货单状态
    * */
    private String status;

    public List<OrderEntry> getOrderEntries() {
        return orderEntries;
    }

    public void setOrderEntries(List<OrderEntry> orderEntries) {
        this.orderEntries = orderEntries;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
