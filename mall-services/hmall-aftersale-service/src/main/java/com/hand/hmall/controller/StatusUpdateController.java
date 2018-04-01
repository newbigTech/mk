/*
 *  Copyright (C) HAND Enterprise Solutions Company Ltd.
 *  All Rights Reserved
 *
 */

package com.hand.hmall.controller;

import com.hand.hmall.dto.HmallAsStatuslog;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.servie.IStatusUpdateService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 李伟
 * @version 1.0
 * @name:StatusUpdateController
 * @Description: 售后单据状态更新
 * @date 2017/8/17 20:36
 */

@RestController
@RequestMapping(value = "/afterSale")
public class StatusUpdateController {

    @Autowired
    private IStatusUpdateService iStatusUpdateService;

    /**
     * 根据服务单id返回状态日志信息
     * @param hmallAsStatuslog
     * @return
     */
    @RequestMapping(value = "/statusLog/query",method = RequestMethod.POST)
    public ResponseData queryStatusLogs(@RequestBody HmallAsStatuslog hmallAsStatuslog)
    {
        ResponseData responseData = new ResponseData();
        //时间格式化
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取服务单id
        Long serviceId = hmallAsStatuslog.getServiceId();
        //检验服务单id是否为
        if(null == serviceId)
        {
            responseData.setMsgCode("statuslog.serviceid.null");
            responseData.setMsg("serviceId为空");
            responseData.setSuccess(false);
            responseData.setResp(null);
            return responseData;
        }
        //根据服务单id获取状态日志
        List<HmallAsStatuslog> statuslogs = iStatusUpdateService.queryByServiceId(serviceId);
        List list = new ArrayList<>();

        //校验状态日志是否为空
        if(CollectionUtils.isEmpty(statuslogs))
        {
            responseData.setMsgCode("statuslog.serviceid.notexist");
            responseData.setMsg("根据serviceId["+ serviceId +"]找不到对应的状态日志");
            responseData.setSuccess(false);
            responseData.setResp(null);
            return responseData;
        }
        else
        {
            for(HmallAsStatuslog log : statuslogs)
            {
                //封装状态日志
                Map<String, Object> mapLogs = new HashMap();
                mapLogs.put("service_id",log.getServiceId());
                mapLogs.put("status",log.getStatus() == null ? "" : log.getStatus());
                mapLogs.put("change_time",log.getChangeTime() == null ? "" : formatter.format(log.getChangeTime()));
                list.add(mapLogs);
            }
        }

        responseData.setMsgCode("1");
        responseData.setMsg("状态日志返回成功");
        responseData.setSuccess(true);
        responseData.setResp(list);
        return responseData;
    }
}
