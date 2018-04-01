package com.hand.hmall.client.sale;

import com.hand.hmall.client.sale.impl.SaleTemplateClientImpl;
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
@FeignClient(value = "hmall-drools-service", fallback = SaleTemplateClientImpl.class)
public interface ISaleTemplateClient {


    @RequestMapping(value = "/h/sale/template/query" , method = RequestMethod.POST)
    public ResponseData query(@RequestBody Map<String, Object> datas);

    @RequestMapping(value = "/h/sale/template/submit" , method = RequestMethod.POST)
    public ResponseData submit(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/template/delete" , method = RequestMethod.POST)
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps);


    @RequestMapping(value = "/h/sale/template/detail" , method = RequestMethod.GET)
    public ResponseData detail(@RequestParam("id") String id);
}
