package com.hand.hap.rm.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.rm.dto.RmItemHistory;
import com.hand.hap.rm.service.IRmItemHistoryService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name RmItemHistoryController
 * @description 访问平台历史记录CRUD类
 * @date 2017/05/24
 */
@Controller
public class RmItemHistoryController extends BaseController {

    @Autowired
    private IRmItemHistoryService service;


    @RequestMapping(value = "/hap/rm/item/history/query")
    @ResponseBody
    public ResponseData query(RmItemHistory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectHistory(dto, requestContext, page, pageSize));
    }

    @RequestMapping(value = "/hap/rm/item/history/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<RmItemHistory> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/rm/item/history/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<RmItemHistory> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/hap/rm/item/history/insertHistory")
    @ResponseBody
    public ResponseData insertHistory(HttpServletRequest request, @RequestBody RmItemHistory rmItemHistory) throws Exception {
        IRequest requestCtx = createRequestContext(request);
        String ip = request.getRemoteAddr().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        Date date = sdf.parse(time);
        rmItemHistory.setUserId(requestCtx.getUserId());
        rmItemHistory.setIp(ip);
        rmItemHistory.setQurtyDate(date);
        service.insertSelective(requestCtx, rmItemHistory);
        return new ResponseData();
    }


    /**
     * 查询平台历史访问记录
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */


    @RequestMapping(value = "/hap/rm/item/history/selectHistory")
    @ResponseBody
    public ResponseData selectHistory(RmItemHistory dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectHistory(dto, requestContext, page, pageSize));
    }
}