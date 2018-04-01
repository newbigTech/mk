package com.hand.hmall.ws.entities;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: retail推送员工信息至hmall的反映实体类
 * @date 2017/6/20 9:49
 */
public class EmployeeResponseBody {

    private String code;

    private String massage;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
