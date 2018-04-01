package com.hand.hap.setup.controllers;
/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name SetupSequenceHeaderController
 * @description 流水码头controller
 * @date 2017/5/27
 */

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.external.setupservice.dto.SetupSequenceConditionDto;
import com.markor.map.external.setupservice.dto.SetupSequenceResponseDto;
import com.markor.map.external.setupservice.service.ISetupSequenceHeaderExternalService;
import com.markor.map.framework.common.interf.entities.ResponseData;
import com.markor.map.setupservice.dto.SetupSequenceHeader;
import com.markor.map.setupservice.service.ISetupSequenceHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SetupSequenceHeaderController extends BaseController {

    @Autowired
    private ISetupSequenceHeaderService service;
    @Autowired
    private ISetupSequenceHeaderExternalService iSetupSequenceHeaderExternalService;

    /**
     * 流水码头查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/setup/sequence/header/query")
    @ResponseBody
    public ResponseData query(SetupSequenceHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.getSetupSequenceHeader(page, pageSize, dto));
    }

    /**
     * 流水码提交
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/setup/sequence/header/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<SetupSequenceHeader> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    /**
     * 生成编码
     *
     * @param request
     * @param setupSequenceCondition
     * @return
     */
    @RequestMapping(value = "api/public/hap/setup/sequence/header/encode")
    @ResponseBody
    public ResponseData encode(HttpServletRequest request, @RequestBody SetupSequenceConditionDto setupSequenceCondition) {
        List<SetupSequenceResponseDto> setupSequenceResponseList = new ArrayList<>();
        SetupSequenceResponseDto setupSequenceResponseDto = iSetupSequenceHeaderExternalService.encode(setupSequenceCondition);
        setupSequenceResponseList.add(setupSequenceResponseDto);
        return new ResponseData(setupSequenceResponseList);
    }
}