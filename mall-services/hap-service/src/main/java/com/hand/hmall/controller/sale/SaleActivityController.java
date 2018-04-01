package com.hand.hmall.controller.sale;

import com.alibaba.fastjson.JSON;
import com.hand.hmall.client.sale.ISaleActivityClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
@RestController
@RequestMapping(value = "/h/sale/activity", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SaleActivityController {
    @Autowired
    private ISaleActivityClient saleActivityClient;

    @RequestMapping(value = "/query" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(@RequestBody Map<String,Object> map)
    {
        ResponseData responseData = saleActivityClient.query(map);
        System.out.println("----------hap------"+ JSON.toJSONString(responseData));
        return responseData;
    }

    @RequestMapping(value = "/submit" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String,Object>map)
    {
        return  saleActivityClient.submit(map);
    }

    @RequestMapping(value = "/startUsing" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData activity(@RequestBody List<Map<String,Object>> maps)
    {
        return saleActivityClient.activity(maps);
    }

    @RequestMapping(value = "/endUsing" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData inactive(@RequestBody List<Map<String,Object>> maps)
    {
        return saleActivityClient.inactive(maps);
    }

    @RequestMapping(value = "/detail" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id")String id)
    {
        return saleActivityClient.detail(id);
    }

    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String,String>> maps)
    {
        return saleActivityClient.delete(maps);
    }
}
