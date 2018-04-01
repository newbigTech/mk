package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import com.markor.map.pointservice.dto.MstPointofService;
import com.markor.map.pointservice.service.IMstPointofServiceService;
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
public class MstPointofServiceController extends BaseController {

    @Autowired
    private IMstPointofServiceService service;


    @RequestMapping(value = "/hmall/mst/pointofservice/query")
    @ResponseBody
    public ResponseData query(MstPointofService dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectByCondition(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/pointofservice/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MstPointofService> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/pointofservice/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MstPointofService> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}