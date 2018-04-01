package com.hand.hmall.util;

import com.hand.hmall.dto.ResponseData;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name ResponseDataUtil
 * @description ResponseData工具类
 * @date 2017/7/7 11:19
 */
public class ResponseDataUtil {
    /**
     * 创建一个ResponseData
     * @param success 是否成功
     * @param msgCode 错误编号
     * @param message 错误消息
     * @return ResponseData
     */
    public static ResponseData createResponseData(boolean success, String msgCode, String message) {
        ResponseData responseData = new ResponseData(success, msgCode);
        responseData.setMsg(message);
        return responseData;
    }

    /**
     * 创建一个ResponseData
     * @param success 是否成功
     * @param msgCode 错误编号
     * @param message 错误消息
     * @param resp 返回信息
     * @return ResponseData
     */
    public static ResponseData createResponseData(boolean success, String msgCode, String message, List<?> resp) {
        ResponseData responseData = new ResponseData(success, msgCode);
        responseData.setMsg(message);
        responseData.setResp(resp);
        return responseData;
    }
}
