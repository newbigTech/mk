package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.OmMailtemplate;
import com.hand.hmall.om.service.IOmMailtemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
public class OmMailtemplateController extends BaseController {

    @Autowired
    private IOmMailtemplateService service;


    @RequestMapping(value = "/hmall/om/mailtemplate/query")
    @ResponseBody
    public ResponseData query(OmMailtemplate dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectByMailTemplate(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/mailtemplate/queryByID")
    @ResponseBody
    public ResponseData queryByID(Long id, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        OmMailtemplate omMailtemplate = new OmMailtemplate();
        omMailtemplate.setId(id);
        return new ResponseData(Arrays.asList(service.selectByPrimaryKey(requestContext, omMailtemplate)));
    }

    @RequestMapping(value = "/hmall/om/mailtemplate/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<OmMailtemplate> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/mailtemplate/saveChange")
    @ResponseBody
    public ResponseData saveChange(HttpServletRequest request, @RequestBody List<OmMailtemplate> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(Arrays.asList(service.updateByPrimaryKeySelective(requestCtx, dto.get(0))));
    }

    @RequestMapping(value = "/hmall/om/mailtemplate/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OmMailtemplate> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}