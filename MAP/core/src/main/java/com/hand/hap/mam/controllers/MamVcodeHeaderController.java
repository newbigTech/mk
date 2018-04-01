package com.hand.hap.mam.controllers;
/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name MamVcodeHeaderController
 * @description v码头controller
 * @date 2017/5/28
 */

import com.alibaba.fastjson.JSONObject;
import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.mam.dto.MamSelectStatus;
import com.hand.hap.mam.dto.MamVcodeHeader;
import com.hand.hap.mam.dto.VolumeCalResponseBody;
import com.hand.hap.mam.service.IMamVcodeHeaderService;
import com.hand.hap.system.controllers.BaseController;
import com.markor.map.framework.common.interf.entities.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MamVcodeHeaderController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MamVcodeHeaderController.class);

    @Autowired
    private IMamVcodeHeaderService service;

    /**
     * v码头查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/vcode/header/query")
    @ResponseBody
    public ResponseData query(MamVcodeHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectVcodeHeader(requestContext, dto, page, pageSize));
    }

    /**
     * v码头查询（查询必选以外的v码头信息）
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/vcode/header/type/query")
    @ResponseBody
    public ResponseData queryType(MamVcodeHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectVcodeHeaderType(requestContext, dto, page, pageSize));
    }

    /**
     * V码类型修改（定制转主推，主推转定制）
     * MAG-1471
     *
     * @param request
     * @param vcodeHeaders
     * @return
     */
    @RequestMapping(value = "/hap/mam/vcode/header/changeVCodeType")
    @ResponseBody
    public ResponseData changeVCodeType(HttpServletRequest request, @RequestBody List<MamVcodeHeader> vcodeHeaders) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData resp = new ResponseData();
        try {
            service.changeVCodeType(requestCtx, vcodeHeaders);
        } catch (Exception e) {
            logger.error(this.getClass().getCanonicalName(), e);
            resp.setSuccess(false);
            resp.setMessage(e.getMessage());
            return resp;
        }
        List<Long> headerIdList = new ArrayList<>();
        for (MamVcodeHeader header : vcodeHeaders) {
            headerIdList.add(header.getHeaderId());
        }
        List<MamVcodeHeader> mamvVcodeHeaders = service.batchSelectVcodeHeader(headerIdList);
        return new ResponseData(mamvVcodeHeaders);
    }

    /**
     * 查询头信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mam/vcode/header/headerInfo")
    @ResponseBody
    public ResponseData selectRetrieveOrderById(MamVcodeHeader dto) {
        return new ResponseData(service.headerInfo(dto));
    }

    /**
     * 获得根据v码计算尺寸接口的返回信息
     *
     * @param code
     * @param msgCode
     * @param msgDetail
     * @param returnValue
     * @return
     * @author xuxiaoxue
     * @date 2017/8/18
     */
    private VolumeCalResponseBody getResponseBody(String code, String msgCode, String msgDetail, String returnValue) {
        VolumeCalResponseBody responseBody = new VolumeCalResponseBody();
        responseBody.setCode(code);
        responseBody.setMsgCode(msgCode);
        responseBody.setMsgDetail(msgDetail);
        responseBody.setReturnValue(returnValue);
        return responseBody;
    }

    /**
     * 查询头信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mam/vcode/header/getHeaderList")
    @ResponseBody
    public ResponseData getHeaderList(MamVcodeHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getHeaderList(dto, requestContext, page, pageSize));
    }

    /**
     * 根据条件导出所需数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mam/vcode/header/downloadHeaderList")
    @ResponseBody
    public void download(@RequestParam String config, HttpServletRequest request, HttpServletResponse response) {
        String dataM = config.substring(9, config.length() - 1);//切割需要的数据
        JSONObject jsStr = JSONObject.parseObject(dataM);//将字符转为json对象

        MamVcodeHeader dto = JSONObject.toJavaObject(jsStr, MamVcodeHeader.class);
        List<MamSelectStatus> mamVcodeHeaderList = service.downLoadData(dto);
        new ExcelUtil(MamSelectStatus.class).exportExcel(mamVcodeHeaderList, "选配状态数据", mamVcodeHeaderList.size(), request, response, "选配状态数据.xlsx");
    }

}
