package com.hand.promotion.client;

import com.hand.promotion.client.impl.StockClientServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by hand on 2017/3/24.
 * 该类以弃用
 */
@FeignClient(value = "stock",fallback = StockClientServiceImpl.class)
public interface IStockClientService {
    @RequestMapping(value = "/o/stock/queryByList",method = RequestMethod.POST)
    public List<Map<String, Object>> queryByList(@RequestBody List<Map<String, Object>> condition);

    @RequestMapping(value = "/o/stock/query",method = RequestMethod.POST)
    public Map<String, ?> query(@RequestBody Map<String, Object> condition);
}
