package com.hand.hmall.log.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name LogManagerController
 * @description 日志
 * @date 2017年7月10日16:53:45
 */
@Controller
public class LogManagerController extends BaseController {

    @Autowired
    private ILogManagerService service;

    /**
     *错误日志查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/log/manager/query")
    @ResponseBody
    public ResponseData query(LogManager dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    /**
     * 错误日志修改
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/log/manager/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<LogManager> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    /**
     * 错误日志删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/log/manager/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<LogManager> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 错误日志查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/log/manager/queryAll")
    @ResponseBody
    public ResponseData queryAll(LogManager dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                 @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryAll(requestContext, dto, page, pageSize));
    }
}