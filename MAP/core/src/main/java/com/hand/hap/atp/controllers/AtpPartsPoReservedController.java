package com.hand.hap.atp.controllers;
/**
 * @author qiang.wang01@hand-china.com
 * @version 0.1
 * @name AtpPartsPoReservedController
 * @description po零部件占用controller
 * @date 2017/8/9
 */

import com.hand.hap.atp.dto.AtpPartsPoReserved;
import com.hand.hap.atp.service.IAtpPartsPoReservedService;
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

@Controller
public class AtpPartsPoReservedController extends BaseController {

    @Autowired
    private IAtpPartsPoReservedService service;

    /**
     * 查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/parts/po/reserved/query")
    @ResponseBody
    public ResponseData query(AtpPartsPoReserved dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 提交
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/atp/parts/po/reserved/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AtpPartsPoReserved> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}