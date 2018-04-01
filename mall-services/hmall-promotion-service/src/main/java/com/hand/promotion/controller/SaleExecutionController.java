package com.hand.promotion.controller;

import com.hand.dto.ResponseData;
import com.hand.promotion.pojo.order.OrderPojo;
import com.hand.promotion.service.IExecutionService;
import com.hand.promotion.util.BeanMapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Describe 订单促销执行入口
 * @Author noob
 * @Date 2017/6/28 16:22
 */
@RestController
@RequestMapping(value = "/sale/execution", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SaleExecutionController {

    @Autowired
    private IExecutionService executionService;
//    @Autowired
//    private ISaleExecutionService saleExecutionService;

    static Logger logger = LoggerFactory.getLogger(SaleExecutionController.class);

    /**
     * @param orderMap 前台促销执行参数
     * @return
     */
    @RequestMapping(value = "/promote", method = RequestMethod.POST)
    public ResponseData promoteOrder(@RequestBody Map<String, Object> orderMap) {

        long currentTime = System.currentTimeMillis();
        OrderPojo orderPojo = BeanMapExchange.mapToObject(orderMap, OrderPojo.class);
        ResponseData responseData = executionService.promoteOrder(orderPojo);
        logger.info("############促销执行耗时:{}", System.currentTimeMillis() - currentTime);
//        saleExecutionService.promoteOrder(orderMap);
        return responseData;
    }


    /**
     * 查询商品对应的促销活动
     *
     * @param productMap
     * @return
     */
    @RequestMapping(value = "/getActivity", method = RequestMethod.POST)
    public ResponseData getActivity(@RequestBody Map productMap) {
        return executionService.getActivity(productMap);
    }
}
