package com.hand.hmall.controller;

import com.hand.hmall.client.UserClient;
import com.hand.hmall.dto.HmallMstCustomerCheck;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * @name UserController
 * @Describe 提供与用户相关的功能操作，包括登陆、注册、查询、更新
 * @Author 唐磊
 * @Date 2017/6/12 13:53
 * @version 1.0
 */

@RestController
public class UserController {

    @Autowired
    private UserClient userClient;
    /**
     * 用户登录
     * @Author: noob
     * @Param:  * @param map
     * @Date: 2017/5/24 9:11
     */
    @RequestMapping(value = "/login/normal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData login(@RequestBody Map<String, Object> map) {
        return userClient.login(map);
    }

    /**
     * 用户查询
     * @Author: noob
     * @Param:  * @param customerId
     * @Date: 2017/5/24 15:01
     */
    @GetMapping("/query")
    public ResponseData query(@RequestParam String customerId) {
        return userClient.queryUserInfo(customerId);
    }
    /**
     * 用户注册
     * @Author: yang
     * @Param:  * @param map
     * @Date: 2017/5/24 15:01
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseData register(@RequestBody Map<String, Object> map) {
        return userClient.register(map);
    }
    /**
     * 用户信息编辑
     * @Author: yang
     * @Param:  * @param map
     * @Date: 2017/5/24 17:01
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseData update(@RequestBody Map Info) {
        return userClient.updateInfo(Info);
    }

    /**
     * 用户手机号校验
     * @Author: yang
     * @Param:  * @param customerID
     * @Date: 2017/5/24 16:01
     */
    @RequestMapping(value = "/mobile",method = RequestMethod.GET)
    public ResponseData checkMobile(@RequestParam String customerId) {
       return userClient.checkMobile(customerId);
    }
    /**
     * 手机验证码生成
     * @Author: yang
     * @Param:  * @param customerID
     * @Date: 2017/5/24 15:01
     */
    @RequestMapping(value = "/createCheckcode",method =RequestMethod.GET)
    public  ResponseData createCheckcode(@RequestParam String mobileNumber,@RequestParam String sendType){
        return  userClient.createCheckcode(mobileNumber,sendType);
    }

    /** 手机验证码校验
     * @Author: yang
     * @Param:  * @param hmallMstCustomerCheck
     * @Date: 2017/5/24 9:11
     */
    @RequestMapping(value = "/checkcode", method = RequestMethod.POST)
    public ResponseData checkcode(@RequestBody HmallMstCustomerCheck hmallMstCustomerCheck) {
        return userClient.checkcode(hmallMstCustomerCheck);
    }

    /**
     * 前台发送短信
     */
    @RequestMapping(value = "/sendMSG", method = RequestMethod.POST)
    public ResponseData sendMSG(@RequestBody Map<String, Object> map) {
        return userClient.sendMSG(map);
    }


}

