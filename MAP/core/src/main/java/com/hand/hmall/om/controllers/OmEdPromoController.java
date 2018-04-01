package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.om.dto.OmEdPromo;
import com.hand.hmall.om.service.IOmEdPromoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OmEdPromoController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IOmEdPromoService service;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    @RequestMapping(value = "/hmall/om/ed/promo/query")
    @ResponseBody
    public ResponseData query(OmEdPromo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/ed/promo/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<OmEdPromo> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/ed/promo/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OmEdPromo> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 查询事后促销规则
     *
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/hmall/om/ed/promo/selectOmEdPromo")
    @ResponseBody
    public ResponseData selectOmEdPromo(OmEdPromo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize){
        return new ResponseData(service.selectOmEdPromo(page,pageSize, dto));
    }

    /**
     * 启用 停用
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/om/ed/promo/updateStatus")
    @ResponseBody
    public ResponseData updateStatus(@RequestBody List<OmEdPromo> dto, String flag){
        return service.updateStatus( dto, flag);
    }

    /**
     * 保存新增事后促销规则
     *
     * @param omEdPromoList
     * @return
     */
    @RequestMapping(value = "/hmall/om/ed/promo/saveOmEdPromo")
    @ResponseBody
    public ResponseData saveOmEdPromo(HttpServletRequest request, @RequestBody List<OmEdPromo> omEdPromoList) {
        IRequest iRequest = this.createRequestContext(request);
        return service.saveOmEdPromo(iRequest, omEdPromoList);
    }

    /**
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @description 赠品发放列表查询
     */
    @RequestMapping(value = "/hmall/om/ed/promo/queryListInfo")
    @ResponseBody
    public ResponseData queryListInfo(OmEdPromo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryEdPromoListInfo(requestContext, dto, page, pageSize));
    }
    /**
     * 删除促销规则
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/om/ed/promo/deleteOmEdPromoById")
    @ResponseBody
    public ResponseData deleteOmEdPromoById(@RequestBody List<OmEdPromo> dto) {
        return service.deleteOmEdPromoById(dto);
    }


    @RequestMapping(value = "/hmall/om/ed/promo/queryOmEdPromo", method = RequestMethod.POST)
    @ResponseBody
    public com.hand.hmall.dto.ResponseData queryOmEdPromo(@RequestBody Map<String, Object> datas) {
        try {
            String url = "/h/sale/coupon/query";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(datas, null);
            com.hand.hmall.dto.ResponseData responseData = restTemplate.exchange(baseUri + modelUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            System.err.println(e);
        }
        com.hand.hmall.dto.ResponseData responseData = new com.hand.hmall.dto.ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("读取数据错误");
        return responseData;
    }

}
