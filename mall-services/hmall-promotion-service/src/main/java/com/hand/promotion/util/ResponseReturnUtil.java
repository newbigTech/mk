package com.hand.promotion.util;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.SimpleMessagePojo;
import com.hand.promotion.pojo.enums.MsgMenu;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shanks on 2017/2/24.
 */
public class ResponseReturnUtil {

    public static ResponseData returnTrueResp(List resp) {
        ResponseData responseData = new ResponseData();
        responseData.setResp(resp);
//        responseData.setTotal(resp.size());
        return responseData;
    }

    public static ResponseData returnResp(List resp, MsgMenu msgMenu, boolean isSuccess) {
        ResponseData responseData = new ResponseData();
        responseData.setMsgCode(msgMenu.getCode());
        responseData.setMsg(msgMenu.getMsg());
        responseData.setSuccess(isSuccess);
        responseData.setResp(resp);
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
        if (!CollectionUtils.isEmpty(respList)) {
            return respList.get(0);
        } else {
            return null;
        }
    }

    public static ResponseData transSimpleMessage(SimpleMessagePojo simpleMessagePojo) {
        ResponseData respData = new ResponseData();
        respData.setSuccess(simpleMessagePojo.isSuccess());
        respData.setMsg(simpleMessagePojo.getCheckMsg());
        respData.setMsgCode(simpleMessagePojo.getMessage().getCode());
        if (simpleMessagePojo.getObj() instanceof List) {
            respData.setResp((List) simpleMessagePojo.getObj());
            return respData;
        } else {
            respData.setResp(Arrays.asList(simpleMessagePojo.getObj()));
            return respData;
        }
    }

    public static ResponseData transMsgMenu(MsgMenu msg) {
        ResponseData responseData = new ResponseData();
        responseData.setMsgCode(msg.getCode());
        responseData.setMsg(msg.getMsg());
        return responseData;
    }

}
