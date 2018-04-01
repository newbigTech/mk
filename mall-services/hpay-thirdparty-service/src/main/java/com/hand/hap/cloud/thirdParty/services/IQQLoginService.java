package com.hand.hap.cloud.thirdParty.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.thirdParty.services
 * @Description
 * @date 2017/7/24
 */
public interface IQQLoginService {
    /**
     * 获取QQ登录URL
     *
     * @param request
     * @return
     */
    String getQQLoginUrl(HttpServletRequest request);

    /**
     * 获取QQ用户信息
     *
     * @param request
     * @param resp
     */
    void getUserInfoByQQ(HttpServletRequest request, HttpServletResponse resp);
}
