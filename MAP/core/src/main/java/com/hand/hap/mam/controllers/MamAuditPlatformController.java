package com.hand.hap.mam.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamAuditPlatform;
import com.hand.hap.mam.service.IMamAuditPlatformService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MamAuditPlatformController extends BaseController {

    @Autowired
    private IMamAuditPlatformService service;


    @RequestMapping(value = "/hap/mam/audit/platform/query")
    @ResponseBody
    public ResponseData query(MamAuditPlatform dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryAll(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/mam/audit/platform/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MamAuditPlatform> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        for(MamAuditPlatform mapf:dto){
            if(mapf.getHeaderId()== null){
                mapf.set__status("add");
            }
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/mam/audit/platform/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MamAuditPlatform> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}