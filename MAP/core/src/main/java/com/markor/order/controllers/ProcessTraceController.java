package com.markor.order.controllers;


import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import com.markor.map.oms.base.process.service.IProcessEngineServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ProcessTraceController
 * @description 订单流程跟踪Controller
 * @date 2018/2/27
 */
@Controller
public class ProcessTraceController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ProcessTraceController.class);

    @Autowired
    private IProcessEngineServiceProvider service;

    //查询流程跟踪记录
    @RequestMapping(value = "/mk/order/queryProcessTrace")
    @ResponseBody
    public ResponseData queryProcessTrace(@RequestParam("orderCode") String orderCode, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryProcessTrace(orderCode, page, pageSize));
    }

    //查询事件入口点跟踪记录
    @RequestMapping(value = "/mk/order/queryEntryPointLogEvent")
    @ResponseBody
    public ResponseData queryEntryPointLogEvent(@RequestParam("orderCode") String orderCode, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryEntryPointLog(orderCode, "EVENT", page, pageSize));
    }

    //查询操作入口点跟踪记录
    @RequestMapping(value = "/mk/order/queryEntryPointLogOperate")
    @ResponseBody
    public ResponseData queryEntryPointLogOperate(@RequestParam("orderCode") String orderCode, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryEntryPointLog(orderCode, "ACTION", page, pageSize));
    }
}