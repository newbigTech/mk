package com.hand.hmall.om.controllers;

import com.hand.hmall.util.Constants;
import com.hand.hmall.ws.client.IOrderPushClient;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hmall.hr.dto.MarkorEmployee;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.process.consignment.service.IConsignmentProcessService;
import com.hand.hmall.process.engine.ProcessManager;
import com.hand.hmall.ws.entities.OrderUpdateRequestbody;
import com.hand.hmall.ws.entities.OrderUpdateResponseBody;
import com.hand.hmall.ws.entities.UpdateReturnItem;
import com.markor.map.framework.common.exception.BusinessException;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name ConsignmentController
 * @description 发货单controller
 * @date 2017年6月5日13:54:47
 */
@Controller
public class ConsignmentController extends BaseController {

    @Autowired
    IOrderPushClient client;

    @Autowired
    private IConsignmentService service;

    @Autowired
    private IConsignmentProcessService consignmentProcessService;

    @Autowired
    @Qualifier("consignmentProcessManager")
    private ProcessManager<Consignment> consignmentProcessManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/hmall/om/consignment/query")
    @ResponseBody
    public ResponseData query(Consignment dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/consignment/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Consignment> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hmall/om/consignment/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Consignment> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 查询发货单列表信息
     *
     * @param maps 前端封装的参数
     * @return
     */
    @RequestMapping(value = "/hmall/om/consignment/selectConsignmentList")
    @ResponseBody
    public ResponseData selectConsignmentList(@RequestParam Map maps) {
        String str = (String) maps.get("data");
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
        net.sf.json.JSONObject status = jsonObject.getJSONObject("status");

        //订单状态
        JSONArray orderStatus_ = status.getJSONArray("orderStatus_");
        String[] strOrderStatus = new String[orderStatus_.size()];
        for (int i = 0; i < orderStatus_.size(); i++) {
            strOrderStatus[i] = orderStatus_.get(i).toString();
        }
        //配送方式
        JSONArray distribution = status.getJSONArray("distribution");
        String[] strDistribution = new String[distribution.size()];
        for (int i = 0; i < distribution.size(); i++) {
            strDistribution[i] = distribution.get(i).toString();
        }
        //订单类型
        JSONArray orderTypes = status.getJSONArray("orderTypes__");
        String[] strOrderTypes = new String[orderTypes.size()];
        for (int i = 0; i < orderTypes.size(); i++) {
            strOrderTypes[i] = orderTypes.get(i).toString();
        }

        //查询参数封装对象
        Map<String, Object> data = (Map<String, Object>) jsonObject.get("pages");

        //发货单号
        String code = "";
        if (data.get("code") != null)
            code = data.get("code").toString().trim();
        //平台订单编号
        String escOrderCode = "";
        if (data.get("escOrderCode") != null)
            escOrderCode = data.get("escOrderCode").toString().trim();
        //工艺审核
        String bomApproved = "";
        if (data.get("bomApproved") != null)
            bomApproved = data.get("bomApproved").toString();
        //是否暂挂
        String pause = "";
        if (data.get("pause") != null)
            pause = data.get("pause").toString();
        //快递单号
        String logisticsNumber = "";
        if (data.get("logisticsNumber") != null)
            logisticsNumber = data.get("logisticsNumber").toString().trim();
        //发货人手机号
        String receiverMobile = "";
        if (data.get("receiverMobile") != null)
            receiverMobile = data.get("receiverMobile").toString().trim();
        //收货人省
        String provice = "";
        if (data.get("provice") != null)
            provice = data.get("provice").toString().trim();
        //收货人市
        String city = "";
        if (data.get("city") != null)
            city = data.get("city").toString().trim();
        //快递公司
        String corporateName = "";
        if (data.get("corporateName") != null)
            corporateName = data.get("corporateName").toString().trim();
        //发货单是否已审核
        String csApproved = "";
        if (data.get("csApproved") != null)
            csApproved = data.get("csApproved").toString().trim();
        //开始时间
        String startTime = "";
        if (data.get("startTime") != null)
            startTime = data.get("startTime").toString().trim();
        //结束时间
        String endTime = "";
        if (data.get("endTime") != null)
            endTime = data.get("endTime").toString().trim();
        int page = 0;
        if (data.get("page") != null)
            page = Integer.parseInt(data.get("page").toString());
        int pagesize = 0;
        if (data.get("pagesize") != null)
            pagesize = Integer.parseInt(data.get("pagesize").toString());

        // 是否收货标识
        String confirmReceiving = null;
        if (data.containsKey("confirmReceiving")) {
            confirmReceiving = data.get("confirmReceiving").toString();
        }


        List<Consignment> list = service.selectConsignmentList(page, pagesize, logisticsNumber, code, provice, city, receiverMobile, startTime, endTime,
                strOrderStatus, strDistribution, strOrderTypes, corporateName, csApproved, bomApproved, pause, escOrderCode, confirmReceiving);
        return new ResponseData(list);
    }

    /**
     * 查询发货单详细信息
     *
     * @param dto      发货单
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/om/consignment/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(Consignment dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryInfo(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/hmall/om/consignment/queryById")
    @ResponseBody
    public ResponseData queryById(int consignmentId, HttpServletRequest request) {
        ResponseData result = new ResponseData(Arrays.asList(service.queryOne(consignmentId)));
        return result;
    }

    /**
     * TODO: 临时方法
     * <p>
     * 发货单详情页点击审核按钮更新状态，
     * 调用update(HttpServletRequest request, @RequestBody List<Consignment> dto)方法，dto对象可正常获得，
     * 但batchUpdate处执行错误。
     * <p>
     * 创建此方法单独实现更新发货单状态更新逻辑
     *
     * @return
     */
    @RequestMapping(value = "/hmall/om/consignment/updateStatus")
    @ResponseBody
    public ResponseData updateStatus(HttpServletRequest request, @RequestBody List<Consignment> dto) {
        IRequest requestCtx = createRequestContext(request);
        service.updateStatus(requestCtx, Math.toIntExact(dto.get(0).getConsignmentId()), dto.get(0).getStatus());
        return new ResponseData(Arrays.asList("success"));
    }


    /**
     * 更新异常状态的发货单为审核状态
     *
     * @param request
     * @param list
     * @return
     */
    @RequestMapping(value = "/hmall/om/consignment/examinestatus")
    @ResponseBody
    public ResponseData examinestatus(HttpServletRequest request, @RequestBody List<Consignment> list) {
        IRequest requestContext = createRequestContext(request);
        service.examinestatus(list, requestContext);
        return new ResponseData(list);
    }

    /**
     * @param request
     * @param list
     * @return
     * @description 审核发货单
     */
    @RequestMapping(value = "/hmall/om/consignment/check")
    @ResponseBody
    public ResponseData abnormalJudgment(HttpServletRequest request, @RequestBody List<Consignment> list) {
        IRequest requestContext = createRequestContext(request);
        //审核人
        MarkorEmployee markorEmployee = new MarkorEmployee();
        markorEmployee.setEmployeeId(requestContext.getUserId());

        if (list != null && list.size() > 0) {
            for (Consignment c : list) {
                Consignment consignment = service.selectByPrimaryKey(requestContext, c);
                if (consignment.getStatus() != null) {
                    //异常判定
                    if ("ABNORMAL".equals(consignment.getStatus())) {
                        try {
                            service.consignmentCheck(requestContext, consignment, markorEmployee);
                            //consignmentProcessService.abnormalJudgment(consignment, markorEmployee);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ResponseData rd = new ResponseData(false);
                            rd.setMessage(e.getMessage());
                            return rd;
                        }
                    } else if ("PROCESS_ERROR".equals(consignment.getStatus())) {
                        //重新进入配货单流程
                        consignmentProcessManager.start(consignment);
                    }
                }
            }
        }
        return new ResponseData(true);
    }

    /**
     * 发货单详情界面，发货单拆分按钮，手动拆分异常状态的发货单
     *
     * @param request
     * @param list
     * @return
     */
    @RequestMapping(value = "/hmall/om/consignment/split")
    @ResponseBody
    public ResponseData split(HttpServletRequest request, @RequestBody List<OrderEntry> list) {
        IRequest requestContext = createRequestContext(request);
        ResponseData rd = new ResponseData();
        try {
            String message = service.checkSplit(requestContext, list);
            if (message != null) {
                rd.setSuccess(false);
                rd.setMessage(message);
                return rd;
            }
            List<Consignment> consignments = service.split(requestContext, list);
            rd.setRows(consignments);
            rd.setSuccess(true);
        } catch (Exception e) {
            rd.setSuccess(false);
            rd.setMessage(e.getMessage());
            logger.error("发货单手工拆单时发生异常" + e.getLocalizedMessage());
        }
        return rd;
    }

    @RequestMapping(value = "/hmall/om/consignment/save")
    @ResponseBody
    public ResponseData saveConsignment(HttpServletRequest request, @RequestBody Consignment consignment) {
        ResponseData responseData = new ResponseData();
        try {
            service.saveConsignment(createRequestContext(request), consignment);
            responseData.setSuccess(true);
            responseData.setMessage("保存成功");
            return responseData;
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }
    }

    /**
     * @param request
     * @param consignment
     * @return
     * @description 申请暂挂订单
     */
    @RequestMapping(value = "/hmall/om/consignment/hold")
    @ResponseBody
    public ResponseData holdConsignment(HttpServletRequest request, @RequestBody Consignment consignment) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        IRequest iRequest = createRequestContext(request);
        Map<String, Object> map = new HashMap<>();
        map.put("syncflag", null);
        map.put("consignmentId", consignment.getConsignmentId());
        List<Consignment> consignmentList = service.selectSendRetailData(map);
        responseData = consignmentToRetail(responseData, iRequest, consignmentList);
        if (responseData.isSuccess()) {
            consignment.setPause("Y");
            consignment.setSyncflag("Y");
            service.updateByPrimaryKeySelective(iRequest, consignment);
        }
        return responseData;
    }

    /**
     * @param request
     * @param consignment
     * @return
     * @description 取消暂挂发货单
     */
    @RequestMapping(value = "/hmall/om/consignment/cancelHold")
    @ResponseBody
    public ResponseData cancelHoldConsignment(HttpServletRequest request, @RequestBody Consignment consignment) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        IRequest iRequest = createRequestContext(request);
        Consignment consignmentInfo = service.selectByPrimaryKey(iRequest, consignment);

        Map<String, Object> map = new HashMap<>();
        map.put("syncflag", null);
        map.put("consignmentId", consignment.getConsignmentId());
        List<Consignment> consignmentList = service.selectSendRetailData(map);
        responseData = consignmentToRetail(responseData, iRequest, consignmentList);
        if (responseData.isSuccess()) {
            responseData.setMessage("取消暂挂成功");
            consignment.setPause("N");
            consignment.setPauseReason("");
            consignment.setSyncflag("Y");
            service.updateByPrimaryKeySelective(iRequest, consignment);
            return responseData;
        } else {
            return responseData;
        }

    }

    private ResponseData consignmentToRetail(ResponseData responseData, IRequest iRequest, List<Consignment> consignmentList) {
        if (CollectionUtils.isNotEmpty(consignmentList)) {
            for (Consignment con : consignmentList) {
                OrderUpdateRequestbody requestbody = service.getBodyForOrderUpdateForConsignmentHold(iRequest, con, null);
                try {
                    OrderUpdateResponseBody responseBody = client.orderUpdate(requestbody);
                    List<UpdateReturnItem> result = responseBody.gettReturn().getItems();

                    if (CollectionUtils.isNotEmpty(result)) {
                        if ("S".equals(result.get(0).getTYPE())) {
                            responseData.setSuccess(true);
                            responseData.setMessage(result.get(0).getMSG());
                        } else if ("E".equals(result.get(0).getTYPE())) {
                            responseData.setSuccess(false);
                            responseData.setMessage(result.get(0).getMSG());
                        }
                    }
                } catch (Exception e) {
                    responseData.setSuccess(false);
                    responseData.setMessage(e.getMessage());
                }
            }
        }
        return responseData;
    }

    /**
     * @param dto
     * @param request
     * @return
     * @description 通过各属性进行查询，不分页
     */
    @RequestMapping(value = "/hmall/om/consignment/queryByCondition")
    @ResponseBody
    public ResponseData queryByCondition(Consignment dto, HttpServletRequest request) {
        return new ResponseData(service.select(dto));
    }


    /**
     * 生成合批号
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/om/consignment/mergeConsignment")
    @ResponseBody
    public ResponseData mergeConsignment(@RequestBody List<Consignment> dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String result = service.mergeConsignment(requestContext, dto);
        ResponseData responseData = new ResponseData();
        if (result.equals("success")) {
            responseData.setSuccess(true);
        } else {
            responseData.setSuccess(false);
            responseData.setMessage(result);
        }
        return responseData;
    }

    /**
     * 取消合批号
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/om/consignment/cancelMergeConsignment")
    @ResponseBody
    public ResponseData cancelMergeConsignment(@RequestBody List<Consignment> dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String result = service.cancelMergeConsignment(requestContext, dto);
        ResponseData responseData = new ResponseData();
        if (result.equals("success")) {
            responseData.setSuccess(true);
        } else {
            responseData.setSuccess(false);
            responseData.setMessage(result);
        }
        return responseData;
    }

    /**
     * @param consignment
     * @return
     * @description 发货单确认收货
     */
    @RequestMapping(value = "/hmall/om/consignment/tradeFinish")
    @ResponseBody
    public ResponseData tradeFinish(@RequestBody Consignment consignment) {
        ResponseData responseData = new ResponseData();
        if (consignment.getConsignmentId() == null) {
            responseData.setSuccess(false);
            responseData.setMessage("发货单id不能为空");
            return responseData;
        }

        try {
            service.consignmentConfirmFinish(consignment);
            responseData.setSuccess(true);
        } catch (BusinessException e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;


    }
}