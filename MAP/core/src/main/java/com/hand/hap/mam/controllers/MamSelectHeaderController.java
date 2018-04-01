package com.hand.hap.mam.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamSelectHeader;
import com.hand.hap.mam.service.IMamSelectHeaderService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjie.zhang@hand-china.com
 * @version 0.1
 * @name MamSelectHeaderController
 * @description 模拟选配头CRUD的controller类
 * @date 2017/7/22
 */
@Controller
public class MamSelectHeaderController extends BaseController {

    @Autowired
    private IMamSelectHeaderService service;


    /**
     * 选配头查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/select/header/query")
    @ResponseBody
    public ResponseData query(MamSelectHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectHeaderData(requestContext, dto, page, pageSize));
    }


    /**
     * 选配值选完插入
     *
     * @param request
     * @param mamSelectHeader
     * @return
     */
    @RequestMapping(value = "/hap/mam/select/header/confirmChoose")
    @ResponseBody
    public ResponseData confirmChoose(HttpServletRequest request, @RequestBody MamSelectHeader mamSelectHeader) {
        IRequest requestCtx = createRequestContext(request);
        try {
            mamSelectHeader = service.confirmChoose(requestCtx, mamSelectHeader);
        } catch (Exception e) {
            ResponseData rd = new ResponseData();
            rd.setSuccess(false);
            rd.setMessage(e.getMessage());
            return rd;
        }

        List<MamSelectHeader> mamSelectHeaders = new ArrayList<>();
        mamSelectHeaders.add(mamSelectHeader);
        return new ResponseData(mamSelectHeaders);
    }


    /**
     * 确定生成,完成选配
     *
     * @param request
     * @param mamSelectHeader
     * @return
     */
    @RequestMapping(value = "/hap/mam/select/header/confirmGenerator")
    @ResponseBody
    public ResponseData confirmGenerator(HttpServletRequest request, @RequestBody MamSelectHeader mamSelectHeader) {
        IRequest requestCtx = createRequestContext(request);
        try {
            mamSelectHeader = service.confirmGenerator(requestCtx, mamSelectHeader);
        } catch (Exception e) {
            ResponseData rd = new ResponseData();
            rd.setSuccess(false);
            rd.setMessage(e.getMessage());
            return rd;
        }

        List<MamSelectHeader> mamSelectHeaders = new ArrayList<>();
        mamSelectHeaders.add(mamSelectHeader);
        return new ResponseData(mamSelectHeaders);
    }

    /**
     * 查找关联的选配值
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/select/header/selectAllRelateItemValue")
    @ResponseBody
    public ResponseData selectAllRelateItemValue(MamSelectHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                 @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        try {
            return new ResponseData(service.selectAllRelateItemValue(requestContext, dto, page, pageSize));
        } catch (Exception e) {
            ResponseData rd = new ResponseData();
            rd.setSuccess(false);
            rd.setMessage(ExceptionUtils.getRootCauseMessage(e));
            return rd;
        }
    }


    /**
     * 校验选配项选择是否按步选择
     *
     * @param request
     * @param mamSelectHeader
     * @return
     */
    @RequestMapping(value = "/hap/mam/select/header/validateChoose")
    @ResponseBody
    public ResponseData validateChoose(HttpServletRequest request, @RequestBody MamSelectHeader mamSelectHeader) {
        IRequest requestCtx = createRequestContext(request);

        try {
            mamSelectHeader = service.validateChoose(requestCtx, mamSelectHeader);
        } catch (Exception e) {
            ResponseData rd = new ResponseData();
            rd.setSuccess(false);
            rd.setMessage(ExceptionUtils.getRootCauseMessage(e));
            return rd;
        }
        List<MamSelectHeader> mamSelectHeaders = new ArrayList<>();
        mamSelectHeaders.add(mamSelectHeader);
        return new ResponseData(mamSelectHeaders);
    }

    /**
     * 当选完约束关系后插入必选项标记为Y的节点
     *
     * @param request
     * @param mamSelectHeader
     * @return
     */
    @RequestMapping(value = "/hap/mam/select/header/insertRequirdItem")
    @ResponseBody
    public ResponseData insertRequirdItem(HttpServletRequest request, @RequestBody MamSelectHeader mamSelectHeader) {
        IRequest requestCtx = createRequestContext(request);

        try {
            mamSelectHeader = service.insertRequirdItem(requestCtx, mamSelectHeader);
        } catch (Exception e) {
            ResponseData rd = new ResponseData();
            rd.setSuccess(false);
            rd.setMessage(ExceptionUtils.getRootCauseMessage(e));
            return rd;
        }
        List<MamSelectHeader> mamSelectHeaders = new ArrayList<>();
        mamSelectHeaders.add(mamSelectHeader);
        return new ResponseData(mamSelectHeaders);
    }

    /**
     * 校验是否完全校验
     *
     * @param request
     * @param mamSelectHeader
     * @return
     */
    @RequestMapping(value = "/hap/mam/select/header/validateAllChoose")
    @ResponseBody
    public ResponseData validateAllChoose(HttpServletRequest request, @RequestBody MamSelectHeader mamSelectHeader) {
        IRequest requestCtx = createRequestContext(request);

        try {
            mamSelectHeader = service.validateAllChoose(requestCtx, mamSelectHeader);
        } catch (Exception e) {
            ResponseData rd = new ResponseData();
            rd.setSuccess(false);
            rd.setMessage(ExceptionUtils.getRootCauseMessage(e));
            return rd;
        }
        List<MamSelectHeader> mamSelectHeaders = new ArrayList<>();
        mamSelectHeaders.add(mamSelectHeader);
        return new ResponseData(mamSelectHeaders);
    }

}