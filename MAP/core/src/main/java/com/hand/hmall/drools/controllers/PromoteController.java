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
 * Created by shanks on 2017/3/23.
 * @desp 优惠券发放界面调用
 */
@Controller
@RequestMapping("/promote")
public class PromoteController {
    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    /**
     * 根据优惠券Id查询优惠券已发放的用户
     *
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryByCidAndUserIds" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest httpServletRequest, @RequestBody Map<String,Object> map) {
        try{
            String url = "/h/promote/queryByCidAndUserIds";
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
