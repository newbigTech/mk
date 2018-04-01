package com.hand.hmall.client.impl;

import com.hand.hmall.client.UserClient;
import com.hand.hmall.dto.HmallMstCustomerCheck;
import com.hand.hmall.dto.ResponseData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @name UserClientHystrix
 * @Describe 提供与用户相关的功能操作，包括登陆、注册、查询、更新
 * @Author 唐磊
 * @Date 2017/6/12 13:53
 * @version 1.0
 */
public class UserClientHystrix implements UserClient {
    /**
     * 用户登录
     * @Author: noob
     * @Param:  * @param map
     * @Date: 2017/5/24 9:15
     */
    @Override
    public ResponseData login(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);
    }

    /**
     * 用户信息查询
     * @Author: noob
     * @Param:  * @param map
     * @Date: 2017/5/24 15:15
     */
    @Override
    public ResponseData queryUserInfo(@PathVariable("customerId") String customerId) {
        return new ResponseData(false);
    }

    /**
     * 用户注册
     * @Author: yang
     * @Param:  * @param map
     * @Date: 2017/5/24 9:15
     */

    @Override
    public ResponseData register(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);
    }
    /**
     * 用户信息更新
     * @Author: yang
     * @Param:  * @param map
     * @Date: 2017/5/24 9:15
     */
    @Override
    public ResponseData updateInfo(@RequestBody Map Info) {
        return new ResponseData(false);
    }

    /**
     * 用户手机号校验
     * @Author: yang
     * @Param:  * @param customerID
     * @Date: 2017/5/24 9:15
     */
    @Override
    public ResponseData checkMobile(@PathVariable("customerId") String customerId) {
        return new ResponseData(false);
    }

    /**
     * 手机验证码生成
     * @Author: yang
     * @Param:  * @param mobileNumber
     * @Date: 2017/5/24 9:15
     */
    @Override
    public ResponseData createCheckcode(@PathVariable("mobileNumber") String mobileNumber,@PathVariable("sendType") String sendType) {
        return new ResponseData(false);
    }

    @Override
    public ResponseData checkcode(@RequestBody HmallMstCustomerCheck hmallMstCustomerCheck) {
        return new ResponseData(false);
    }

    /**
     * 发送短信
     */

    @Override
    public ResponseData sendMSG(@RequestBody Map<String, Object> map) {
        return new ResponseData(false);
    }

}
