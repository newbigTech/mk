package com.hand.hap.im.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name ImInterfaceHistoryController
 * @description 存储接口调用历史记录信息
 * @date 2017/06/07
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImInterfaceHistory;
import com.hand.hap.im.service.IImInterfaceHistoryService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ImInterfaceHistoryController extends BaseController {

    @Autowired
    private IImInterfaceHistoryService service;


    /**
     * 接口调用记录自带查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/interface/history/query")
    @ResponseBody
    public ResponseData query(ImInterfaceHistory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 接口调用记录查询
     * @author yanjie.zhang@hand-china.com
     * @date 2017/07/11
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/interface/history/queryHis")
    @ResponseBody
    public ResponseData queryHis(ImInterfaceHistory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryHis(requestContext, dto, page, pageSize));
    }
}