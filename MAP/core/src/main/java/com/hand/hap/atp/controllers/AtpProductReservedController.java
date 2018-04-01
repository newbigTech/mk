package com.hand.hap.atp.controllers;

import com.hand.hap.atp.dto.AtpProductReserved;
import com.hand.hap.atp.service.IAtpProductReservedService;
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
 * @name AtpProductReservedController
 * @description 成品库存预留controller
 * @date 2017/7/02
 */
@Controller
public class AtpProductReservedController extends BaseController {

    @Autowired
    private IAtpProductReservedService service;


    @RequestMapping(value = "/hap/atp/product/reserved/query")
    @ResponseBody
    public ResponseData query(AtpProductReserved dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/atp/product/reserved/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AtpProductReserved> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/atp/product/reserved/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AtpProductReserved> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}