package com.hand.hap.mam.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.BomQuantityChangeDto;
import com.hand.hap.mam.service.IMamBomQuantityService;
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
import java.util.Map;

@Controller
public class MamBomQuantityController extends BaseController {

    @Autowired
    private IMamBomQuantityService service;


    /**
     * 通过物料编码查询对应的V码行
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/hap/mam/bom/quantity/change/query")
    @ResponseBody
    public ResponseData query(BomQuantityChangeDto dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryBomQuantity(requestContext, dto, page, pageSize));
    }

    /**
     * 通过类型的不同更新组件用量
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mam/bom/quantity/change/update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody List<BomQuantityChangeDto> dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        // retrun map
        Map<String, Object> rd = service.updateBomQuantity(requestContext,dto);
        return rd;
    }

    /**
     * 属性头删除CHECK入口
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mam/bom/quantity/change/push")
    @ResponseBody
    public Map<String, Object> push(HttpServletRequest request, @RequestBody List<BomQuantityChangeDto> dto) {
        IRequest requestCtx = createRequestContext(request);

        Map<String, Object> rtn = service.pushBomQuantity(requestCtx,dto);

        return rtn;
    }

}