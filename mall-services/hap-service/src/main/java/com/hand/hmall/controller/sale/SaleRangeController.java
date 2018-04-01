package com.hand.hmall.controller.sale;


import com.hand.hmall.client.sale.ISaleRangeClient;
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
@RequestMapping(value = "/h/sale/range", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SaleRangeController {

    @Autowired
    private ISaleRangeClient saleRangeClient;

    @RequestMapping(value = "/condition/query" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData conditionQuery(@RequestBody Map<String,Object> datas) {
        return saleRangeClient.conditionQuery(datas);
    }


    @RequestMapping(value = "/group/queryByType" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseData groupQuery(@RequestParam("type")String type)
    {
        return saleRangeClient.groupQuery(type);
    }

    @RequestMapping(value = "/group/queryByConditions" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData queryByConditions(@RequestBody Map<String,Object> map)
    {
        return saleRangeClient.queryByConditions(map);
    }

    @RequestMapping(value = "/group/submit" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData group(@RequestBody List<Map<String,Object>> maps)
    {
        return saleRangeClient.groupSubmit(maps);
    }

    @RequestMapping(value = "/group/delete" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseData groupDelete(@RequestBody List<Map<String,Object>> maps)
    {
        return saleRangeClient.groupDelete(maps);
    }
}
