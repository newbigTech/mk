package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.SuitlineMapping;
import com.hand.hmall.mst.service.ISuitlineMappingService;
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
 * @Description: 套件映射对象的Controller层
 * @date 2017/7/10 14:37
 */
@Controller
public class SuitlineMappingController extends BaseController {

    @Autowired
    private ISuitlineMappingService service;


    @RequestMapping(value = "/hmall/mst/suitline/mapping/query")
    @ResponseBody
    public ResponseData query(SuitlineMapping dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/mst/suitline/mapping/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<SuitlineMapping> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/mst/suitline/mapping/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<SuitlineMapping> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 商品详情页面中修改套件数量后，保存后同时修改syncflag标志
     */
    @RequestMapping(value = "/hmall/mst/suitline/mapping/updateSuitMapping")
    @ResponseBody
    public ResponseData updateSuitMapping(HttpServletRequest request, @RequestBody List<SuitlineMapping> dto) {
        IRequest requestCtx = createRequestContext(request);
        if (dto != null && dto.size() > 0) {
            for (SuitlineMapping suitlineMapping : dto) {
                service.updateByPrimaryKeySelective(requestCtx, suitlineMapping);
            }
        }
        return new ResponseData(dto);
    }

    /**
     * 查询商品下的套件编码和名称
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/suitline/mapping/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(SuitlineMapping dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectInfo(requestContext, dto, page, pageSize));
    }
}