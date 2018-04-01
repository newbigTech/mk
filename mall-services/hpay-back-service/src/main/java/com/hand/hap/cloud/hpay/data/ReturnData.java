package com.hand.hap.cloud.hpay.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jiaxin.jiang
 * @Title
 * @Description 统一返回信息
 * @date 2017/7/6
 */
public class ReturnData {

    private String msgCode;

    private String msg;

    private List<?> resp;

    private boolean success;

    private Integer total;

    public ReturnData() {

    }

    public ReturnData(String msgCode, String msg, List<?> resp, boolean success, Integer total) {
        this.msgCode = msgCode;
        this.msg = msg;
        this.resp = resp;
        this.success = success;
        this.setTotal(Integer.valueOf(resp.size()));
    }

    public ReturnData(String msgCode, String msg, boolean success) {
        this.msgCode = msgCode;
        this.msg = msg;
        this.success = success;

    }

    public ReturnData(String msgCode, String msg, List<?> resp, boolean success) {
        this.msgCode = msgCode;
        this.msg = msg;
        this.resp = resp;
        this.success = success;
        this.total = resp.size();
    }

    public ReturnData(boolean success) {
        this.success = true;
        this.setSuccess(success);
    }

    public ReturnData(List<?> list) {
        this(true);
        this.setResp(list);
        this.total = resp.size();
    }

    public ReturnData(boolean success, String msgCode) {
        this.success = true;
        this.setSuccess(success);
        this.setMsgCode(msgCode);
    }


    public ReturnData(Map<String, ?> map) {
        this(true);
        List list = new ArrayList();
        list.add(map);
        this.setResp(list);
        this.total = list.size();
    }

    public String getMsgCode() {
        return this.msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<?> getResp() {
        return this.resp;
    }

    public void setResp(List<?> resp) {
        this.resp = resp;
        this.setTotal(Integer.valueOf(resp.size()));
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
