package com.hand.hap.atp.controllers;

import com.hand.hap.atp.dto.AtpPartsReserved;
import com.hand.hap.atp.service.IAtpPartsReservedService;
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
 * @author qiang.wang01@hand-china.com
 * @version 0.1
 * @name AtpPartsReservedController
 * @description 零部件可用库存占用Controller
 * @date 2017/8/7
 */

@Controller
public class AtpPartsReservedController extends BaseController {

    @Autowired
    private IAtpPartsReservedService service;

    /**
     * 查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/parts/reserved/query")
    @ResponseBody
    public ResponseData query(AtpPartsReserved dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 保存
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/atp/parts/reserved/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AtpPartsReserved> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}