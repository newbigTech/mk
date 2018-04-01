package com.hand.hap.mdm.item.controllers;

/**
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name MdmItemSizeInfoController
 * @description 物料尺寸信息
 * @date 2017/6/19
 */


import com.hand.hap.core.IRequest;
import com.hand.hap.mdm.item.dto.MdmItemSizeInfo;
import com.hand.hap.mdm.item.service.IMdmItemSizeInfoService;
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
public class MdmItemSizeInfoController extends BaseController {

    @Autowired
    private IMdmItemSizeInfoService service;

    /**
     * 物料尺寸信息查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/size/info/query")
    @ResponseBody
    public ResponseData query(MdmItemSizeInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 物料尺寸信息数据提交
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/size/info/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<MdmItemSizeInfo> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    /**
     * 产品外形尺寸计算属性MAP->M3D
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/im/item/inface/exportSizeInfo")
    @ResponseBody
    public ResponseData exportSizeInfo(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        IRequest iRequest = createRequestContext(request);
        try {
            service.wsExportSizeToM3D(iRequest);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
}