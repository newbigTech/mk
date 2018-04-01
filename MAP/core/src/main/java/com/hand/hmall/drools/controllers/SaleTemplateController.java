package com.hand.hmall.drools.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/1/4.
 * @desp 促销模板界面调用 用于模板界面增删改操作
 */
@Controller
@RequestMapping("/sale/template")

public class SaleTemplateController extends BaseController{
    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;
//    private String modelUri = "hmall-drools-service/h/sale/template";

    /**
     * 查询所有可用模板
     *
     * @param httpServletRequest
     * @param datas
     * @return
     */
    @RequestMapping(value = "/query" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(HttpServletRequest httpServletRequest, @RequestBody Map<String,Object> datas) {

        try{
            String url = "/h/sale/template/query";
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(datas,null);
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


    /**
     * 新建模板
     * @param map
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/submit" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String,Object> map, HttpServletRequest httpServletRequest) {

        try{
            IRequest iRequest=createRequestContext(httpServletRequest);
            map.put("userId",iRequest.getUserId());
            String url = "/h/sale/template/submit";
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(map,null);
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.POST,entity,ResponseData.class).getBody();
            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("插入数据错误");
        return responseData;
    }


    /**
     * 批量删除模板
     * @param maps
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String,Object>> maps, HttpServletRequest httpServletRequest)
    {
        try{
            String url = "/h/sale/template/delete";

            HttpEntity<List<Map<String,Object>>> entity = new HttpEntity<>(maps,null);

            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.POST,entity,ResponseData.class).getBody();

            return  responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("删除数据失败");
        return responseData;
    }


    /**
     * 查询模板详细信息
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/detail" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id")String id, HttpServletRequest httpServletRequest)
    {
        try{
            String url = "/h/sale/template/detail?id="+id;
            ResponseData responseData=restTemplate.exchange(baseUri+modelUri+url, HttpMethod.GET,null,ResponseData.class).getBody();
            return responseData;
        }catch (Exception e){
            System.err.println(e);
        }
        ResponseData responseData=new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("获取数据失败");
        return  responseData;
    }



}
