package com.hand.hap.cloud.thirdParty.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hand.hap.cloud.thirdParty.entities.OutBoundLogs;
import com.hand.hap.cloud.thirdParty.entities.ThirdPartyApiLogs;
import com.hand.hap.cloud.thirdParty.mq.MessageSender;
import com.hand.hap.cloud.thirdParty.services.IPostZmallService;
import com.hand.hap.cloud.thirdParty.services.IQQLoginService;
import com.hand.hap.cloud.thirdParty.utils.AccessURL;
import com.hand.hap.cloud.thirdParty.utils.GetQQUserInfo;
import com.hand.hap.cloud.thirdParty.utils.LogsUtils;
import com.hand.hap.cloud.thirdParty.utils.PropertiesUtil;
import com.qq.connect.api.OpenID;
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
public class QQLoginServiceImpl implements IQQLoginService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private IPostZmallService postZmallService;

    /**
     * 获取QQ登录URL
     *
     * @param request
     * @return
     */
    public String getQQLoginUrl(HttpServletRequest request) {
        return AccessURL.getQQUrl(request);
    }

    /**
     * 获取QQ用户信息
     *
     * @param request
     * @param resp
     */
    @Override
    public void getUserInfoByQQ(HttpServletRequest request, HttpServletResponse resp) {

        logger.info("===============AfterLoginRedirectServlet============");

        Map<String, Object> parms = new HashMap<>();
        OutBoundLogs logs = new OutBoundLogs();
        logs.setRequestTime(new Date());
        logs.setTargetSystem("ZMALL");
        logs.setUsage("QQ-LOGIN");
        try {
            String access_token;
            String openid;
            logger.info("===============getRequestURL============" + request.getRequestURL());
            String code = request.getParameter("code");
            logger.info(">>>>>>>>>>>>>>>>>>getParameterCode>>>>>" + code);
            String returnState = request.getParameter("state");
            logger.info(">>>>>>>>>>>>>>>>>>getParameterState>>>>>" + returnState);
            String state = (String) ((HttpServletRequest) request).getSession().getAttribute("qq_connect_state");
            logger.info(">>>>>>>>>>>>>>>>>>getSessionState>>>>>" + state);
//            if (null == returnState || null == state || !returnState.equals(state)) {

            if (null == returnState) {
                logs.setMessage("未能发送请求，非正常请求:returnState为空");
                logs.setSuccess(false);
                messageSender.sendMsg(logs);
                logger.info("+++++++++++++非正常请求++++++++++++");
            } else {
                if (StringUtils.isEmpty(code)) {
                    logs.setMessage("未能发送请求，禁止授权:code为空");
                    logs.setSuccess(false);
                    messageSender.sendMsg(logs);
                    logger.info("+++++++++++++禁止授权++++++++++++");
                } else {
                    String resultAccessToken = excuteRequest(getAccessTokenURL(code));
                    logger.info("-------------getAccessTokenURL" + getAccessTokenURL(code));
                    logger.info("+++++++++++++resultAccessToken++++++++++++" + resultAccessToken);
                    JSONObject jsonAccessToken = toJson(resultAccessToken);
                    if (StringUtils.isEmpty(jsonAccessToken.getString("access_token"))) {
                        logs.setMessage("未能发送请求，无法获得AccessToken");
                        logs.setSuccess(false);
                        messageSender.sendMsg(logs);
                        logger.info("+++++++++++++无法获得AccessToken++++++++++++");
                    } else {
                        access_token = jsonAccessToken.getString("access_token");
                        logger.info("+++++++++++++access_token++++++++++++" + access_token);
                        OpenID openIDObj = new OpenID(access_token);
                        openid = openIDObj.getUserOpenID();
                        logger.info("+++++++++++++openid++++++++++++" + openid);
//                        UserInfo qzoneUserInfo = new UserInfo(access_token, openid);
//                        UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                        GetQQUserInfo getQQUserInfo = new GetQQUserInfo();
                        com.qq.connect.utils.json.JSONObject qqInfo = getQQUserInfo.getUserInfo(openid, access_token);
//                        logger.info("+++++++++++++qzoneUserInfo++++++++++++" + qzoneUserInfo);

                        if ("".equals(qqInfo.getString("ret"))) {
                            logs.setMessage("未能发送请求，无法获得用户信息，msg:" + qqInfo.getString("msg"));
                            logs.setSuccess(false);
                            messageSender.sendMsg(logs);
                            logger.info("+++++++++++++无法获得用户信息-" + qqInfo.getString("msg") + "++++++++++++");
                        } else {
                            logger.info("+++++++++++++用户信息++++++++++++" + qqInfo);
                            String nickName = qqInfo.getString("nickname");
                            String sex = qqInfo.getString("gender");
                            String userHeadImg = qqInfo.getString("figureurl_qq_1");
                            String platform = "1";
                            String zestId = request.getParameter("zestId");
                            String preUrl = request.getParameter("preUrl");

                            parms.put("identityCode", openid);
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
            logs.setMessage("发送请求期间发生异常");
            logs.setExceptionStack(e.getStackTrace());
            messageSender.sendMsg(logs);
            LogsUtils.clear();
            logger.info("--------qqConnectException----");
            logger.info(e.getMessage());
        }
    }

    /**
     * AccessTokenURL
     *
     * @param code code
     * @return
     */
    private String getAccessTokenURL(String code) {
        return PropertiesUtil.getQQValue("accessTokenURL").trim() + "?client_id=" + PropertiesUtil.getQQValue("app_ID").trim() + "&client_secret="
                + PropertiesUtil.getQQValue("app_KEY").trim() + "&code=" + code + "&grant_type=authorization_code&redirect_uri=" + PropertiesUtil.getQQValue("redirect_URI");
    }

    /**
     * 发送请求
     *
     * @param url url
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
            logger.info("-------------response" + response);
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
            logger.error(this.getClass().getName(), e);
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

    /**
     * 处理返回数据
     *
     * @param text
     * @return
     */
    private JSONObject toJson(String text) {
        try {
            JSONObject jsonObject = JSON.parseObject(text);
            return jsonObject;
        } catch (Exception e) {
            if (text.contains("&")) {
                String strArray[] = text.split("&");
                JSONObject jsonObject = new JSONObject();
                for (String s : strArray) {
                    String sArray[] = s.split("=");
                    jsonObject.put(sArray[0], sArray[1]);
                }
                logger.info("-----------accessToken Json-----------" + jsonObject);

                return jsonObject;
            } else {
                String[] sa = text.split("[()]");
                logger.info("-----------[()]toJson---------------" + sa[1].trim());
                JSONObject jsonObject = JSON.parseObject(sa[1].trim());
                logger.info("-----------toJson callback------" + jsonObject);
                return jsonObject;
            }
        }
    }
}
