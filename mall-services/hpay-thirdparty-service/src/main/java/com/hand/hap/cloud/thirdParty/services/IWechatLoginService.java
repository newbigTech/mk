package com.hand.hap.cloud.thirdParty.services;

import com.qq.connect.QQConnectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.thirdParty.services
 * @Description
 * @date 2017/7/24
 */
public interface IWechatLoginService {

    /**
     * 获取微信登陆URL
     *
     * @param request
     * @return
     * @throws QQConnectException
     */
    String getWXLoginUrl(HttpServletRequest request) throws QQConnectException;

    /**
     * 获取微信用户信息
     *
     * @param request
     * @param resp
     */
    void getUserInfoByWechat(HttpServletRequest request, HttpServletResponse resp);
}
