package com.hand.hmall.util;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IRuleTempService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by shanks on 2017/3/27.
 */
@Component
public class CouponCreateJarThread implements Runnable{
    @Autowired
    private IRuleTempService ruleTempService;
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    private String id="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void run() {

        ResponseData responseData=ruleTempService.releaseCoupon(this.id);
        if(!responseData.isSuccess()){
            logger.debug(responseData.getMsg());
        }

    }
}
