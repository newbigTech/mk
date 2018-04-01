package com.hand.promotion.job;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author XinyangMei
 * @Title TransDataJob
 * @Description 转换数据job
 * @date 2017/12/11 16:30
 */
@Component
@Order(2)
public class TransDataJob extends BaseJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doExecute() {
        logger.info("----------transData-------------{}","jj");
    }

}
