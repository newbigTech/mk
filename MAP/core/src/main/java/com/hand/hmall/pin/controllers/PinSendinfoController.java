package com.hand.hmall.pin.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.pin.dto.PinSendinfo;
import com.hand.hmall.pin.service.IPinSendinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class PinSendinfoController extends BaseController {

    @Autowired
    private IPinSendinfoService service;


    @RequestMapping(value = "/hmall/pin/sendinfo/query")
    @ResponseBody
    public ResponseData query(PinSendinfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/pin/sendinfo/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PinSendinfo> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/pin/sendinfo/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<PinSendinfo> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}