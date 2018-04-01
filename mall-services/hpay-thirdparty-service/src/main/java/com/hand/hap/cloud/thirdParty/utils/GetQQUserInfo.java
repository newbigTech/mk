package com.hand.hap.cloud.thirdParty.utils;

import com.qq.connect.QQConnect;
import com.qq.connect.QQConnectException;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.http.PostParameter;
import com.qq.connect.utils.json.JSONObject;

/**
 * @author jiaxin.jiang
 * @Title com.hand.hap.cloud.thirdParty.utils
 * @Description
 * @date 2017/8/30
 */
public class GetQQUserInfo extends QQConnect {
    public JSONObject getUserInfo(String openid, String access_token) throws QQConnectException {
        return this.client.get(QQConnectConfig.getValue("getUserInfoURL"), new PostParameter[]{new PostParameter("openid", openid), new PostParameter("oauth_consumer_key", QQConnectConfig.getValue("app_ID")), new PostParameter("access_token", access_token), new PostParameter("format", "json")}).asJSONObject();
    }
}
