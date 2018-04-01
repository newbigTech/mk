package com.hand.hmall.mst.controllers;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.mst.dto.PriceRequestData;
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
import java.util.List;

/**
 * Created by qinzhipeng on 2017/9/6.
 */
@Controller
@RequestMapping("/hmall/price")
public class PriceController {
    private RestTemplate restTemplate = new RestTemplate();
    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    @RequestMapping(value = "/calculateSalePrice", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData calculateSalePrice(HttpServletRequest httpServletRequest, @RequestBody List<PriceRequestData> priceRequestDataList) {
        try {
            String url = "/h/price/calculateSalePrice";
            HttpEntity<List<PriceRequestData>> entity = new HttpEntity<>(priceRequestDataList, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

    @RequestMapping(value = "/calculateOrderPrice", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData calculateOrderPrice(HttpServletRequest httpServletRequest, @RequestBody List<PriceRequestData> priceRequestDataList) {
        try {
            String url = "/h/price/calculateOrderPrice";
            HttpEntity<List<PriceRequestData>> entity = new HttpEntity<>(priceRequestDataList, null);
            ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, ResponseData.class).getBody();
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
