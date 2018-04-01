package com.hand.hap.atp.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name AtpCapacityInfoController
 * @description atp产能controller
 * @date 2017/6/19
 */

import com.hand.hap.atp.dto.AtpCapacityInfo;
import com.hand.hap.atp.service.IAtpCapacityInfoService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AtpCapacityInfoController extends BaseController {

    @Autowired
    private IAtpCapacityInfoService service;

    /*
     * 产能查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/capacity/info/query")
    @ResponseBody
    public ResponseData query(AtpCapacityInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }
}