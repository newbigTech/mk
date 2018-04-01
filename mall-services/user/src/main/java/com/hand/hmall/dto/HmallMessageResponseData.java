/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.dto;

/**
 * @author 李伟
 * @version 1.0
 * @name:HmallMessageResponseData
 * @Description:
 * @date 2017/8/2 11:49
 */
public class HmallMessageResponseData {

    String code; // 请求状态码，取值0000（成功）
    String msgid; //短信唯一标识符
    String codemsg; //状态码相应的解释


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getCodemsg() {
        return codemsg;
    }

    public void setCodemsg(String codemsg) {
        this.codemsg = codemsg;
    }
}
