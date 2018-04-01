package com.hand.hmall.om.controllers;

import com.alibaba.fastjson.JSON;
import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.om.dto.*;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.om.service.IOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author peng.chen
 * @version 0.1
 * @name OrderEntryController
 * @description 订单行
 * @date 2017年5月25日18:53:32
 */

@Controller
public class OrderEntryController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IOrderEntryService service;
    @Autowired
    private IOrderService orderService;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    @RequestMapping(value = "/hmall/om/order/entry/create")
    @ResponseBody
    public ResponseData create(@RequestParam("orderId") Long orderId, @RequestParam("code") String code) {
        System.out.println(orderId + ":" + code);
        return new ResponseData();
    }

    @RequestMapping(value = "/hmall/om/order/entry/query")
    @ResponseBody
    public ResponseData query(OrderEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/order/entry/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<OrderEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/order/entry/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OrderEntry> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 查询订单行
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/entry/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(OrderEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(requestContext, dto, page, pageSize));
    }

    /**
     * @param dto
     * @param request
     * @return
     * @description 订单详情页面中查询订单行信息，不分页，查询所有
     */
    @RequestMapping(value = "/hmall/om/order/entry/queryAllInfo")
    @ResponseBody
    public ResponseData queryAllInfo(OrderEntry dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(requestContext, dto, 1, Integer.MAX_VALUE));
    }

    @RequestMapping(value = "/hmall/om/order/entry/queryInfoNoPage")
    @ResponseBody
    public ResponseData queryInfoNoPage(OrderEntry dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(requestContext, dto));
    }


    /**
     * 拆分订单行
     *
     * @param request
     * @param orderEntries
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/entry/splitOrderEntry")
    @ResponseBody
    public ResponseData spiltOrderEntry(HttpServletRequest request, @RequestBody List<OrderEntry> orderEntries) {
        IRequest requestContext = createRequestContext(request);
        ResponseData rd = new ResponseData();
        String message = service.checkSuitableSplitOrderEntries(orderEntries);
        try {
            if (message == null) {
                List<OrderEntry> orderEntries1 = service.spiltOrderEntry(requestContext, orderEntries);
                rd.setMessage("订单拆分成功");
                rd.setRows(orderEntries1);
            } else {
                throw new RuntimeException(message + "");
            }
        } catch (RuntimeException e) {
            rd.setSuccess(false);
            rd.setMessage(e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return rd;
    }

    /**
     * 订单拆分时获取可拆分的订单行
     *
     * @param orderId
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/entry/splitOrderInfo")
    @ResponseBody
    public ResponseData splitOrderInfo(@RequestParam("orderId") Long orderId, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<OrderEntry> orderEntries = service.getSuitableSplitEntries(requestContext, orderId, page, pageSize);
        return new ResponseData(orderEntries);
    }

    /**
     * 查询服务单关联的订单行
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/entry/queryServiceOrderInfo")
    @ResponseBody
    public ResponseData queryServiceOrderInfo(OrderEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryServiceOrderInfo(requestContext, dto, page, pageSize));
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 更新订单行
     */
    @RequestMapping(value = "/hmall/om/order/entry/updateOrderEntry")
    @ResponseBody
    public ResponseData updateOrderEntry(HttpServletRequest request, @RequestBody List<OrderEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        if (CollectionUtils.isNotEmpty(dto)) {
            for (OrderEntry orderEntry : dto) {
                service.updateByPrimaryKeySelective(requestCtx, orderEntry);
                //增加书面记录
                OrderEntry entry = service.selectByPrimaryKey(requestCtx, orderEntry);
                HmallSoChangeLog soChangeLog = new HmallSoChangeLog();
                soChangeLog.setOrderId(entry.getOrderId());
                soChangeLog.setOrderEntryId(entry.getOrderEntryId());
                soChangeLog.setOrderType("1");
                soChangeLog.setPin(entry.getPin());
                soChangeLog.setProductId(entry.getProductId());
                orderService.addSoChangeLog(soChangeLog);
            }
        }
        return new ResponseData(dto);
    }


    /**
     * 根据订单行id查询订单行
     *
     * @param ids
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/entry/queryByIds")
    @ResponseBody
    public ResponseData queryByIds(@RequestBody List<Long> ids, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByIds(requestContext, ids, page, pageSize));
    }

    /**
     * 派工单界面派工单对应订单信息
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/entry/queryForDispatchOrder")
    @ResponseBody
    public ResponseData queryForDispatchOrder(OrderEntry dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryForDispatchOrder(requestContext, dto, page, pageSize));
    }

    /**
     * 订单取消的时候，判断当前操作是否是全部取消
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/om/order/entry/isAllCanceled")
    @ResponseBody
    public ResponseData isAllCanceled(@RequestBody List<OrderEntry> dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            List<OrderEntry> list = service.isAllCanceled(requestContext, dto);
            responseData.setRows(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }
        return responseData;
    }


    /**
     * @param dto
     * @param flag
     * @param request
     * @return
     * @description 获取用户可用促销（flag='N'） 或者 根据所选优惠计算订单金额（flag='Y'）
     */
    @RequestMapping(value = "/hmall/om/order/entry/querySaleActivityOptions")
    @ResponseBody
    public com.hand.hmall.dto.ResponseData querySaleActivityOptions(@RequestBody List<OrderEntry> dto, @RequestParam("flag") String flag, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        //获取非取消的normal状态的所有非组件订单行 以便调用促销微服务查询促销信息
        List<OrderEntry> list = service.selectUnCancelOrderEntry(requestContext, dto);

        OrderPojo orderPojo = service.changePojo(requestContext, list, flag);
        logger.info(JSON.toJSONString(orderPojo));
        try {
            String url = "hmall-drools-service/sale/execution/promote";
            HttpEntity<OrderPojo> entity = new HttpEntity<>(orderPojo, null);
            com.hand.hmall.dto.ResponseData responseData = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
            return responseData;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        com.hand.hmall.dto.ResponseData responseData = new com.hand.hmall.dto.ResponseData();
        responseData.setSuccess(false);
        if ("N".equals(flag)) {
            responseData.setMsg("获取用户可用促销失败！");
        } else {
            responseData.setMsg("优惠计算订单金额失败！");
        }
        return responseData;
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 取消订单行以及取消其子订单行
     */
    @RequestMapping(value = "/hmall/om/order/entry/cancelOrderEntry")
    @ResponseBody
    public ResponseData cancelOrderEntry(HttpServletRequest request, @RequestBody List<OrderEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            service.cancelOrderEntry(requestCtx, dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }
    /**
     * @param request
     * @param dto
     * @return
     * @description 取消赠品行
     */
    @RequestMapping(value = "/hmall/om/order/entry/cancelGiftEntry")
    @ResponseBody
    public ResponseData cancelGiftEntry(HttpServletRequest request, @RequestBody List<OrderEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            service.cancelGiftEntry(requestCtx, dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 新增赠品行
     */
    @RequestMapping(value = "/hmall/om/order/entry/addOrderEntry")
    @ResponseBody
    public ResponseData addOrderEntry(HttpServletRequest request, @RequestBody List<OrderEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            service.addOrderEntry(requestCtx, dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 对于天猫订单，取消订单行以及取消其子订单行
     */
    @RequestMapping(value = "/hmall/om/order/entry/cancelOrderEntryForTm")
    @ResponseBody
    public ResponseData cancelOrderEntryForTm(HttpServletRequest request, @RequestBody List<OrderEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
            service.cancelOrderEntryForTm(requestCtx, dto);
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 退货订单行以及退货其子订单行
     */
    @RequestMapping(value = "/hmall/om/order/entry/confirmReturnGoods")
    @ResponseBody
    public ResponseData confirmReturnGoods(HttpServletRequest request, @RequestBody List<OrderEntry> dto, Double currentAmount, String chosenCoupon, String chosenPromotion, String websiteId) {
        IRequest requestCtx = createRequestContext(request);
        return service.confirmReturnGoods(requestCtx, dto, currentAmount, chosenCoupon, chosenPromotion, websiteId);
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 订单取消时，对于全部订单行的取消（将所有订单行均置为取消状态），则订单头状态更新为"TRADE_CLOSED"
     */
    @RequestMapping(value = "/hmall/om/order/entry/cancelAllOrderAndRntry")
    @ResponseBody
    public ResponseData cancelAllOrderAndRntry(HttpServletRequest request, @RequestBody List<OrderEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.cancelAllOrderAndRntry(requestCtx, dto));
    }

    /**
     * @param request
     * @param dto
     * @return
     * @description 对于天猫订单，取消全部的订单行
     */
    @RequestMapping(value = "/hmall/om/order/entry/cancelAllOrderAndRntryForTm")
    @ResponseBody
    public ResponseData cancelAllOrderAndRntryForTm(HttpServletRequest request, @RequestBody List<OrderEntry> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.cancelAllOrderAndRntryForTm(requestCtx, dto));
    }


    /**
     * @param request
     * @return
     * @description 订单号数据比较
     */
    @RequestMapping(value = "/hmall/om/order/entry/allOrderEntryCompare")
    @ResponseBody
    public ResponseData allOrderEntryCompare(OrderEntryCompare dto, HttpServletRequest request, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                             @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.allOrderEntryCompare(requestCtx, dto, page, pageSize));
    }

    /**
     * @param request
     * @return
     * @description 订单号数据比较
     */
    @RequestMapping(value = "/hmall/om/order/entry/allOrderEntryComparePart")
    @ResponseBody
    public ResponseData allOrderEntryComparePart(OrderEntryComparePojo dto, HttpServletRequest request, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                 @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.allOrderEntryComparePart(requestCtx, dto, page, pageSize));
    }


    /**
     * @param request
     * @return
     * @description 订单行比较数据导出
     */
    @RequestMapping(value = "/hmall/om/order/entry/exportOrderEntryCompare")
    @ResponseBody
    public void exportOrderEntryCompare(@RequestParam String vproductCode, @RequestParam String platformCode, HttpServletRequest request, HttpServletResponse response) {
        OrderEntryCompare dto = new OrderEntryCompare();
        dto.setVproductCode(vproductCode);
        dto.setPlatformCode(platformCode);
        IRequest requestCtx = createRequestContext(request);
        List<OrderEntryCompare> list = service.exportOrderEntryCompare(requestCtx, dto);
        new ExcelUtil(OrderEntryCompare.class).exportExcel(list, "V码订单校验报表", list.size(), request, response, "V码订单校验报表.xlsx");
    }

    /**
     * @param request
     * @return
     * @description 部分订单行比较数据导出
     */
    @RequestMapping(value = "/hmall/om/order/entry/exportOrderEntryComparePart")
    @ResponseBody
    public void exportOrderEntryComparePart(@RequestParam String vproductCode, @RequestParam String platformCode, HttpServletRequest request, HttpServletResponse response) {
        OrderEntryComparePojo dto = new OrderEntryComparePojo();
        dto.setVproductCode(vproductCode);
        dto.setPlatformCode(platformCode);
        IRequest requestCtx = createRequestContext(request);
        List<OrderEntryComparePojo> list = service.exportOrderEntryComparePart(requestCtx, dto);
        new ExcelUtil(OrderEntryComparePojo.class).exportExcel(list, "V码订单校验报表", list.size(), request, response, "V码订单校验报表.xlsx");
    }


    /**
     * @param request
     * @param dto
     * @return
     * @description 获取新赠品时，获取交期
     */
    @RequestMapping(value = "/hmall/om/order/entry/getAtpTime")
    @ResponseBody
    public ResponseData getAtpTime(HttpServletRequest request, @RequestBody List<OrderEntry> dto,@RequestParam String receiverCity,@RequestParam String receiverDistrict) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        if (CollectionUtils.isNotEmpty(dto)) {

                try {
                    List<OrderEntry> dateList = service.getAtpTime(requestContext, dto,receiverCity,receiverDistrict);
                    responseData.setRows(dateList);
                    return responseData;
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
     * @description 构造赠品行数据
     */
    @RequestMapping(value = "/hmall/om/order/entry/setGiftEntry")
    @ResponseBody
    public ResponseData setGiftEntry(HttpServletRequest request, @RequestBody List<OrderEntry> dto,@RequestParam Long orderId) {
        IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        if (CollectionUtils.isNotEmpty(dto)) {

            try {
                List<OrderEntry> dataList = service.setGiftEntry(requestContext, dto,orderId);
                responseData.setRows(dataList);
                return responseData;
            } catch (Exception e) {
                responseData.setSuccess(false);
                responseData.setMessage(e.getMessage());
                return responseData;
            }

        }
        return responseData;
    }
}