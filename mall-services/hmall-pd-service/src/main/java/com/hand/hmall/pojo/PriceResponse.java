package com.hand.hmall.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PriceResponse
 * @description 价格计算返回消息
 * @date 2017/7/19 15:54
 */
@XmlRootElement
@XmlType(propOrder = {"success", "msgCode", "msg", "total", "resp"})
public class PriceResponse {
    // 错误编号
    private String msgCode;
    // 错误消息
    private String msg;
    // 返回数据
    private List<PriceResponseRow> resp;
    // 请求是否成功
    private boolean success;
    // 返回信息条数
    private Integer total;

    public PriceResponse(){}

    public PriceResponse(boolean success, String msgCode, String msg) {
        this.success = success;
        this.msg = msg;
        this.msgCode = msgCode;
    }

    public PriceResponse(boolean success, String msgCode, String msg, List<PriceResponseRow> resp) {
        this.msgCode = msgCode;
        this.msg = msg;
        this.resp = resp;
        this.success = success;
        this.total = resp.size();
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<PriceResponseRow> getResp() {
        return resp;
    }

    @XmlElementWrapper(name = "resp")
    @XmlElement(name = "priceRow")
    public void setResp(List<PriceResponseRow> resp) {
        this.resp = resp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
