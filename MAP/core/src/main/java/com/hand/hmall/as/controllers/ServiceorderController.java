package com.hand.hmall.as.controllers;

import com.alibaba.fastjson.JSON;
import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.as.dto.Serviceorder;
import com.hand.hmall.as.dto.ServiceorderEntry;
import com.hand.hmall.as.service.IServiceorderEntryService;
import com.hand.hmall.as.service.IServiceorderService;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ServiceorderController
 * @description 服务单列表页面 Controller类
 * @date 2017/7/17
 */
@Controller
public class ServiceorderController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IServiceorderService service;

    @Autowired
    private IServiceorderEntryService serviceorderEntryService;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("#{configProperties['baseUri']}")
    private String baseUri;

    @Value("#{configProperties['modelUri']}")
    private String modelUri;

    /**
     * 根据查询条件查询符合条件的服务单列表
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return ResponseData
     */
    @RequestMapping(value = "/hmall/as/serviceorder/queryServiceOrderList")
    @ResponseBody
    public ResponseData queryServiceOrderList(Serviceorder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.queryServiceOrderList(dto, page, pageSize));
    }

    /**
     * 查询服务单详细信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/serviceorder/selectServiceOrderByCode")
    @ResponseBody
    public ResponseData selectServiceOrderByCode(Serviceorder dto) {
        return new ResponseData(service.selectServiceOrderByCode(dto));
    }


    /**
     * 根据服务单ID查询多媒体中的图片信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/serviceorder/queryMediaByServiceOrderId")
    @ResponseBody
    public ResponseData queryMediaByServiceOrderId(Serviceorder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.queryMediaByServiceOrderId(dto, page, pageSize));
    }

    /**
     * 根据订单ID查询服务单信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/serviceorder/queryServiceOrderListBySaleCode")
    @ResponseBody
    public ResponseData queryServiceOrderListBySaleCode(Serviceorder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(service.queryServiceOrderListBySaleCode(dto, page, pageSize));
    }


    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/serviceorder/selectUserInfoByOrderId")
    @ResponseBody
    public ResponseData selectUserInfoByOrderId(Serviceorder dto) {
        return new ResponseData(service.selectUserInfoByOrderId(dto));
    }


    /**
     * 保存服务单信息 服务单行信息
     * 删除服务单行信息
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/serviceorder/saveServiceOrder")
    @ResponseBody
    public Serviceorder saveCategory(HttpServletRequest request, @RequestBody List<Serviceorder> dto, String serviceOrderId) {
        IRequest iRequest = this.createRequestContext(request);
        return service.saveCategory(iRequest, dto, serviceOrderId);
    }

    /**
     * 删除服务单行
     *
     * @return
     */
    @RequestMapping(value = "/hmall/as/serviceorder/deleteServiceOrderEntry")
    @ResponseBody
    public String deleteServiceOrderEntry(HttpServletRequest request, @RequestBody List<ServiceorderEntry> dto) {
        if (dto.size() > 0) {
            for (int i = 0; i < dto.size(); i++) {
                if (dto.get(i).getServiceOrderId() != null) {
                    serviceorderEntryService.deleteByPrimaryKey(dto.get(i));
                }
            }
            return "success";
        }
        return "fail";
    }

    @RequestMapping(value = "/hmall/as/serviceorder/query")
    @ResponseBody
    public ResponseData query(Serviceorder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/as/serviceorder/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Serviceorder> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/as/serviceorder/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Serviceorder> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 导出服务单数据
     *
     * @param dto
     * @param request
     * @param response
     */
    @RequestMapping(value = "/hmall/as/serviceorder/exportData", method = RequestMethod.GET)
    public void exportData(Serviceorder dto, HttpServletRequest request, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //创建日期从
        if (!"".equals(dto.getCreationStartTime())) {
            Date sDate = new Date(dto.getCreationStartTime());
            dto.setCreationStartTime(sdf.format(sDate));
        }
        //创建日期至
        if (!"".equals(dto.getCreationEndTime())) {
            Date eDate = new Date(dto.getCreationEndTime());
            dto.setCreationEndTime(sdf.format(eDate));
        }
        //完结日期从
        if (!"".equals(dto.getFinishStartTime())) {
            Date sDate = new Date(dto.getFinishStartTime());
            dto.setFinishStartTime(sdf.format(sDate));
        }
        //完结日期至
        if (!"".equals(dto.getFinishEndTime())) {
            Date eDate = new Date(dto.getFinishEndTime());
            dto.setFinishEndTime(sdf.format(eDate));
        }

        List<Serviceorder> list = service.queryServiceOrderListWithoutPage(dto);

        new ExcelUtil(Serviceorder.class).exportExcel(list, "服务单列表", list.size(), request, response, "服务单列表.xlsx");
    }

    /**
     * 设置服务单归属信息
     *
     * @param soIDs      - 服务单ID列表
     * @param employeeId - 雇员ID
     * @return
     */
    @GetMapping("/hmall/as/serviceOrder/setAssiging")
    @ResponseBody
    public ResponseData setAssiging(@RequestParam("soIDs") String soIDs, @RequestParam("employeeId") Long employeeId) {
        List<String> soIdList = Arrays.asList(soIDs.split(","));
        List<Long> soIds_ = new ArrayList<>();
        for (String idStr : soIdList) {
            soIds_.add(Long.parseLong(idStr));
        }
        service.setAssiging(soIds_, employeeId);
        return new ResponseData();
    }

    /**
     * 新建保价单
     *
     * @return
     */
    @RequestMapping(value = "/hmall/as/serviceOrder/insuredOrder")
    @ResponseBody
    public com.hand.hmall.dto.ResponseData insuredOrder(@RequestParam("orderId") Long orderId, @RequestParam("serviceOrderId") Long serviceOrderId, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        com.hand.hmall.dto.ResponseData responseData = new com.hand.hmall.dto.ResponseData();
        //查询订单是否可以保价
        if (!service.checkInsuredOrder(orderId)) {
            responseData.setSuccess(false);
            responseData.setMsg("该订单已经无法保价！");
            return responseData;
        }
        //组装数据
        Order order = service.insuredOrder(orderId);
        if (order != null) {
            try {
                String url = "hmall-drools-service/sale/execution/supportValue";
                HttpEntity<Order> entity = new HttpEntity<>(order, null);
                responseData = restTemplate.exchange(baseUri + url, HttpMethod.POST, entity, com.hand.hmall.dto.ResponseData.class).getBody();
                //数据转换
                JSONArray jsonArray = JSONArray.fromObject(JSON.toJSONString(responseData.getResp()));
                //装为order对象
                Order orderResult = (Order) JSONObject.toBean(JSONObject.fromObject(jsonArray.get(0)), Order.class);
                //转换订单行
                if (orderResult != null && (CollectionUtils.isNotEmpty(orderResult.getOrderEntryList()))) {
                    List<OrderEntry> orderEntryList = new ArrayList<>();
                    for (int i = 0; i < orderResult.getOrderEntryList().size(); i++) {
                        orderEntryList.add((OrderEntry) JSONObject.toBean(JSONObject.fromObject(orderResult.getOrderEntryList().get(i)), OrderEntry.class));
                    }
                    orderResult.setOrderEntryList(orderEntryList);
                }
                //判断是否需要生成销售赔付单
                if (service.checkInsuredOrderExtraReduce(orderResult)) {
                    //生成销售赔付单 更新订单 订单行信息.同时记录“赔付费用”大于0即生成对应退款单
                    orderResult.setOrderId(orderId);
                    responseData = service.insertAsCompensate(orderResult, serviceOrderId, requestContext);
                } else {
                    responseData.setSuccess(false);
                    responseData.setMsg("该订单不需要保价！");
                }
                return responseData;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                responseData.setSuccess(false);
                responseData.setMsg("新建保价单失败！");
                return responseData;
            }
        } else {
            responseData.setSuccess(false);
            responseData.setMsg("新建保价单失败！");
            return responseData;
        }
    }

}