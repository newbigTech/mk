package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.PatchlineMapping;
import com.hand.hmall.mst.service.IPatchlineMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 补件映射对象的Controller层
 * @date 2017/7/10 14:37
 */
@Controller
public class PatchlineMappingController extends BaseController {

    @Autowired
    private IPatchlineMappingService service;


    @RequestMapping(value = "/hmall/mst/patchline/mapping/query")
    @ResponseBody
    public ResponseData query(PatchlineMapping dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/patchline/mapping/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<PatchlineMapping> dto) {
        System.out.println(dto.toString());
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/patchline/mapping/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<PatchlineMapping> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }


    /**
     * 查询商品下的补件编码和名称
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/patchline/mapping/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(PatchlineMapping dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectInfo(requestContext, dto, page, pageSize));
    }
}