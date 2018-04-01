package com.hand.hap.cloud.thirdParty.entities;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * author: zhangzilong
 * name: OutBoundLogs
 * discription: Rest类型的请求日志
 * date: 2017/6/12
 */
public class OutBoundLogs implements Serializable {

    //ID由MongoDB自动生成
    @Id
    private String _id;
    //请求时间
    private Date requestTime;
    //请求方式
    private String requestMethod;
    //请求头
    private String requestBody;
    //请求地址
    private String requestAddr;
    //目标系统
    private String targetSystem;
    //响应时间
    private Date responseTime;
    private String responseBody;
    //用时
    private long during;
    //调用结果(成功/失败)
    private boolean success;
    //异常信息
    private String exceptionStack;
    //信息
    private String message;

    private String usage;

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestAddr() {
        return requestAddr;
    }

    public void setRequestAddr(String requestAddr) {
        this.requestAddr = requestAddr;
    }

    public String getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
        this.targetSystem = targetSystem;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public long getDuring() {
        return during;
    }

    public void setDuring(long during) {
        this.during = during;
    }

    public String getExceptionStack() {
        return exceptionStack;
    }

    public void setExceptionStack(String exceptionStack) {
        this.exceptionStack = exceptionStack;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setExceptionStack(StackTraceElement[] stackTraceElements){
        StringBuilder exceptionStack = new StringBuilder();
        for (StackTraceElement element : stackTraceElements){
            exceptionStack.append(element.toString());
        }
        this.exceptionStack = exceptionStack.toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
