package com.hand.hmall.om.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.OmAbnormaltype;
import com.hand.hmall.om.service.IOmAbnormaltypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class OmAbnormaltypeController extends BaseController {

    @Autowired
    private IOmAbnormaltypeService service;


    @RequestMapping(value = "/hmall/om/abnormaltype/query")
    @ResponseBody
    public ResponseData query(OmAbnormaltype dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/abnormaltype/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<OmAbnormaltype> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/abnormaltype/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OmAbnormaltype> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}