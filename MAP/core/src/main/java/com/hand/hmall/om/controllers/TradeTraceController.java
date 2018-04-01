package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.TradeTrace;
import com.hand.hmall.om.service.ITradeTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name TradeTraceController
 * @description 物流跟踪表 Controller类
 * @date 2017/8/11
 */
@Controller
public class TradeTraceController extends BaseController {

    @Autowired
    private ITradeTraceService service;

    @RequestMapping(value = "/hmall/om/trade/trace/getTradeTraceInfo")
    @ResponseBody
    public ResponseData getTradeTraceInfo(TradeTrace dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getTradeTraceInfo(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/trade/trace/getTradeTraceInfoByDelivery")
    @ResponseBody
    public ResponseData getTradeTraceInfoByDelivery(TradeTrace dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getTradeTraceInfoByDelivery(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/trade/trace/query")
    @ResponseBody
    public ResponseData query(TradeTrace dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/trade/trace/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<TradeTrace> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/trade/trace/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<TradeTrace> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}