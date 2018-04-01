package com.hand.hap.atp.controllers;

import com.hand.hap.atp.dto.AtpOccupyHistory;
import com.hand.hap.atp.service.IAtpOccupyHistoryService;
import com.hand.hap.core.IRequest;
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

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name AtpOccupyHistoryController
 * @description atp占用历史CRUD类
 * @date 2017/7/20
 */
@Controller
public class AtpOccupyHistoryController extends BaseController {

    @Autowired
    private IAtpOccupyHistoryService service;


    @RequestMapping(value = "/hap/atp/occupy/history/query")
    @ResponseBody
    public ResponseData query(AtpOccupyHistory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hap/atp/occupy/history/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AtpOccupyHistory> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }
}