package com.hand.hmall.util;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.controller.SaleExecutionController;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.pojo.OrderPojo;
import com.hand.hmall.service.PromoteCaculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

/**
 * 计算运费的线程
 */
public class CaculateFeight implements Callable<ResponseData> {

    private com.hand.hmall.pojo.OrderPojo OrderPojo;

    private PromoteCaculateService promoteCaculateService;

    static Logger logger = LoggerFactory.getLogger(SaleExecutionController.class);

    public CaculateFeight(OrderPojo orderPojo,PromoteCaculateService promoteCaculateService) {
        this.promoteCaculateService = promoteCaculateService;
        this.OrderPojo = orderPojo;
    }

    @Override
    public ResponseData call() throws Exception {
        logger.info("计算运费");
        return promoteCaculateService.caculateExpressAndLogisticsFreight(OrderPojo);
    }
}
