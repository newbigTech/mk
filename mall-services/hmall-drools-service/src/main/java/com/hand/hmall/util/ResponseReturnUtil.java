package com.hand.hmall.util;

import com.hand.hmall.dto.ResponseData;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/24.
 */
public class ResponseReturnUtil {

    public static ResponseData returnTrueResp(List resp) {
        ResponseData responseData = new ResponseData();
        responseData.setResp(resp);
        responseData.setTotal(resp.size());
        return responseData;
    }

    public static ResponseData returnFalseResponse(String msg, String msgCode) {
        ResponseData responseData = new ResponseData();
        responseData.setMsg(msg);
        responseData.setSuccess(false);
        responseData.setMsgCode(msgCode);
        return responseData;
    }


    public static List getRespList(ResponseData responseData) {
        if (responseData.isSuccess()) {
            return responseData.getResp();
        } else {
            return null;
        }
    }

    public static Object getRespObj(ResponseData responseData) {
        List respList = getRespList(responseData);
        if (CollectionUtils.isNotEmpty(respList)) {
            return respList.get(0);
        } else {
            return null;
        }
    }
}
