package com.hand.hmall.util;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.hmall.service.IDroolsInitDateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author XinyangMei
 * @Title InitDataUtil
 * @Description desp
 * @date 2017/9/27 22:33
 */
@Component
@Order(value = 1)
public class InitDataUtil implements CommandLineRunner {
    @Autowired
    private IDroolsInitDateService droolsInitDateService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(String... strings) throws Exception {

        if (true) {
            logger.info("\n-------------start init redis for drools---------------");
            logger.info("\n-------------clear old data in redis for drools");
            droolsInitDateService.clearOldData();
            logger.info("\n-------------initSelectCondition for drools");
            droolsInitDateService.initSelectConditionDao();
            logger.info("\n-------------initSelectAction for drools");
            droolsInitDateService.initSelectActionDao();
            logger.info("\n-------------initGroup for drools");
            droolsInitDateService.initGroup();
            logger.info("\n-------------initModelData for drools");
            droolsInitDateService.initModelData();
            logger.info("\n-------------finish init redis for drools---------------");
        }


    }
}
