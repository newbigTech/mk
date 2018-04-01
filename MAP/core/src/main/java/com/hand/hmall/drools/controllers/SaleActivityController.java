package com.hand.hmall.drools.controllers;

import com.alibaba.fastjson.JSON;
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
 * Created by shanks on 2017/1/5.
 * @desp 调用微服务 对促销活动进行管理 包括增删改 启用 停用
 */
@Controller
@RequestMapping("/drools/sale/activity")
public class SaleActivityController extends BaseController {

    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    /**
     * 分页查询所有促销活动
     *
     * @param map                可选字段 activityId（规则ID编码） 生效时间（StartDate） endDate(失效时间)
     *                           activityName（规则名称）
     *                           status（状态）
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public ResponseData query(@RequestBody Map<String, Object> map, HttpServletRequest httpServletRequest) {
        try {
            String url = "/h/sale/activity/query";
            System.out.println("-------url---" + baseUri + modelUri + url);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            System.out.println("---resp--" + JSON.toJSONString(responseData));
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    /**
     * 删除已过期的促销活动
     *
     * @param maps
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps, HttpServletRequest httpServletRequest) {
        try {
            String url = "/h/sale/activity/delete";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("删除数据错误");
        return responseData;
    }

    /**
     * 创建促销活动
     * @param map
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String, Object> map, HttpServletRequest httpServletRequest) {
        try {
            IRequest iRequest = createRequestContext(httpServletRequest);
            map.put("userId", iRequest.getUserId());
            String url = "/h/sale/activity/submit";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("保存数据错误");
        return responseData;
    }

    /**
     * 启用促销
     * @param maps
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/startUsing")
    @ResponseBody
    public ResponseData activity(@RequestBody List<Map<String, Object>> maps, HttpServletRequest httpServletRequest) {
        try {
            String url = "/h/sale/activity/startUsing";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("保存数据错误");
        return responseData;
    }

    /**
     * 停用促销
     * @param maps
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/endUsing")
    @ResponseBody
    public ResponseData inactive(@RequestBody List<Map<String, Object>> maps, HttpServletRequest httpServletRequest) {
        try {
            String url = "/h/sale/activity/endUsing";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("保存数据错误");
        return responseData;
    }

    /**
     * 查看促销详细信息
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id") String id, HttpServletRequest httpServletRequest) {
        try {
            String url = "/h/sale/activity/detail?id=" + id;
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.GET, null, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }
}
