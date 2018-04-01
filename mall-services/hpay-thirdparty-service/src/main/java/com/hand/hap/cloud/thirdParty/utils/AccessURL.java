package com.hand.hap.cloud.thirdParty.utils;

import com.qq.connect.QQConnectException;
import com.qq.connect.utils.RandomStatusGenerator;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.thirdParty.utils
 * @Description
 * @date 2017/8/26
 */
public class AccessURL {

    /**
     * 获取微信登陆URL
     *
     * @param request
     * @return
     * @throws QQConnectException
     */
    public static String getWXURL(ServletRequest request) throws QQConnectException {

        String state = RandomStatusGenerator.getUniqueState();
        String redirect_uri = "";
        try {
            String redirect_URI = PropertiesUtil.getWechatValue("redirect_URI").trim();
            String zestId = request.getParameter("zestId");
            String preUrl = request.getParameter("preUrl");
            redirect_uri = URLEncoder.encode(
                    (redirect_URI
                            + "?preUrl=" + preUrl
                            + "&zestId=" + zestId), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ((HttpServletRequest) request).getSession().setAttribute("wx_connect_state", state);

        String scope = PropertiesUtil.getWechatValue("scope");

        return PropertiesUtil.getWechatValue("authorizeURL").trim()
                + "?appid=" + PropertiesUtil.getWechatValue("app_ID").trim()
                + "&redirect_uri=" + redirect_uri
                + "&response_type=" + PropertiesUtil.getWechatValue("response_type").trim()
                + "&state=" + state
                + "&scope=" + scope
                + "#wechat_redirect";
    }

    /**
     * 获取QQ登陆URL
     *
     * @param request
     * @return
     */
    public static String getQQUrl(ServletRequest request) {
        String state = RandomStatusGenerator.getUniqueState();
        String redirect_uri = "";
        try {
            String redirect_URI = PropertiesUtil.getQQValue("redirect_URI").trim();
            String zestId = request.getParameter("zestId");
            String preUrl = request.getParameter("preUrl");
            redirect_uri = URLEncoder.encode(
                    (redirect_URI
                            + "?preUrl=" + preUrl
                            + "&zestId=" + zestId), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ((HttpServletRequest) request).getSession().setAttribute("qq_connect_state", state);

        String scope = PropertiesUtil.getQQValue("scope");

        return PropertiesUtil.getQQValue("authorizeURL").trim()
                + "?response_type=" + PropertiesUtil.getQQValue("response_type").trim()
                + "&client_id=" + PropertiesUtil.getQQValue("app_ID").trim()
                + "&redirect_uri=" + redirect_uri
                + "&state=" + state
                + "&scope=" + scope;
    }
}
