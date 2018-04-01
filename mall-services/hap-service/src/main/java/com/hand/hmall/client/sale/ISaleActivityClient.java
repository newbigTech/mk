package com.hand.hmall.client.sale;

import com.hand.hmall.client.sale.impl.SaleActivityClientImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/2/15.
 */
@FeignClient(value = "hmall-drools-service", fallback = SaleActivityClientImpl.class)
public interface ISaleActivityClient {

    @RequestMapping(value = "/h/sale/activity/query" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData query(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/activity/submit" , method = RequestMethod.POST)
    public ResponseData submit(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/activity/startUsing" , method = RequestMethod.POST)
    public ResponseData activity(@RequestBody List<Map<String, Object>> maps);

    @RequestMapping(value = "/h/sale/activity/endUsing" , method = RequestMethod.POST)
    public ResponseData inactive(@RequestBody List<Map<String, Object>> maps);

    @RequestMapping(value = "/h/sale/activity/detail" , method = RequestMethod.GET)
    public ResponseData detail(@RequestParam("id") String id);

    @RequestMapping(value = "/h/sale/activity/delete" , method = RequestMethod.POST)
    public ResponseData delete(@RequestBody List<Map<String, String>> maps);
}
