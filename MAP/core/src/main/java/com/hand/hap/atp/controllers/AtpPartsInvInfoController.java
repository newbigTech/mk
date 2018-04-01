package com.hand.hap.atp.controllers;

import com.hand.hap.atp.dto.AtpPartsInvInfo;
import com.hand.hap.atp.service.IAtpPartsInvInfoService;
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
 * @author xiaowei.zhang@hand-china.com
 * @version 0.1
 * @name AtpPartsInvInfoController
 * @description 零部件可用库存Controller
 * @date 2017/6/21
 */

@Controller
public class AtpPartsInvInfoController extends BaseController {

    @Autowired
    private IAtpPartsInvInfoService service;

    /**
     * 零部件可用库存查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/parts/inv/info/query")
    @ResponseBody
    public ResponseData query(AtpPartsInvInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 零部件可用库存提交
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/atp/parts/inv/info/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AtpPartsInvInfo> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}