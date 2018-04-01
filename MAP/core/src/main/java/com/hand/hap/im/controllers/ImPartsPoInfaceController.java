package com.hand.hap.im.controllers;
/**
 * @author qiang.wang01@hand-china.com
 * @version 1.0
 * @name ImPartsPoInfaceController
 * @description 零部件Po接口表Controller
 * @date 2017/8/7
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImPartsPoInface;
import com.hand.hap.im.service.IImPartsPoInfaceService;
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
public class ImPartsPoInfaceController extends BaseController {

    @Autowired
    private IImPartsPoInfaceService service;

    /**查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/parts/po/inface/query")
    @ResponseBody
    public ResponseData query(ImPartsPoInface dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 提交
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/im/parts/po/inface/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ImPartsPoInface> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}