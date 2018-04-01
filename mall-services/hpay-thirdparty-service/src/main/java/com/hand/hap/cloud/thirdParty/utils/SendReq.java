package com.hand.hap.cloud.thirdParty.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author jiaxin.jiang
 * @version 0.1
 * @name RestClient
 * @description <pre>
 */
public class SendReq {
    public static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static Logger logger = LoggerFactory.getLogger(SendReq.class);

    /**
     * 发送get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String httpGet(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string(); // 返回的是string 类型，json的mapper可以直接处理
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static String httpPost(String url, String content) throws IOException {
        RequestBody requestBody = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 发送请求
     *
     * @param url
     * @param content
     * @param params
     * @return
     * @throws IOException
     */
    public static String post(String url,String content, Map<String, String> params) throws IOException {
        logger.info("-------url---------" + makeUrlWithParameters(url, params));
        return httpPost(makeUrlWithParameters(url, params), content);
    }

    /**
     * 发送请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static JSONObject get(String url, Map<String, String> params) throws IOException {
        String response = httpGet(makeUrlWithParameters(url, params));
        JSONObject responseObj = JSON.parseObject(response);
        return responseObj;
    }

    /**
     * 将参数拼接到Url地址中
     *
     * @param url
     * @param parameters
     * @return
     */
    private static String makeUrlWithParameters(String url, Map<String, String> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            for (String key : parameters.keySet()) {
                String value = parameters.get(key);
                try {
                    if (value != null)
                        url += ((url.contains("?") ? "&" : "?") + key + "=" + URLEncoder.encode(value, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return url;
    }

    /**
     * 添加请求Header信息
     *
     * @param builder
     * @param headers
     */
    private void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (value != null)
                    builder.addHeader(key, headers.get(key));
            }
        }
    }
}
