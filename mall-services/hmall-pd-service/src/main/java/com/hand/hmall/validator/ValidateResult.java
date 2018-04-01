package com.hand.hmall.validator;

/**
 * @author 马君
 * @version 0.1
 * @name ValidateResult
 * @description 合法性检查结果
 * @date 2017/6/5 19:53
 */
public class ValidateResult {
    /*
    * 验证是否合法
    * */
    private boolean valid;

    /*
    * 验证消息
    * */
    private String message;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
