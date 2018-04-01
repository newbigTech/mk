package com.hand.hmall.controller.sale;

import com.alibaba.fastjson.JSON;
import com.hand.hmall.client.sale.ISaleTemplateClient;
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
@RequestMapping(value = "/h/sale/template", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SaleTemplateController {

    @Autowired
    private ISaleTemplateClient saleTemplateClient;

    @RequestMapping(value = "/query" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(@RequestBody Map<String,Object> datas)
    {
        return saleTemplateClient.query(datas);
    }

    @RequestMapping(value = "/submit" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData submit(@RequestBody Map<String,Object> map)
    {
        return saleTemplateClient.submit(map);
    }

    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody List<Map<String,Object>> maps)
    {
        return saleTemplateClient.delete(maps);
    }

    @RequestMapping(value = "/detail" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData detail(@RequestParam("id")String id)
    {
        return saleTemplateClient.detail(id);
    }
}
