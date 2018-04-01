package com.hand.hmall.service;

import com.hand.hmall.dto.HmallMstUser;
import java.security.NoSuchAlgorithmException;


/**
 * @author 唐磊
 * @name:UserServiceImpl
 * @Description:用户相关业务逻辑
 * @date 2017/5/23 19:11
 */
public interface IUserService
{
    /**
     * 根据用户名查询用户
     * @param customerId 用户名
     * @return
     */
    HmallMstUser selectByCustomerId(String customerId);


    /**
     * 登陆校验
     * @param mobileNumber 手机号
     * @param pwd 密码
     * @return
     * @throws NoSuchAlgorithmException
     */
    Boolean validate(String mobileNumber, String pwd) throws NoSuchAlgorithmException;
}
