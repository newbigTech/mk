package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.Catalogversion;
import com.hand.hmall.mst.service.ICatalogversionService;
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
 * @name CatalogversionController
 * @description 目录版本
 * @date 2017年5月26日10:52:23
 */
@Controller
public class CatalogversionController extends BaseController {

    @Autowired
    private ICatalogversionService service;


    @RequestMapping(value = "/hmall/mst/catalogversion/query")
    @ResponseBody
    public ResponseData query(Catalogversion dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/catalogversion/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Catalogversion> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/catalogversion/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Catalogversion> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 联合catalog表查询出组合版本目录，如美克美家-online
     */
    @RequestMapping(value = "/hmall/mst/catalogversion/selectCatalog")
    @ResponseBody
    public ResponseData selectCatalog(HttpServletRequest request, Catalogversion dto) {
        return new ResponseData(service.selectCatalogVersion());
    }

}