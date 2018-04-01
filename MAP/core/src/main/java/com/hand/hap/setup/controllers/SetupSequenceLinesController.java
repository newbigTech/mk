package com.hand.hap.setup.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name SetupSequenceLinesController
 * @description 流水码行controller
 * @date 2017/5/27
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import com.markor.map.setupservice.dto.SetupSequenceLines;
import com.markor.map.setupservice.service.ISetupSequenceLinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SetupSequenceLinesController extends BaseController {

    @Autowired
    private ISetupSequenceLinesService service;

    /**
     * 流水码行查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/setup/sequence/lines/query")
    @ResponseBody
    public ResponseData query(SetupSequenceLines dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getSetupSequenceLine(page, pageSize, dto));
    }

    /**
     * 流水码行提交
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/setup/sequence/lines/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<SetupSequenceLines> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}