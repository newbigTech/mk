package com.hand.hmall.client;

import com.hand.hmall.client.impl.UserClientHystrix;
import com.hand.hmall.dto.HmallMstCustomerCheck;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @name UserClient
 * @Describe 调用下层服务。提供与用户相关的功能操作，包括登陆、注册、查询、更新
 * @Author 唐磊
 * @Date 2017/6/12 13:53
 * @version 1.0
 */
@FeignClient(value = "user", fallback = UserClientHystrix.class)
public interface UserClient {
    /**
     * 用户登录
     * @Author: noob
     * @Param:  * @param map
     * @Date: 2017/5/24 9:11
     */
    @RequestMapping(value = "/i/login/normal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData login(@RequestBody Map<String, Object> map);

    /**
     * 用户查询
     * @Author: noob
     * @Param:  * @param map
     * @Date: 2017/5/24 15:14
     */
    @RequestMapping(value = "/i/query/{customerId}", method = RequestMethod.GET)
    public ResponseData queryUserInfo(@PathVariable("customerId") String customerId);

    /**
     * 用户注册
     * @Author: yang
     * @Param:  * @param map
     * @Date: 2017/5/24 9:11
     */

    @RequestMapping(value = "/i/register", method = RequestMethod.POST)
    public ResponseData register(@RequestBody Map<String, Object> map);


    /**
     * 用户信息编辑
     * @Author: yang
     * @Param:  * @param map
     * @Date: 2017/5/24 9:11
     */
    @RequestMapping(value = "/i/customer/info/update", method = RequestMethod.POST)
    public ResponseData updateInfo(@RequestBody Map Info);


    /**
     * 用户手机号校验
     * @Author: yang
     * @Param:  * @param customerID
     * @Date: 2017/5/24 9:11
     */
    @RequestMapping(value = "/i/mobile/{customerId}", method = RequestMethod.GET)
    public ResponseData checkMobile(@PathVariable("customerId") String customerId);


    /** 手机验证码生成
     * @Author: yang
     * @Param:  * @param mobileNumber
     * @Date: 2017/5/24 9:11
     */
    @RequestMapping(value = "/i/customer/createcheckcode/{mobileNumber}/sendType/{sendType}", method = RequestMethod.GET)
    public  ResponseData createCheckcode(@PathVariable("mobileNumber") String mobileNumber,@PathVariable("sendType") String sendType);


    /** 手机验证码校验
     * @Author: yang
     * @Param:  * @param ma
     * @Date: 2017/5/24 9:11
     */
    @RequestMapping(value = "/i/customer/checkcode", method = RequestMethod.POST)
    public  ResponseData checkcode(@RequestBody HmallMstCustomerCheck hmallMstCustomerCheck);


    /**
     * 发送短信
     */
    @RequestMapping(value = "/i/sendMSG", method = RequestMethod.POST)
    public ResponseData sendMSG(@RequestBody Map<String, Object> map);


}
