package com.hand.hmall.common;

/**
 * @author 马君
 * @version 0.1
 * @name ResponseType
 * @description 接口返回类型
 * @date 2017/7/14 11:16
 */
public interface ResponseType {

    // 返回成功
    interface SUCCESS {
        String CODE = "1";
        String MESSAGE = "操作成功";
        String PARTIAL_SUCCESS_MESSAGE = "部分成功";
    }
    // 返回失败
    interface FAILURE {
        String CODE = "2";
        String MESSAGE = "操作失败";
    }
    // 参数异常
    interface INVALID_PARAMETER {
        String CODE = "6001";
        String MESSAGE = "操作失败";
    }
    interface SYSTEM_ERROR {
        String CODE = "4002";
        String MESSAGE = "系统异常";
    }
}
