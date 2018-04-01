package com.hand.hap.atp.controllers;

import com.hand.hap.atp.dto.AtpPoInvInfo;
import com.hand.hap.atp.service.IAtpPoInvInfoService;
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
 * @author qiang.wang01@hand-china.com
 * @version 0.1
 * @name AtpPoInvInfoController
 * @description 采购类库存控制类
 * @date 2017/6/19
 */
@Controller
public class AtpPoInvInfoController extends BaseController {

    @Autowired
    private IAtpPoInvInfoService service;

    /**
     * 查询
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hap/atp/po/inv/info/query")
    @ResponseBody
    public ResponseData query(AtpPoInvInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryList(requestContext, dto, page, pageSize));
    }

    /**
     * 提交
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hap/atp/po/inv/info/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AtpPoInvInfo> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

}