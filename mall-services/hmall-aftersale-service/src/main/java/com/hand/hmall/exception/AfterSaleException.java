package com.hand.hmall.exception;

/**
 * @version 1.0
 * @name AfterSaleException
 * @Describe com.hand.hmall.exception.AfterSaleException
 * @Author chenzhigang
 * @Date 2017/09/01
 */
public class AfterSaleException extends RuntimeException {

    private String msgCode;

    public AfterSaleException(String msgCode, String message, Throwable cause) {
        super(message, cause);
        this.msgCode = msgCode;
    }

    public String getMsgCode() {
        return msgCode;
    }

}
