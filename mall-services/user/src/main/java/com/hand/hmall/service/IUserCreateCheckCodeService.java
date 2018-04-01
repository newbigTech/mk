package com.hand.hmall.service;

import com.hand.hmall.dto.ResponseData;

/**
 * @author 阳赳
 * @name:IUserCreateCheckcodeService
 * @Description:验证码的生成
 * @date 2017/6/1 15:27
 */
public interface IUserCreateCheckCodeService {

    //验证码与手机号进行绑定
    ResponseData insertOracle (String mobileNumber, String sendType);

    //发送短信验证码
    ResponseData sendMessage(String mobileNumber, String sendType);

    //验证码生成
    String createCheckCode();

}

