package com.hand.hmall.controller;

import com.hand.hmall.code.MessageCode;
import com.hand.hmall.dto.HmallMstCustomerCheck;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mapper.HmallMstCustomerCheckMapper;
import com.hand.hmall.service.IUserCheckCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.hand.hmall.dto.MsgCode.MSG_03_VALUE;
import static com.hand.hmall.dto.MsgCode.MSG_04_VALUE;

/**
 * @author 阳赳
 * @name:UserCheckcodecontroller
 * @Description:用户验证码校验
 * @date 2017/6/1 19:37
 */
@RestController
@RequestMapping(value = "/i/customer", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserCheckCodeController {

    @Autowired
    private IUserCheckCodeService iUserCheckCodeService;

    /**
     * 校验验证码是否合法
     * @param hmallMstCustomerCheck 校验所需信息
     * @return
     */
    @RequestMapping(value = "/checkcode", method = RequestMethod.POST)
    public ResponseData checkcode(@RequestBody HmallMstCustomerCheck hmallMstCustomerCheck)
    {
        return iUserCheckCodeService.checkCode(hmallMstCustomerCheck);
    }
}
