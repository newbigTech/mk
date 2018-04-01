package com.hand.hmall.as.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.ChangeGoodDto;
import com.hand.hmall.as.dto.ServiceorderEntry;
import com.hand.hmall.as.service.IServiceorderEntryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangmeng
 * @version 0.1
 * @name ServiceorderEntryController
 * @date 2017/7/19
 */
@Controller
public class ServiceorderEntryController extends BaseController {

    @Autowired
    private IServiceorderEntryService service;


    /**
     * 查询售后单关联的售后单行
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/order/serviceorderEntry/queryServiceOrderInfo")
    @ResponseBody
    public ResponseData queryServiceOrderInfo(ServiceorderEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryServiceOrderInfo(requestContext, dto, page, pageSize));
    }


    @RequestMapping(value = "/hmall/as/serviceorder/entry/query")
    @ResponseBody
    public ResponseData query(ServiceorderEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/serviceorder/entry/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<ServiceorderEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/serviceorder/entry/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ServiceorderEntry> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 根据服务单ID查询其对应的全部服务单行
     *
     * @param serviceOrderId - 服务单ID
     * @return
     */
    @RequestMapping(value = "/hmall/as/serviceOrder/entries/query")
    @ResponseBody
    public ResponseData queryOrderEntriesByOrderId(@RequestParam("serviceOrderId") long serviceOrderId) {
        return new ResponseData(service.queryOrderEntriesByOrderId(serviceOrderId));
    }

    /**
     * 根据服务单id查询退货单对应的所有服务单行
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return ResponseData
     */
    @RequestMapping(value = "/hmall/as/serviceorder/entry/queryReturnOrder")
    @ResponseBody
    public ResponseData queryReturnOrder(ServiceorderEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryReturnOrder(dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/serviceorder/entry/getServiceOrderListExcludeProductId")
    @ResponseBody
    public ResponseData getServiceOrderListExcludeProductId(String serviceOrderId, String excludeProductIds, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        ServiceorderEntry dto = new ServiceorderEntry();
        dto.setServiceOrderId(Long.valueOf(serviceOrderId));
        List<String> exclude = new ArrayList<>();
        if (!excludeProductIds.equals("")) {
            String[] ids = excludeProductIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                exclude.add(ids[i]);
            }
            dto.setExcludeProductIds(exclude);
        }

        List<ServiceorderEntry> list = service.getServiceOrderListExcludeProductId(dto, page, pageSize);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/hmall/as/serviceorder/entry/selectByOrderEntryIdList")
    @ResponseBody
    public ResponseData selectByOrderEntryIdList(HttpServletRequest request, @RequestBody List<ServiceorderEntry> dto) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectByOrderServiceOrderEntryIdList(requestContext, dto));
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 生成换发单
     */
    @RequestMapping(value = "/hmall/as/serviceorder/entry/createChangeGoodOrder")
    @ResponseBody
    public ResponseData createChangeGoodOrder(HttpServletRequest request, @RequestBody List<ChangeGoodDto> dto) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        if (CollectionUtils.isNotEmpty(dto)) {
            try {
                Map<String, Object> map = service.createChangeGoodOrder(requestCtx, dto.get(0));
                String orderCode = (String) map.get("orderCode");
                String returnCode = (String) map.get("returnCode");
                responseData.setCode(orderCode + "," + returnCode);
            } catch (Exception e) {
                responseData.setSuccess(false);
                responseData.setMessage(e.getMessage());
                return responseData;
            }
        }
        return responseData;
    }


    /**
     * @param request
     * @param dto
     * @return
     * @description 创建换货单时，获取交期
     */
    @RequestMapping(value = "/hmall/as/serviceorder/entry/getAtp")
    @ResponseBody
    public ResponseData getAtp(HttpServletRequest request, @RequestBody List<ChangeGoodDto> dto) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        if (CollectionUtils.isNotEmpty(dto)) {
            for (ChangeGoodDto changeGoodDto : dto) {
                try {
                    List<Date> dateList = service.getAtp(requestContext, changeGoodDto);
                    responseData.setRows(dateList);
                    return responseData;
                } catch (Exception e) {
                    responseData.setSuccess(false);
                    responseData.setMessage(e.getMessage());
                    return responseData;
                }
            }
        }
        return responseData;
    }

}