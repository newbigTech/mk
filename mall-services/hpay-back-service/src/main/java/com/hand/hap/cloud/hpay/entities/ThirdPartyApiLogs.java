package com.hand.hap.cloud.hpay.entities;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

/**
 * author: zhangzilong
 * name: ThirdPartyApiLogs
 * discription: 日志实体类
 * date: 2017/8/20
 * version: 0.1
 */
public class ThirdPartyApiLogs {
    
    private String requestBody;

    private Date requestTime;

    private Date responseTime;

    private long during;

    private String target;

    private String clientType;

    private String requestMethod;

    private boolean success;

    private String responseBody;

    private String exceptionMsg;

    private String exceptionCode;

    private String respMsgCode;

    private String respMsg;

    private String exceptionStack;

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public long getDuring() {
        return during;
    }

    public void setDuring(long during) {
        this.during = during;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public void setResponseBody(Map responseBody){
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        try {
            objectMapper.writeValue(stringWriter,responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.responseBody = stringWriter.toString();
    }

    public String getRespMsgCode() {
        return respMsgCode;
    }

    public void setRespMsgCode(String respMsgCode) {
        this.respMsgCode = respMsgCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getExceptionStack() {
        return exceptionStack;
    }

    public void setExceptionStack(String exceptionStack) {
        this.exceptionStack = exceptionStack;
    }
    
    public void setExceptionStack(StackTraceElement[] stackTraceElements){
        StringBuilder exceptionStack = new StringBuilder();
        for (StackTraceElement element : stackTraceElements){
            exceptionStack.append(element.toString());
        }
        this.exceptionStack = exceptionStack.toString();
    }
}
