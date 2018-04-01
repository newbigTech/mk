package com.hand.hap.atp.controllers;

import com.hand.hap.atp.dto.AtpCapacityReserved;
import com.hand.hap.atp.service.IAtpCapacityReservedService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name AtpCapacityReservedController
 * @description 产能占用Controller
 * @date 2017/07/04
 */
@Controller
public class AtpCapacityReservedController extends BaseController {

    @Autowired
    private IAtpCapacityReservedService service;


    @RequestMapping(value = "/hap/atp/capacity/reserved/query")
    @ResponseBody
    public ResponseData query(AtpCapacityReserved dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/atp/capacity/reserved/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AtpCapacityReserved> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/atp/capacity/reserved/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AtpCapacityReserved> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}