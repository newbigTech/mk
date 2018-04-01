package com.hand.hmall.controller;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IUserMobileCheakService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 阳赳
 * @name:UserMobileCheakController
 * @Description:手机号码校验
 * @date 2017/6/1 9:46
 */
@RestController
@RequestMapping(value = "/i", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserMobileCheckController
{
    @Resource(name = "IUserMobileCheakService")
    private IUserMobileCheakService IUserMobileCheakService;

    /**
     * 校验用户手机号码入口
     *
     * @param customerId 用户id
     * @return
     */
    @RequestMapping(value = "/mobile/{customerId}", method = RequestMethod.GET)
    public ResponseData checkMobile(@PathVariable("customerId") String customerId)
    {
        return IUserMobileCheakService.check(customerId);
    }
}
