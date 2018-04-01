package com.hand.hmall.as.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.ExchangeEntry;
import com.hand.hmall.as.service.IExchangeEntryService;
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
public class ExchangeEntryController extends BaseController {

    @Autowired
    private IExchangeEntryService service;


    @RequestMapping(value = "/hmall/as/exchange/entry/query")
    @ResponseBody
    public ResponseData query(ExchangeEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/exchange/entry/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ExchangeEntry> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/exchange/entry/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ExchangeEntry> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}