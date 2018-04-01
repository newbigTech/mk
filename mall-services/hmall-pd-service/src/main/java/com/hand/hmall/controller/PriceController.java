package com.hand.hmall.controller;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.model.Pricerow;
import com.hand.hmall.pojo.PriceRequest;
import com.hand.hmall.pojo.PriceRequestData;
import com.hand.hmall.pojo.PriceResponse;
import com.hand.hmall.service.IPriceCalculateCXFService;
import com.hand.hmall.service.IPricerowService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name PriceController
 * @description PriceController
 * @date 2017/7/5 10:35
 */
@RestController
@RequestMapping("/i/price")
public class PriceController {

    public static final Logger logger = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private IPriceCalculateCXFService iPriceCalculateCXFService;

    @Autowired
    private IPricerowService pricerowService;

    /**
     * 价格计算
     *
     * @param priceRequest 价格计算请求对象
     * @return ResponseData
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public PriceResponse calculatePrice(@RequestBody PriceRequest priceRequest) {
        return iPriceCalculateCXFService.calculatePrice(priceRequest);
    }

    @PostMapping(value = "/getGiftPrice")
    @ResponseBody
    public ResponseData getGiftPrice(@RequestParam Long productId) {
        ResponseData responseData = new ResponseData();
        List<Pricerow> pricerows = pricerowService.selectGiftPricerows(productId, "1", "1");
        if (CollectionUtils.isEmpty(pricerows)) {
            responseData.setSuccess(false);
            responseData.setMsg(productId + "对应的价格行不存在");
            return responseData;
        }
        return new ResponseData(pricerows);
    }


    /**
     * 采购价格计算
     *
     * @param priceRequestDataList 价格计算请求
     * @return PriceResponse
     */
    @RequestMapping(value = "/calculateOrderPrice", method = RequestMethod.POST)
    public PriceResponse calculateOrderPrice(@RequestBody List<PriceRequestData> priceRequestDataList) {
        return iPriceCalculateCXFService.calculateOrderPrice(priceRequestDataList);
    }

    /**
     * 销售价格计算
     *
     * @param priceRequestDataList 价格计算请求
     * @return PriceResponse
     */
    @RequestMapping(value = "/calculateSalePrice", method = RequestMethod.POST)
    public PriceResponse calculateSalePrice(@RequestBody List<PriceRequestData> priceRequestDataList) {
        return iPriceCalculateCXFService.calculateSalePrice(priceRequestDataList);
    }
}
