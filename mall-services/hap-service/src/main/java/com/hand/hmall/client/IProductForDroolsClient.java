package com.hand.hmall.client;


import com.hand.hmall.client.impl.ProductForDroolsClientImpl;
import com.hand.hmall.dto.ResponseData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/3/21.
 */
@FeignClient(value="product",fallback=ProductForDroolsClientImpl.class)
public interface IProductForDroolsClient {
    @RequestMapping(value = "/i/product/drools/selectAllProduct", method = RequestMethod.POST)
    public ResponseData selectAllProduct(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/i/product/drools/selectGift", method = RequestMethod.POST)
    public ResponseData selectGift(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/i/product/drools/queryByNotInAndCount", method = RequestMethod.POST)
    public ResponseData queryByNotInAndCount(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/i/product/drools/selectSkuByNotIn", method = RequestMethod.POST)
    public ResponseData selectSkuByNotIn(@RequestBody List<String> productIds);

    @RequestMapping(value = "/i/product/drools/queryByProductIds" , method = RequestMethod.POST)
    public ResponseData queryByProductIds(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "/i/product/drools/checkedProductCount" , method = RequestMethod.POST)
    public ResponseData checkedProductCount(@RequestBody List<Map<String, Object>> maps);

    @RequestMapping(value = "/i/product/drools/category/queryByCondition",method = RequestMethod.POST)
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map);
}
