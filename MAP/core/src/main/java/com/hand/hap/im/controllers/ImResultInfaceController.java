package com.hand.hap.im.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name ImResultInfaceController
 * @description 接收选配结果
 * @date 2017/5/27
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImResultInface;
import com.hand.hap.im.service.IImResultInfaceService;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.dto.ResponseData;
import com.markor.map.framework.common.interf.entities.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ImResultInfaceController extends BaseController {

    @Autowired
    private IImResultInfaceService service;

    @RequestMapping(value = "/hap/im/result/inface/query")
    @ResponseBody
    public ResponseData query(ImResultInface dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        PaginatedList<ImResultInface> list = service.queryList(requestContext, dto, page, pageSize);
        ResponseData responseData = new ResponseData(list.getRows());
        responseData.setTotal((int) list.getTotalCount());
        return responseData;
    }

    @RequestMapping(value = "/hap/im/result/inface/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ImResultInface> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/im/result/inface/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ImResultInface> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}