package com.hand.hmall.client.sale;

import com.hand.hmall.client.sale.impl.SaleRangeClientImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
@FeignClient(value = "hmall-drools-service", fallback = SaleRangeClientImpl.class)
public interface ISaleRangeClient  {
    @RequestMapping(value = "/h/sale/range/condition/query" , method = RequestMethod.POST)
    public ResponseData conditionQuery(@RequestBody Map<String, Object> datas);



    @RequestMapping(value = "/h/sale/range/group/queryByType" , method = RequestMethod.GET)
    public ResponseData groupQuery(@RequestParam("type") String type);

    @RequestMapping(value = "/h/sale/range/group/submit" , method = RequestMethod.POST)
    public ResponseData groupSubmit(@RequestBody List<Map<String, Object>> maps);

    @RequestMapping(value = "/h/sale/range/group/queryByConditions" , method = RequestMethod.POST)
    public ResponseData queryByConditions(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/range/group/delete" , method = RequestMethod.POST)
    public ResponseData groupDelete(@RequestBody List<Map<String, Object>> maps);

}
