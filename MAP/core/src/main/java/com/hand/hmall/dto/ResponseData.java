package com.hand.hmall.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据返回对象.
 * Created by DongFan on 2016/11/15.
 *
 * @author fan.dong@hand.china.com
 */
public class ResponseData {

    // 返回状态编码
    private String msgCode;

    // 返回信息
    private String msg;

    private List<?> resp;

    // 成功标识
    private boolean success = true;

    private Integer total = 0;

    public ResponseData() {
    }

    public ResponseData(boolean success) {
        setSuccess(success);
    }

    public ResponseData(List<?> list) {
        this(true);
        setResp(list);
        setTotal(list.size());
    }

    public ResponseData(boolean success, String msgCode) {
        this.setSuccess(success);
        this.setMsgCode(msgCode);
    }

    public ResponseData(Map<String, ?> map) {
        this(true);
        List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
        list.add(map);
        setResp(list);
        setTotal(1);
    }

    public String getMsgCode() {
        return msgCode;
    }

    public Integer getTotal() {
        return total;
    }

    public String getMsg() {
        return msg;
    }

    public List<?> getResp() {
        return resp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResp(List<?> resp) {
        this.resp = resp;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
