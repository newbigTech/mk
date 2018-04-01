package com.hand.hmall.controller;

import com.hand.hmall.client.IProductForDroolsClient;
import com.hand.hmall.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by shanks on 2017/3/21.
 */
@RestController
@RequestMapping(value = "/h/product/drools", produces = {MediaType.APPLICATION_JSON_VALUE})
public class HapProductForDroolsController {

    @Autowired
    private IProductForDroolsClient productForDroolsClient;
    @RequestMapping(value = "/selectAllProduct",method = RequestMethod.POST)
    public ResponseData selectAllSku(@RequestBody Map<String,Object> map){

        return productForDroolsClient.selectAllProduct(map);
    }
    @RequestMapping(value = "/selectGift", method = RequestMethod.POST)
    public ResponseData selectGift(@RequestBody Map<String,Object> map){
        return productForDroolsClient.selectGift(map);
    }

    @RequestMapping(value = "/queryByNotInAndCount", method = RequestMethod.POST)
    public ResponseData queryByNotInAndCount(@RequestBody Map<String,Object> map){
        return productForDroolsClient.queryByNotInAndCount(map);
    }

    @RequestMapping(value = "/selectSkuByNotIn",method = RequestMethod.POST)
    public ResponseData selectSkuByNotIn(@RequestBody List<String> productIds){

        return productForDroolsClient.selectSkuByNotIn(productIds);
    }

    @RequestMapping(value = "/queryByProductIds" , method = RequestMethod.POST)
    public ResponseData queryByProductIds(@RequestBody Map<String,Object> map)
    {
        return productForDroolsClient.queryByProductIds(map);
    }

    @RequestMapping(value = "/checkedProductCount" , method = RequestMethod.POST)
    public ResponseData checkedProductCount(@RequestBody List<Map<String,Object>> maps)
    {
        return productForDroolsClient.checkedProductCount(maps);
    }

    @RequestMapping(value = "/category/queryByCondition",method = RequestMethod.POST)
    public ResponseData queryByCondition(@RequestBody Map<String, Object> map)
    {
        return productForDroolsClient.queryByCondition(map);
    }
}
