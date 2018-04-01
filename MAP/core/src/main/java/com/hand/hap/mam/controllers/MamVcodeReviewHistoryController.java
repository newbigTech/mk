package com.hand.hap.mam.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamVcodeReviewHistory;
import com.hand.hap.mam.service.IMamVcodeReviewHistoryService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MamVcodeReviewHistoryController extends BaseController {

    @Autowired
    private IMamVcodeReviewHistoryService service;


    @RequestMapping(value = "/hap/mam/vcode/review/history/query")
    @ResponseBody
    public ResponseData query(MamVcodeReviewHistory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/mam/vcode/review/history/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<MamVcodeReviewHistory> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/mam/vcode/review/history/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MamVcodeReviewHistory> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hap/mam/vcode/review/history/addHistory")
    @ResponseBody
    public ResponseData addHistory(HttpServletRequest request, @RequestBody MamVcodeReviewHistory history) {
        IRequest requestCtx = createRequestContext(request);
        service.insertSelective(requestCtx, history);
        return new ResponseData();
    }
}