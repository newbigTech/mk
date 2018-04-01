package com.hand.hmall.as.controllers;

import com.hand.hmall.as.dto.AsMaterialEntry;
import com.hand.hmall.as.service.IAsMaterialEntryService;
import com.hand.hmall.common.service.ISequenceGenerateService;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class AsMaterialEntryController extends BaseController {

    @Autowired
    private IAsMaterialEntryService asMaterialEntryService;
    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    @RequestMapping(value = "/hmall/as/material/entry/query")
    @ResponseBody
    public ResponseData query(AsMaterialEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(asMaterialEntryService.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/material/entry/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<AsMaterialEntry> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(asMaterialEntryService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/material/entry/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AsMaterialEntry> dto) {
        asMaterialEntryService.batchDelete(dto);
        return new ResponseData();
    }
    @RequestMapping(value = "/hmall/as/material/entry/getAllEntryByMaterialId")
    @ResponseBody
    public ResponseData getAllEntryByMaterialId(AsMaterialEntry dto) {
        return new ResponseData(asMaterialEntryService.getAllEntryByMaterialId(dto));
    }
    /**
     * 删除物耗单行
     *
     * @return
     */
    @RequestMapping(value = "/hmall/as/material/entry/deleteOrderEntry")
    @ResponseBody
    public String deleteServiceOrderEntry(HttpServletRequest request, @RequestBody List<AsMaterialEntry> dto) {
        if (dto.size() > 0) {
            for (int i = 0; i < dto.size(); i++) {
                if (dto.get(i).getMaterialEntryId() != null) {
                    asMaterialEntryService.deleteByPrimaryKey(dto.get(i));
                }
            }
            return "success";
        }
        return "fail";
    }

}