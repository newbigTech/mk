package com.hand.hmall.om.controllers;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.NoticeConfig;
import com.hand.hmall.om.service.INoticeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class NoticeConfigController extends BaseController {

    @Autowired
    private INoticeConfigService service;

    @RequestMapping(value = "/hmall/om/notice/queryNoticeConfigList")
    @ResponseBody
    public ResponseData queryNoticeConfigList(NoticeConfig dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.noticeConfigList(requestContext, dto, page, pageSize));
    }


    @RequestMapping(value = "/hmall/om/notice/config/query")
    @ResponseBody
    public ResponseData query(NoticeConfig dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/notice/config/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<NoticeConfig> dto, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        if(CollectionUtils.isNotEmpty(dto)){
            for(NoticeConfig noticeConfig : dto){
                if(noticeConfig.getConfigId() != null){
                    service.updateByPrimaryKey(requestCtx,noticeConfig);
                }else{
                    service.insertSelective(requestCtx,noticeConfig);
                }
            }
        }
        return new ResponseData(dto);
    }

    @RequestMapping(value = "/hmall/om/notice/config/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<NoticeConfig> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}