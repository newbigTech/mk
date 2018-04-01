package com.hand.hmall.client.sale;

import com.hand.hmall.client.sale.impl.SaleDrawClientImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by cw on 2017/2/28.
 */

@FeignClient(value = "hmall-drools-service", fallback = SaleDrawClientImpl.class)
public interface ISaleDrawClient {

    @RequestMapping(value = "/h/sale/draw/query", method = RequestMethod.POST)
    public ResponseData query(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/draw/submit", method = RequestMethod.POST)
    public ResponseData submit(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/draw/startUsing", method = RequestMethod.POST)
    public ResponseData active(@RequestBody List<Map<String, Object>> maps);

    @RequestMapping(value = "/h/sale/draw/endUsing", method = RequestMethod.POST)
    public ResponseData inactive(@RequestBody List<Map<String, Object>> maps);

    @RequestMapping(value = "/h/sale/draw/detail", method = RequestMethod.GET)
    public ResponseData detail(@RequestParam("id") String id);

    @RequestMapping(value = "/h/sale/draw/delete", method = RequestMethod.POST)
    public ResponseData delete(@RequestBody List<Map<String, Object>> maps);

    @RequestMapping(value = "/h/sale/draw/awardPro", method = RequestMethod.POST)
    public ResponseData awardPro(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/draw/submitAwardPro", method = RequestMethod.POST)
    public ResponseData submitAwardPro(@RequestBody List<Map<String, Object>> list);

    @RequestMapping(value = "/h/sale/draw/startUsingAwardPro", method = RequestMethod.POST)
    public ResponseData startUsingAwardPro(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/draw/endUsingAwardPro", method = RequestMethod.POST)
    public ResponseData endUsingAwardPro(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/h/sale/draw/getAward", method = RequestMethod.POST)
    public ResponseData getAward(@RequestBody Map<String, Object> map);
}
