package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.Syncconfig;
import com.hand.hmall.mst.service.ISyncconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name SyncconfigController
 * @description 同步配置
 * @date 2017年5月26日10:52:23
 */
@Controller
public class SyncconfigController extends BaseController {

    @Autowired
    private ISyncconfigService service;


    @RequestMapping(value = "/hmall/mst/syncconfig/query")
    @ResponseBody
    public ResponseData query(Syncconfig dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/syncconfig/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Syncconfig> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/syncconfig/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Syncconfig> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 查询HMALL_MST_SYNCCONFIG 的数据
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/mst/syncconfig/lov")
    @ResponseBody
    public ResponseData lov(Syncconfig dto) {
        return new ResponseData(service.selectLovData(dto));
    }

    /**
     * 根据catalogVersionId查询所有的HMALL_MST_SYNCCONFIG
     * @param catalogVersionId
     * @return
     */
    @RequestMapping(value = "/hmall/mst/syncconfig/versionTo")
    @ResponseBody
    public ResponseData versionTo(Long catalogVersionId) {
        return new ResponseData(service.selectByCatalogVersionId(catalogVersionId));
    }

}