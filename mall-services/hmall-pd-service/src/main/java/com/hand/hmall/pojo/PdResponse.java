package com.hand.hmall.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ResponseData
 * @description 接口响应对象
 * @date 2017/6/3 17:49
 */
@XmlRootElement
@XmlType(propOrder = {"success", "msgCode", "msg", "total", "resp"})
public class PdResponse {

    private String msgCode;
    private String msg;
    private List<ErrorLine> resp;
    private boolean success;
    private Integer total;

    public PdResponse() {}

    public PdResponse(boolean success, String msgCode, String msg) {
        this.success = success;
        this.msgCode = msgCode;
        this.msg = msg;
    }

    public PdResponse(boolean success, String msgCode, String msg, List<ErrorLine> resp) {
        this.success = success;
        this.msgCode = msgCode;
        this.msg = msg;
        this.resp = resp;
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

    public List<ErrorLine> getResp() {
        return resp;
    }

    public void setResp(List<ErrorLine> resp) {
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
