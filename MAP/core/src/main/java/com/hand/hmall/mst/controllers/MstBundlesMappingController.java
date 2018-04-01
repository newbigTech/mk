package com.hand.hmall.mst.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.MstBundlesMapping;
import com.hand.hmall.mst.service.IMstBundlesMappingService;
import com.hand.hmall.mst.service.IMstBundlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author heng.zhang04@hand-china.com
 * @version 0.1
 * @name
 * @description 商品套装产品映射controller
 * @date 2017/08/30
 */
@Controller
public class MstBundlesMappingController extends BaseController {

    @Autowired
    private IMstBundlesMappingService service;
    @Autowired
    private IMstBundlesService bundlesService;

    /**
     * 查询对应的映射商品
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/bundles/mapping/query")
    @ResponseBody
    public ResponseData query(MstBundlesMapping dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectProduct(requestContext, dto, page, pageSize));
    }

    /**
     * 套装组成的提交
     *
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/mst/bundles/mapping/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MstBundlesMapping> dto, BindingResult result, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData rsd = new ResponseData();
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            rsd.setSuccess(false);
            rsd.setMessage(getErrorMessage(result, request));
            return rsd;
        }
        try {
            List<MstBundlesMapping> MstBundlesMappingList= service.batchUpdateMappingData(dto, requestCtx);
            rsd.setSuccess(true);
            rsd.setRows(MstBundlesMappingList);
            rsd.setMessage("提交成功");
        } catch (Exception e) {
            rsd.setSuccess(false);
            rsd.setMessage(e.getMessage());
        }

        return rsd;

    }

    @RequestMapping(value = "/hmall/mst/bundles/mapping/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MstBundlesMapping> dto) {
        ResponseData rsd = new ResponseData();

        try {
            service.batchDeleteMappnigData(dto);
            rsd.setSuccess(true);
            rsd.setMessage("刪除成功！");
        } catch (Exception e) {
            rsd.setSuccess(false);
            rsd.setMessage(e.getMessage());
        }

        return rsd;


    }


}