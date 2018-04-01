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
 * @author XinyangMei
 * @Title InstallationPay
 * @Description desp
 * @date 2017/11/24 15:36
 */
public class InstallationPay implements Callable<ResponseData> {

    private com.hand.hmall.pojo.OrderPojo OrderPojo;

    private PromoteCaculateService promoteCaculateService;

    static Logger logger = LoggerFactory.getLogger(SaleExecutionController.class);

    public InstallationPay(OrderPojo orderPojo, PromoteCaculateService promoteCaculateService) {
        this.OrderPojo = orderPojo;
        this.promoteCaculateService = promoteCaculateService;
    }

    @Override
    public ResponseData call() throws Exception {
        long startTime = System.currentTimeMillis();
        ResponseData responseData = promoteCaculateService.caculateInstallationPay(OrderPojo);
        logger.info(" 3. --计算安装费----" + (System.currentTimeMillis() - startTime));
        return responseData;
    }
}
