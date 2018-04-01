package com.hand.hap.cloud.thirdParty.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hand.hap.cloud.thirdParty.entities.OutBoundLogs;
import com.hand.hap.cloud.thirdParty.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.thirdParty.mq.MessageSender;
import com.hand.hap.cloud.thirdParty.services.IPostZmallService;
import com.hand.hap.cloud.thirdParty.services.IWechatLoginService;
import com.hand.hap.cloud.thirdParty.utils.AccessURL;
import com.hand.hap.cloud.thirdParty.utils.LogsUtils;
import com.hand.hap.cloud.thirdParty.utils.PropertiesUtil;
import com.qq.connect.QQConnectException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.thirdParty.services.impl
 * @Description
 * @date 2017/7/24
 */
@Service
public class WechatLoginServiceImpl implements IWechatLoginService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSender messageSender;
    @Autowired
    private IPostZmallService postZmallService;

    /**
     * 获取微信登陆URL
     *
     * @param request
     * @return
     * @throws QQConnectException
     */
    @Override
    public String getWXLoginUrl(HttpServletRequest request) throws QQConnectException {
        return AccessURL.getWXURL(request);
    }

    /**
     * 获取微信用户信息
     *
     * @param request
     * @param resp
     */
    @Override
    public void getUserInfoByWechat(HttpServletRequest request, HttpServletResponse resp) {

        logger.info("===============AfterLoginRedirectServlet============");

        Map<String, Object> parms = new HashMap<>();

        OutBoundLogs logs = new OutBoundLogs();
        logs.setTargetSystem("ZMALL");
        logs.setRequestTime(new Date());
        logs.setUsage("WECHAT-LOGIN");
        try {
            logger.info("===============getRequestURL============" + request.getRequestURL());
            String code = request.getParameter("code");
            logger.info(">>>>>>>>>>>>>>>>>>getParameterCode>>>>>" + code);
            String returnState = request.getParameter("state");
            logger.info(">>>>>>>>>>>>>>>>>>getParameterState>>>>>" + returnState);
            String state = (String) ((HttpServletRequest) request).getSession().getAttribute("wx_connect_state");
            logger.info(">>>>>>>>>>>>>>>>>>getSessionState>>>>>" + state);
//            if (null == returnState || null == state || !returnState.equals(state)) {
            if (null == returnState) {
                logs.setMessage("未能发送请求，非正常请求:returnState为空");
                logs.setSuccess(false);
                logger.info("+++++++++++++非正常请求++++++++++++");
            } else {
                if (StringUtils.isEmpty(code)) {
                    logs.setMessage("未能发送请求，禁止授权:code为空");
                    logs.setSuccess(false);
                    logger.info("+++++++++++++禁止授权++++++++++++");
                } else {
                    String resultAccessToken = excuteRequest(getAccessTokenURL(code));
                    logger.info("-------------getAccessTokenURL" + getAccessTokenURL(code));

                    logger.info("+++++++++++++resultAccessToken++++++++++++" + resultAccessToken);

                    JSONObject jsonAccessToken = JSON.parseObject(resultAccessToken);
                    if (StringUtils.isNotEmpty(jsonAccessToken.getString("errcode"))) {
                        logs.setMessage("未能发送请求，无法获得AccessToken，errmsg:" + jsonAccessToken.get("errmsg"));
                        logs.setSuccess(false);
                        logger.info("+++++++++++++无法获得AccessToken-" + jsonAccessToken.get("errmsg") + "++++++++++++");

                    } else {
                        String access_token = jsonAccessToken.getString("access_token");
                        String openid = jsonAccessToken.getString("openid");

                        String resultUserInfo = excuteRequest(getUserInfo(access_token, openid));

                        logger.info("+++++++++++++resultUserInfo++++++++++++" + resultUserInfo);

                        JSONObject jsonUserInfo = JSON.parseObject(resultUserInfo);

                        if (StringUtils.isNotEmpty(jsonUserInfo.getString("errcode"))) {
                            logs.setMessage("未能发送请求，无法获得用户信息，errmsg:" + jsonUserInfo.get("errmsg"));
                            logs.setSuccess(false);
                            logger.info("+++++++++++++无法获得用户信息-" + jsonUserInfo.get("errmsg") + "++++++++++++");

                        } else {
                            String nickName = jsonUserInfo.getString("nickname");
                            String sex = jsonUserInfo.getString("sex");

                            String userHeadImg = jsonUserInfo.getString("headimgurl");
                            String identityCode = jsonUserInfo.getString("unionid");

                            String platform = "2";
                            String zestId = request.getParameter("zestId");
                            String preUrl = request.getParameter("preUrl");

                            parms.put("identityCode", identityCode);
                            parms.put("platform", platform);
                            parms.put("zestId", zestId);
                            parms.put("preUrl", preUrl);
                            parms.put("nickName", nickName);
                            parms.put("sex", sex);
                            parms.put("userHeadImg", userHeadImg);
                            logger.info(">>>>>>>>>>>requestParms<<<<<<<<<" + parms.toString());
                            String content = JSON.toJSONString(parms);
                            logs.setRequestTime(new Date());
                            logs.setRequestAddr(PropertiesUtil.getZmallValue("return_url"));
                            logs.setRequestMethod("POST");
                            logs.setTargetSystem("ZMALL");
                            logs.setRequestBody(content);
                            LogsUtils.setLogs(logs);
                            JSONObject respMapObj = postZmallService.postToZmall(PropertiesUtil.getZmallValue("return_url"), content, null);
                            if (StringUtils.isNotEmpty(respMapObj.getString("resp"))) {
                                String redirectUrl = respMapObj.getString("resp");
                                logger.info(">>>>>>>>>>>sendRedirect<<<<<<<<<" + redirectUrl);
                                resp.sendRedirect(redirectUrl);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logs.setSuccess(false);
            Date responseTime = new Date();
            logs.setResponseTime(responseTime);
            logs.setExceptionStack(e.getStackTrace());
            messageSender.sendMsg(logs);
            LogsUtils.clear();
            logger.info("--------wxConnectException----");
            logger.info(e.getMessage());
        }
    }

    /**
     * access_token url
     *
     * @param code
     * @return
     */
    private String getAccessTokenURL(String code) {
        logger.info("--------&secret=----" + PropertiesUtil.getWechatValue("app_KEY").trim());
        return PropertiesUtil.getWechatValue("accessTokenURL").trim() + "?appid=" + PropertiesUtil.getWechatValue("app_ID").trim() + "&secret="
                + PropertiesUtil.getWechatValue("app_KEY").trim() + "&code=" + code + "&grant_type=authorization_code";
    }

    /**
     * 用户信息URL
     *
     * @param access_token
     * @param openid
     * @return
     */
    private String getUserInfo(String access_token, String openid) {
        return PropertiesUtil.getWechatValue("getUserInfoURL").trim() + "?access_token=" + access_token + "&openid=" + openid;
    }

    /**
     * 发送请求
     *
     * @param url
     * @return
     */
    private String excuteRequest(String url) {
        BufferedReader in = null;

        String content = null;
        ThirdPartyApiLogs log = new ThirdPartyApiLogs();
        log.setTarget("QQ");
        log.setRequestMethod("LOGIN");
        log.setRequestBody(url);
        Date requestTime = new Date();
        log.setRequestTime(requestTime);
        log.setRequestBody(url);
        try {
            // 定义HttpClient
            HttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            logger.info("-------------response" + response.toString());
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            content = sb.toString();
            log.setResponseBody(content);
            Date responseTime = new Date();
            log.setResponseTime(responseTime);
            log.setDuring(responseTime.getTime() - requestTime.getTime());
            log.setSuccess(true);
        } catch (Exception e) {
            log.setSuccess(false);
            log.setExceptionStack(e.getStackTrace());
            e.printStackTrace();
            logger.info("+++++++++++++excuteRequest error++++++++++++");
        } finally {
            messageSender.sendMsg(log);
            if (in != null) {
                try {
                    in.close();// 最后要关闭BufferedReader
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }
}
