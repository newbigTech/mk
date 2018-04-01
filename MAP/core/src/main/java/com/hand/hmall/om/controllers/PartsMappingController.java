package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.PartsMapping;
import com.hand.hmall.om.service.IPartsMappingService;
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
 * @name PartsMappingController
 * @description 配件controller
 * @date 2017年5月26日10:52:23
 */
@Controller
public class PartsMappingController extends BaseController {

    @Autowired
    private IPartsMappingService service;


    @RequestMapping(value = "/hmall/om/parts/mapping/query")
    @ResponseBody
    public ResponseData query(PartsMapping dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/parts/mapping/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<PartsMapping> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/parts/mapping/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<PartsMapping> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hmall/om/parts/mapping/queryParts")
    @ResponseBody
    public ResponseData queryInfo(PartsMapping dto) {
        return new ResponseData(service.selectParts(dto));
    }
}