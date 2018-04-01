package com.hand.hap.im.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImAtpBomSource;
import com.hand.hap.im.service.IImAtpBomSourceService;
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
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name ImAtpBomSourceController
 * @description atpBom卷算controller
 * @date 2017/6/21
 */

@Controller
public class ImAtpBomSourceController extends BaseController {

    @Autowired
    private IImAtpBomSourceService service;

    /**
     * bom source查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/atp/bom/source/query")
    @ResponseBody
    public ResponseData query(ImAtpBomSource dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * bom source提交
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/im/atp/bom/source/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ImAtpBomSource> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}