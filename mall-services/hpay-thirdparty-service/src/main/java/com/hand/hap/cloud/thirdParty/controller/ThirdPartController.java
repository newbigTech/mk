package com.hand.hap.cloud.thirdParty.controller;

import com.hand.hap.cloud.thirdParty.services.IQQLoginService;
import com.hand.hap.cloud.thirdParty.services.IWechatLoginService;
import com.qq.connect.QQConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.thirdParty.controller
 * @Description
 * @date 2017/7/21
 */
@RestController
@RequestMapping(value = "/v1")
public class ThirdPartController {

    @Autowired
    private IQQLoginService qqLoginService;

    @Autowired
    private IWechatLoginService wechatLoginService;

    /**
     * 获取微信登陆Url
     *
     * @param request
     * @return
     * @throws QQConnectException
     */
    @RequestMapping(value = "/thirdPartyWXUrl", method = RequestMethod.GET)
    public String getWXLoginUrl(HttpServletRequest request) throws QQConnectException {
        return wechatLoginService.getWXLoginUrl(request);
    }

    /**
     * 获取QQ登录Url
     *
     * @param request
     * @return
     * @throws QQConnectException
     */
    @RequestMapping(value = "/thirdPartyQQUrl", method = RequestMethod.GET)
    public String getQQLoginUrl(HttpServletRequest request) throws QQConnectException {
        return qqLoginService.getQQLoginUrl(request);
    }

    /**
     * QQ登录回调
     *
     * @param request
     * @param resp
     * @throws IOException
     */
    @RequestMapping(value = "/thirdPartyQQ", method = RequestMethod.GET)
    public void thirdPartyQQ(HttpServletRequest request, HttpServletResponse resp) throws  IOException {
        qqLoginService.getUserInfoByQQ(request, resp);
    }

    /**
     * 微信登陆回调
     *
     * @param request
     * @param resp
     * @throws IOException
     */
    @RequestMapping(value = "/thirdPartyWechat", method = RequestMethod.GET)
    public void thirdPartyWechat(HttpServletRequest request, HttpServletResponse resp) throws  IOException {
        wechatLoginService.getUserInfoByWechat(request, resp);
    }
}
