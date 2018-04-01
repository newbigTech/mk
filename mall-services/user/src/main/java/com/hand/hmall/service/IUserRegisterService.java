package com.hand.hmall.service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author 唐磊
 * @name:UserRegisterServiceImpl
 * @Description:实现用户注册相关逻辑
 * @date 2017/5/23 19:10
 * @version:1.0
 */
public interface IUserRegisterService {

    /**
     * 校验手机号
     *
     * @param mobile 手机号码 值与用户id一致
     * @return
     */
    String check(String mobile);

    /**
     * 实现注册逻辑
     *
     * @param map
     * @throws NoSuchAlgorithmException
     */
    void register(Map<String,Object> map) throws NoSuchAlgorithmException;
}
