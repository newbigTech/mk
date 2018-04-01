package com.hand.hmall.util;

import com.hand.hmall.dto.ResponseData;

/**
 * Created by shanks on 2017/2/24.
 */
public class ResponseReturnUtil {

    public static ResponseData returnFalseResponse(String msg, String msgCode) {
        ResponseData responseData = returnFalseResponse(msg);
        responseData.setMsgCode(msgCode);
        return responseData;
    }

    public static ResponseData returnFalseResponse(String msg) {
        ResponseData responseData = new ResponseData();
        responseData.setMsg(msg);
        responseData.setSuccess(false);
        return responseData;
    }
}
