package com.hand.hap.mdm.item.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.mdm.item.dto.MdmItemPlatform;
import com.hand.hap.mdm.item.service.IMdmItemPlatformService;
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
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name MdmItemPlatformController
 * @description 物料平台controller
 * @date 2017/6/12
 */

@Controller
public class MdmItemPlatformController extends BaseController {

    @Autowired
    private IMdmItemPlatformService service;

    /**
     * 平台查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/platform/query")
    @ResponseBody
    public ResponseData query(MdmItemPlatform dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    /**
     * 平台数据提交
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/mdm/item/platform/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<MdmItemPlatform> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}