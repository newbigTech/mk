package com.hand.hap.rm.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.rm.dto.RmItemRelateLine;
import com.hand.hap.rm.service.IRmItemRelateLineService;
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
 * @name RmItemRelateLineController
 * @description 物料关系行表Controller
 * @date 2017/6/20
 */
@Controller
public class RmItemRelateLineController extends BaseController {

    @Autowired
    private IRmItemRelateLineService service;

    @RequestMapping(value = "/hap/rm/item/relate/line/query")
    @ResponseBody
    public ResponseData query(RmItemRelateLine dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectRelationLine(requestContext, dto, page, pageSize));
    }

    /**
     * 提交选配关系行数据
     *
     * @param request
     * @param rmItemRelateLines
     * @return
     */
    @RequestMapping(value = "/hap/rm/item/relate/line/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<RmItemRelateLine> rmItemRelateLines) {
        IRequest requestCtx = createRequestContext(request);
        try {
            service.batchSubmit(requestCtx, rmItemRelateLines);
        } catch (Exception e) {
            ResponseData rd = new ResponseData();
            rd.setSuccess(false);
            rd.setMessage(e.getMessage());
            return rd;
        }
        return new ResponseData(rmItemRelateLines);
    }

    @RequestMapping(value = "/hap/rm/item/relate/line/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<RmItemRelateLine> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 查询选配关系头
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/rm/item/relate/line/queryReleteLine")
    @ResponseBody
    public ResponseData queryReleteLine(RmItemRelateLine dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        if (dto.getRelateId() != null && !"".equals(dto.getRelateId())) {
            return new ResponseData(service.selectRelationLine(requestContext, dto, page, pageSize));
        } else {
            return new ResponseData();
        }

    }
}