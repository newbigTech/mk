package com.hand.hmall.as.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.Svsales;
import com.hand.hmall.as.dto.SvsalesEntry;
import com.hand.hmall.as.service.ISvsalesEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhangzilong
 * @version 0.1
 * @name ServiceorderEntryController
 * @date 2017/7/19
 */
@Controller
public class SvsalesEntryController extends BaseController {

    @Autowired
    private ISvsalesEntryService service;


    /**
     * 查询售后单关联的售后单行
     *
     * @param svsalesId
     * @return
     */
    @RequestMapping(value = "/hmall/as/order/svsalesEntry/querySvsalesEntriesInfo")
    @ResponseBody
    public ResponseData queryServiceOrderInfo(@RequestParam(name = "svsalesId") Long svsalesId, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        Svsales dto = new Svsales();
        dto.setAsSvsalesId(svsalesId);
        return new ResponseData(service.querySvsalesEntriesInfo(requestContext, dto, page, pageSize));
    }


    @RequestMapping(value = "/hmall/as/svsalesEntry/query")
    @ResponseBody
    public ResponseData query(SvsalesEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/svsalesEntry/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<SvsalesEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/svsalesEntry/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<SvsalesEntry> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

}