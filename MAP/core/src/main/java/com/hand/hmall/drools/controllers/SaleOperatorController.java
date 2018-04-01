package com.hand.hmall.drools.controllers;

import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by shanks on 2017/1/19.
 * @desp 用于查询优惠券 促销活动 的操作人信息
 */

@Controller
@RequestMapping("/sale/operator")
public class SaleOperatorController {
    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    /**
     * 用于查询优惠券 促销活动编码ID 的操作人信息
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryByBaseId" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData productQuery(HttpServletRequest httpServletRequest, @RequestBody Map<String,Object> map) {
        try{
            String url = "/h/sale/operator/queryByBaseId";
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(map,null);
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.POST,entity,ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }
}
