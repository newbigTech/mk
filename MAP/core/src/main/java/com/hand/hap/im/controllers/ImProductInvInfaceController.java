package com.hand.hap.im.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name ImProductInvInfaceController
 * @description  成品可用controller
 * @date 2017/6/21
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImProductInvInface;
import com.hand.hap.im.service.IImProductInvInfaceService;
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
public class ImProductInvInfaceController extends BaseController {

    @Autowired
    private IImProductInvInfaceService service;

    /**
     * 成品可用查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/product/inv/inface/query")
    @ResponseBody
    public ResponseData query(ImProductInvInface dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 成品可用提交
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/im/product/inv/inface/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ImProductInvInface> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}