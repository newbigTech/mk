/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.service.impl;

import com.hand.hmall.dto.*;
import com.hand.hmall.mapper.HmallFndGlobalVariantMapper;
import com.hand.hmall.service.IUserSendMessageService;
import com.hand.hmall.util.MD5EncryptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.*;

/**
 * @author 李伟
 * @version 1.0
 * @name:UserSendMessageServiceImpl
 * @Description:
 * @date 2017/8/1 14:41
 */
@Service("IUserSendMessageService")
public class UserSendMessageServiceImpl implements IUserSendMessageService {

    private static final Logger logger = LoggerFactory.getLogger(UserSendMessageServiceImpl.class);

    @Autowired
    private HmallFndGlobalVariantMapper hmallFndGlobalVariantMapper;

    //第三方短信平台的url
    @Value("${data.url}")
    private String url;

    @Override
    public ResponseData sendSMG(Map<String, Object> map)
    {
        ResponseData responseData = new ResponseData();
        //手机号码
        String phone = (String) map.get("phone");

        //短信内容
        String msg = (String) map.get("msg");

        //发送时间
        String sendtime = (String) map.get("sendtime");

        //将发送内容和发送时间进行UTF-8转码
        try {
            msg = URLEncoder.encode(msg,"UTF-8");
            sendtime  = URLEncoder.encode(sendtime,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("others cause,change failure");
        }

        //验证手机号码的正则表达式
        String regex = "^((13[0-9])|(14[5,6,7,9])|(15[^4,\\D])|(16[6])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";

        //手机号码去除空格
        phone = phone.replace(" ","");

        //验证手机号码的合法性
        if (StringUtils.isBlank(phone))
        {
            responseData.setMsg("手机号码为空");
            responseData.setMsgCode("ur.phone.null");
            responseData.setSuccess(false);
            return responseData;
        }
        else if(!phone.matches(regex))
        {
            responseData.setMsg("手机号码不合法");
            responseData.setMsgCode("ur.phone.illegal");
            responseData.setSuccess(false);
            return responseData;
        }

        //验证短信内容
        if(StringUtils.isBlank(msg))
        {
            responseData.setMsg("短信内容为空");
            responseData.setMsgCode("ur.msg.null");
            responseData.setSuccess(false);
            return responseData;
        }

        //获取用户账号
        HmallFndGlobalVariant globalVariantUser = hmallFndGlobalVariantMapper.getUserOrPwdByCode("renxinl-user");
        //用户账号
        String user = null;
        if(globalVariantUser == null)
        {
            responseData.setMsg("任信了账号为空");
            responseData.setMsgCode("ur.user.null");
            responseData.setSuccess(false);
            return responseData;
        }
        else
        {
            //得到用户账号
            user = globalVariantUser.getValue();
        }

        //获取用户密码
        HmallFndGlobalVariant globalVariantPwd = hmallFndGlobalVariantMapper.getUserOrPwdByCode("renxinl-password");
        //用户密码
        String pwd = null;
        if(globalVariantPwd == null)
        {
            responseData.setMsg("任信了密码为空");
            responseData.setMsgCode("ur.pwd.null");
            responseData.setSuccess(false);
            return responseData;
        }
        else
        {
            //得到用户密码
            pwd = globalVariantPwd.getValue();

            //对密码进行MD5加密
            pwd = MD5EncryptionUtil.getMd5(pwd);
        }

        map.put("user",user);
        map.put("pwd",pwd);
        map.put("phone",phone);
        map.put("msg",msg);
        map.put("sendtime",sendtime);
        System.out.println("user:" +user+",pwd:"+pwd+",phone:"+phone+",msg:"+msg+",sendtime:"+sendtime);

        try{
            RestTemplate restTemplate = new RestTemplate();

            //短信接口的URL
            logger.info("===url===:" + url);
            //调用短信的接口参数
            String params = "?user="+user+"&pwd="+pwd+"&phone="+phone+"&msg="+msg+"&sendtime="+sendtime;
            logger.info("---params---:" + params);
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(map,null);
            //发送请求,获取第三方短信的返回值
            HmallMessageResponseData response = restTemplate.exchange(url+params, HttpMethod.GET,entity,HmallMessageResponseData.class).getBody();

            //请求状态码，取值0000（成功）
            String code = response.getCode();
            //短信唯一标识符
            String msgid = response.getMsgid();
            //状态码相应的解释
            String codemsg = response.getCodemsg();


            //将第三方短信平台返回是信息反馈给前台
            Map<String, Object> mapMsgid = new HashMap();
            List list = new ArrayList<>();
            mapMsgid.put("msgid",msgid);
            list.add(mapMsgid);

            //发送成功
            if("0000".equals(code))
            {
                responseData.setMsg("短信发送成功");
                responseData.setMsgCode("1");
                responseData.setResp(list);
                responseData.setSuccess(true);
            }
            else
            {
                //发送失败
                responseData.setMsg(codemsg);
                responseData.setMsgCode("2");
                responseData.setResp(list);
                responseData.setSuccess(false);
            }
            return  responseData;
        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e);
        }

        responseData.setMsgCode("6001");  //发送失败
        responseData.setMsg("参数异常,发送失败");
        responseData.setSuccess(false);
        return responseData;
    }
}
