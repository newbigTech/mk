package com.hand.hap.atp.controllers;

import com.hand.hap.atp.dto.AtpMoReservedInfo;
import com.hand.hap.atp.service.IAtpMoReservedInfoService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name AtpMoReservedInfoController
 * @description 成品零部件在制占用Controller
 * @date 2017/7/03
 */
@Controller
public class AtpMoReservedInfoController extends BaseController {

    @Autowired
    private IAtpMoReservedInfoService service;


    /**
     * 成品零部件在制占用查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/mo/reserved/info/query")
    @ResponseBody
    public ResponseData query(AtpMoReservedInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }
}