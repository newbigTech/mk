package com.hand.hmall.code;

/**
 * @author 唐磊
 * @version 0.1
 * @name:MessageCode
 * @Description:用于用户相关业务的返回状态信息
 * @date 2017/6/3 13:30
 */
public enum MessageCode {
    UR_LOGIN_03("用户名不存在"),
    UR_LOGIN_002("用户名不能为空"),

    UR_PASSWORD_003("登陆成功"),
    UR_PASSWORD_004("密码不合法"),
    UR_PASSWORD_021("密码错误"),
    UR_PASSWORD_023("密码不能为空"),

    UR_ADDRESS_005("地址字段不允许为空"),

    UR_SEX_006("性别不允许为空"),
    UR_SEX_007("性别不合法"),

    UR_CODE_008("用户组Code不能为空"),
    UR_CODE_009("Code已经存在"),

    UR_CHECK_010("必须先生成验证码"),
    UR_CHECK_011("验证码失效"),
    UR_CHECK_019("验证码正确"),
    UR_CHECK_020("验证码错误"),
    UR_CHECK_024("验证码不能为空"),
    UR_CHECK_025("验证码生成成功"),

    UR_EMAIL_025("邮箱格式不合法"),

    UR_REGISTER_026("注册成功"),

    UR_UPDATE_012("更新成功"),
    UR_MOBILE_013("手机号不能为空"),
    UR_MOBILE_014("手机号合法且未被使用") ,
    UR_MOBILE_015("手机号合法但被使用"),
    UR_MOBILE_022("手机号不合法"),
    UR_SELECT_016("查询成功");
    private String key;

    private String value;

    private MessageCode( String value) {

        this.value = value;
    }


    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.value;

    }

}
