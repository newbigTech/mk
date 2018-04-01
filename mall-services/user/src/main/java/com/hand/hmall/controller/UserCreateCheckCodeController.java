package com.hand.hmall.controller;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IUserCreateCheckCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 阳赳
 * @name:UserCreateCheckcodecontroller
 * @Description:验证码的生成
 * @date 2017/6/1 16:11
 */
@RestController
@RequestMapping(value = "/i/customer", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserCreateCheckCodeController {

    @Autowired
    private IUserCreateCheckCodeService iUserCreateCheckCodeService;

    /**
     * 生成验证码入口
     * @param mobileNumber 手机号码
     * @param sendType 业务类型
     * @return
     */
    @RequestMapping(value = "createcheckcode/{mobileNumber}/sendType/{sendType}", method = RequestMethod.GET)
    public ResponseData createCheckCode(@PathVariable("mobileNumber") String mobileNumber,@PathVariable("sendType") String sendType)
    {
        //屏蔽发短信功能
//       return iUserCreateCheckCodeService.insertOracle(mobileNumber, sendType);

        //发送短信
       return iUserCreateCheckCodeService.sendMessage(mobileNumber, sendType);
    }
}
