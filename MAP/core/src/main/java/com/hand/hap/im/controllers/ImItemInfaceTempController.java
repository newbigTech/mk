package com.hand.hap.im.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.im.dto.ImItemInfaceTemp;
import com.hand.hap.im.service.IImItemInfaceTempService;
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
public class ImItemInfaceTempController extends BaseController {

    @Autowired
    private IImItemInfaceTempService service;


    @RequestMapping(value = "/hap/im/item/inface/temp/query")
    @ResponseBody
    public ResponseData query(ImItemInfaceTemp dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/im/item/inface/temp/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ImItemInfaceTemp> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/im/item/inface/temp/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ImItemInfaceTemp> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}