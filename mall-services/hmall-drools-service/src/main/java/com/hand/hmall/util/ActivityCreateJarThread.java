package com.hand.hmall.util;

import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.service.IRuleTempService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by shanks on 2017/3/27.
 */
@Component
public class ActivityCreateJarThread implements  Runnable{
    @Autowired
    private IRuleTempService ruleTempService;

    private Map<String,Object> activityMap;

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    public void setActivityMap(Map<String, Object> activityMap) {
        this.activityMap = activityMap;
    }

    @Override
    public void run() {
        ResponseData responseData=ruleTempService.releaseActivity(this.activityMap);
        if(!responseData.isSuccess()){
            logger.debug(responseData.getMsg());
        }
    }
}
