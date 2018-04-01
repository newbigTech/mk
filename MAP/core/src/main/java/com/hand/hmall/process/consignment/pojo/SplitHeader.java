package com.hand.hmall.process.consignment.pojo;

import com.hand.hmall.om.dto.Consignment;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name SplitHeader
 * @description 拆单请求头
 * @date 2017/6/22 14:58
 */
public class SplitHeader {
    /*
    * 发货单
    * */
    private Consignment consignment;

    /*
    * 拆单原因
    * */
    private String splitReason;

    /*
    * 拆单行
    * */
    List<SplitRow> splitRows;

    public Consignment getConsignment() {
        return consignment;
    }

    public void setConsignment(Consignment consignment) {
        this.consignment = consignment;
    }

    public String getSplitReason() {
        return splitReason;
    }

    public void setSplitReason(String splitReason) {
        this.splitReason = splitReason;
    }

    public List<SplitRow> getSplitRows() {
        return splitRows;
    }

    public void setSplitRows(List<SplitRow> splitRows) {
        this.splitRows = splitRows;
    }
}
