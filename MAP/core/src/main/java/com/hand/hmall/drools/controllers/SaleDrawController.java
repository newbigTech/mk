package com.hand.hmall.drools.controllers;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.util.SortList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by cw on 2017/2/28.
 * 用于抽奖页面调用 暂不启用
 */

@RestController
@RequestMapping("/sale/draw")
public class SaleDrawController {

    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    private String modelUri = "hap-service/h/sale/draw/";

    @RequestMapping(value = "/query" , method = RequestMethod.POST)
    public ResponseData query(@RequestBody Map<String, Object> map) {
            String url = baseUri + modelUri + "query";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseData.class).getBody();
            responseData.setResp(SortList.addNumber((List<Map<String, ?>>)responseData.getResp()));
            return responseData;
    }

    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps) {
            String url = baseUri + modelUri + "delete";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps,null);
            ResponseData responseData = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
    }

    @RequestMapping(value = "/submit" , method = RequestMethod.POST)
    public ResponseData submit(@RequestBody Map<String, Object>map) {
            String url = baseUri + modelUri + "submit";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
            ResponseData responseData = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
    }

    @RequestMapping(value = "/startUsing" , method = RequestMethod.POST)
    public ResponseData active(@RequestBody List<Map<String, Object>> maps) {
            String url =  baseUri + modelUri + "startUsing";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps, null);
            ResponseData responseData = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
    }

    @RequestMapping(value = "/endUsing" , method = RequestMethod.POST)
    public ResponseData inactive(@RequestBody List<Map<String, Object>> maps) {
            String url = baseUri + modelUri + "endUsing";
            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(maps, null);
            ResponseData responseData = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseData detail(@RequestParam("id") String id) {
            String url = baseUri + modelUri + "detail?id=" + id;
            ResponseData responseData = restTemplate.exchange(url, HttpMethod.GET, null, ResponseData.class).getBody();
            return responseData;
    }

    @RequestMapping(value = "/awardPro" , method = RequestMethod.POST)
    public ResponseData awardPro(@RequestBody Map<String, Object> map) {
        String url = baseUri + modelUri + "awardPro";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, null);
        ResponseData responseData = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseData.class).getBody();
        return responseData;
    }
}
