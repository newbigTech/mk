package com.hand.hmall.as.controllers;

import com.github.pagehelper.StringUtil;
import com.hand.common.util.ExcelUtil;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hmall.as.dto.AsRefund;
import com.hand.hmall.as.service.IAsRefundService;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.Paymentinfo;
import com.hand.hmall.om.service.IOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author: weishoupeng
 * name: RefundOrderController.java
 * discription: 退款单Controller
 * date: 2017/7/21
 * version: 0.1
 */
@Controller
public class RefundOrderController extends BaseController {

    @Autowired
    private IAsRefundService service;
    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICodeService iCodeService;

    /**
     * 退款单界面新建时根据订单ID查询对应的支付信息行
     *
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hmall/as/refundOrder/saveRefundOrderInfo")
    @ResponseBody
    public ResponseData saveRefundOrderInfo(HttpServletRequest request, @RequestBody List<AsRefund> dto) {
        IRequest requestCtx = createRequestContext(request);

        if (CollectionUtils.isNotEmpty(dto)) {
            AsRefund asRefund = dto.get(0);
            try {
                if (asRefund.getAsRefundId() != null) {
                    return new ResponseData(service.saveRefundOrderInfo(requestCtx, dto.get(0)));
                } else {
                    Order order = new Order();

                    order.setOrderId(asRefund.getOrderId());
                    Order orderInfo = orderService.selectByPrimaryKey(requestCtx, order);
                    ResponseData responseData = new ResponseData();
                    responseData.setSuccess(false);
                    responseData.setMessage("订单金额/订单支付金额有变更，请确认后重新创建退款单");
                    if (orderInfo.getCurrentAmount() == null && asRefund.getCurrentAmount() == null) {
                        if (orderInfo.getOrderAmount().compareTo(asRefund.getOrderAmount()) == 0 && (new BigDecimal(orderInfo.getPaymentAmount())).compareTo(new BigDecimal(asRefund.getPaymentAmount())) == 0) {
                            return new ResponseData(service.saveRefundOrderInfo(requestCtx, dto.get(0)));
                        } else {
                            return responseData;
                        }
                    } else if (orderInfo.getCurrentAmount() != null && asRefund.getCurrentAmount() != null) {
                        if (orderInfo.getOrderAmount().compareTo(asRefund.getOrderAmount()) == 0 && (new BigDecimal(orderInfo.getCurrentAmount())).compareTo(new BigDecimal(asRefund.getCurrentAmount())) == 0 &&
                                (new BigDecimal(orderInfo.getPaymentAmount())).compareTo(new BigDecimal(asRefund.getPaymentAmount())) == 0) {
                            return new ResponseData(service.saveRefundOrderInfo(requestCtx, dto.get(0)));
                        } else {
                            return responseData;
                        }
                    } else {
                        return responseData;
                    }
                }
            } catch (Exception e) {
                ResponseData response = new ResponseData(false);
                response.setMessage(e.getMessage());

                response.setCode("ERR_AT_SAVE_RF");
                return response;
            }

        } else {
            return new ResponseData();
        }
    }

    /**
     * 根据订单ID查询对应的支付信息，退款单新建时表格数据
     *
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/as/refundOrder/selectRefundOrderEntry")
    @ResponseBody
    public ResponseData selectRefundOrderEntry(@RequestParam("orderId") Long orderId, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<Paymentinfo> paymentinfos = service.selectRefundOrderEntry(requestContext, orderId);
        if (CollectionUtils.isEmpty(paymentinfos))
            return new ResponseData();
        else
            return new ResponseData(paymentinfos);
    }

    /**
     * 根据退款单ID查询对应的退款单信息
     *
     * @param asRefundId
     * @param request
     * @return
     */
    @RequestMapping(value = "/hmall/as/refundOrder/queryRefundOrderById")
    @ResponseBody
    public ResponseData queryDispatchorderById(@RequestParam("asRefundId") Long asRefundId, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(Arrays.asList(service.getRefundOrderInfo(asRefundId)));
    }


    /**
     * 查询并返回该订单可用于退款的金额
     *
     * @param orderId - 订单ID
     * @return
     */
    @GetMapping("/hmall/as/refundOrder/calculateReferenceSum")
    @ResponseBody
    public ResponseData calculateReferenceSum(@RequestParam("code") String code, @RequestParam("orderId") Long orderId, HttpServletRequest request) {
        IRequest iRequest = createRequestContext(request);
        return new ResponseData(Arrays.asList(service.howMuchCanBeUsedRefund(code, orderId, iRequest)));
    }

    /**
     * 工作流相关
     * 执行退款审批流程
     *
     * @param request
     * @param asRefundOrderId - 退款单ID
     * @return
     */
    @GetMapping("/hmall/as/refundOrder/approval")
    @ResponseBody
    public ResponseData approvalRefund(HttpServletRequest request, @RequestParam("id") Long asRefundOrderId) {
        ResponseData response;
        try {
            service.approvalRefund(createRequestContext(request), asRefundOrderId);
            response = new ResponseData(Arrays.asList("相关退款单ID：" + asRefundOrderId + "\n请到工作流待办事项中查看"));
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseData(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 提交审核
     *
     * @param refundOrderId
     * @param request
     * @return
     */
    @RequestMapping("/hmall/as/refund/commitApprove")
    @ResponseBody
    public ResponseData commitApprove(@RequestParam("asRefundId") Long refundOrderId, HttpServletRequest request) {
        return new ResponseData(service.updateStatusToProc(refundOrderId));
    }

    /**
     * 取消退款单
     *
     * @param refundOrderId
     * @param request
     * @return
     */
    @RequestMapping("/hmall/as/refund/cancelRefund")
    @ResponseBody
    public ResponseData cancelRefund(@RequestParam("asRefundId") Long refundOrderId, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return service.updateStatusToCanc(requestContext, refundOrderId);
    }

    /**
     * 完成退款
     *
     * @param refundOrderId
     * @param request
     * @return
     */
    @RequestMapping("/hmall/as/refund/completeRefund")
    @ResponseBody
    public ResponseData completeRefund(@RequestParam("asRefundId") Long refundOrderId, HttpServletRequest request) {
        return new ResponseData(service.updateStatusToFini(refundOrderId));
    }

    /**
     * 查询当前用户打开退款单页面时是否显示退款审批按钮
     *
     * @param request
     * @param asRefundId - 退款单ID
     * @return
     */
    @GetMapping("/hmall/as/refund/showExecuteRefundFlag")
    @ResponseBody
    public ResponseData showExecuteRefundFlag(HttpServletRequest request, @RequestParam("asRefundId") Long asRefundId) {

        IRequest iRequest = createRequestContext(request);
        AsRefund asRefund = service.getRefundOrderInfo(asRefundId);

        // 当前操作员岗位编码
        List<Map> positionMap = service.queryEmployeePositionInfo(iRequest.getUserId());
        // 退款单状态
        String refundStatus = asRefund.getStatus();

        // 具有退款执行权限的岗位代码列表
        List<String> refundAuthorityCodes = Arrays.asList(orderService.getProperties()
                .getProperty("refund.execute.process.authority.employee.codes").toUpperCase().split(","));

        if ("REJECT".equals(refundStatus) || "NEW".equals(refundStatus)) {
            for (Map positionInfo : positionMap) {
                // 退款单状态符合要求
                if (refundAuthorityCodes.contains(positionInfo.get("POSITION_CODE").toString().toUpperCase())) {
                    // 当前用户岗位具有执行退款审批的权限
                    return new ResponseData(Arrays.asList("show"));
                }
            }
        }
        return new ResponseData(Arrays.asList("hide"));
    }

    /**
     * 撤销执行退款流程，将退款单状态恢复
     *
     * @param request
     * @param procInsId
     * @return
     */
    @GetMapping("/hmall/as/refundOrder/activiti/procIns/back")
    public ResponseData repealRefundProcIns(HttpServletRequest request, @RequestParam("procInsId") Long procInsId) {
        IRequest iRequest = createRequestContext(request);
        service.repealRefundProcIns(iRequest, procInsId);
        return new ResponseData();
    }

    /**
     * 退款单列表查询
     *
     * @param maps
     * @return 订单列表集合
     */
    @RequestMapping(value = "/hmall/as/refund/query")
    @ResponseBody
    public ResponseData query(HttpServletRequest request, @RequestParam Map maps) {

        IRequest requestCtx = createRequestContext(request);

        String str = (String) maps.get("data");
        JSONObject jsonObject = JSONObject.fromObject(str);
        JSONObject status = jsonObject.getJSONObject("status");
        //退款单状态
        JSONArray refundStatus = status.getJSONArray("refundStatus");
        String[] strRefundStatus = new String[refundStatus.size()];
        for (int i = 0; i < refundStatus.size(); i++) {
            strRefundStatus[i] = refundStatus.get(i).toString();
        }
        //退款场景
        JSONArray refundScenario = status.getJSONArray("refundScenar");
        String[] strRefundScenario = new String[refundScenario.size()];
        for (int i = 0; i < refundScenario.size(); i++) {
            strRefundScenario[i] = refundScenario.get(i).toString();
        }
        //退款原因
        JSONArray refundReason = status.getJSONArray("refundReason");
        String[] strRefundReason = new String[refundReason.size()];
        for (int i = 0; i < refundReason.size(); i++) {
            strRefundReason[i] = refundReason.get(i).toString();
        }

        Map<String, Object> data = (Map<String, Object>) jsonObject.get("pages");
        //退款单单号
        String code = "";
        if (data.get("code") != null) {
            code = data.get("code").toString();
        }
        //服务单单号
        String serviceCode = "";
        if (data.get("serviceCode") != null) {
            serviceCode = data.get("serviceCode").toString();

        }
        //原销售订单号
        String orderCode = "";
        if (data.get("orderCode") != null) {
            orderCode = data.get("orderCode").toString();
        }
        //用户账户
        String customerid = "";
        if (data.get("customerid") != null) {
            customerid = data.get("customerid").toString();
        }
        //收货人手机号
        String mobile = "";
        if (data.get("mobile") != null) {
            mobile = data.get("mobile").toString();
        }
        //创建时间从
        String creationDateStart = "";
        if (data.get("creationDateStart") != null) {
            creationDateStart = data.get("creationDateStart").toString();
        }
        //创建时间至
        String creationDateEnd = "";
        if (data.get("creationDateEnd") != null) {
            creationDateEnd = data.get("creationDateEnd").toString();
        }
        //完结时间从
        String finishTimeStart = "";
        if (data.get("finishTimeStart") != null) {
            finishTimeStart = data.get("finishTimeStart").toString();
        }
        //完结时间至
        String finishTimeEnd = "";
        if (data.get("finishTimeEnd") != null) {
            finishTimeEnd = data.get("finishTimeEnd").toString();
        }

        int page = 0;
        if (data.get("page") != null) {
            page = Integer.parseInt(data.get("page").toString());
        }
        int pagesize = 0;
        if (data.get("pagesize") != null) {
            pagesize = Integer.parseInt(data.get("pagesize").toString());
        }
        List<AsRefund> list = service.selectRufundList(requestCtx,
                page, pagesize, code, serviceCode, orderCode, customerid, mobile, creationDateStart, creationDateEnd, finishTimeStart, finishTimeEnd, strRefundStatus, strRefundScenario, strRefundReason);
        return new ResponseData(list);
    }


    /**
     * 退款单列表导出
     *
     * @param request
     * @param response
     * @param code
     * @param serviceCode
     * @param orderCode
     * @param customerid
     * @param mobile
     * @param sCreationDateStart
     * @param sCreationDateEnd
     * @param sFinishTimeStart
     * @param sFinishTimeEnd
     * @param refundStatus
     * @param refundScenar
     * @param refundReason
     */
    @RequestMapping(value = "/hmall/as/refund/exportData", method = RequestMethod.GET)
    public void exportData(HttpServletRequest request, HttpServletResponse response, @RequestParam String code, @RequestParam String serviceCode, @RequestParam String orderCode, @RequestParam String customerid, @RequestParam String mobile, @RequestParam String sCreationDateStart, @RequestParam String sCreationDateEnd, @RequestParam String sFinishTimeStart, @RequestParam String sFinishTimeEnd, @RequestParam String refundStatus, @RequestParam String refundScenar, @RequestParam String refundReason) {

        IRequest requestCtx = createRequestContext(request);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //创建日期从
        String fCreationDateStart = "";
        if (!"".equals(sCreationDateStart)) {
            Date sDate = new Date(sCreationDateStart);
            fCreationDateStart = sdf.format(sDate);
        }
        //创建日期至
        String eCreationDateEnd = "";
        if (!"".equals(sCreationDateEnd)) {
            Date eDate = new Date(sCreationDateEnd);
            eCreationDateEnd = sdf.format(eDate);
        }
        //完结时间从
        String fFinishTimeStart = "";
        if (!"".equals(sFinishTimeStart)) {
            Date sDate = new Date(sFinishTimeStart);
            fFinishTimeStart = sdf.format(sDate);
        }
        //完结时间至
        String eFinishTimeEnd = "";
        if (!"".equals(sFinishTimeEnd)) {
            Date eDate = new Date(sFinishTimeEnd);
            eFinishTimeEnd = sdf.format(eDate);
        }
        //订单状态
        String[] strRefundStatus = refundStatus.split(",");
        //配送方式
        String[] strRefundScenar = refundScenar.split(",");
        //退款原因
        String[] strRefundReason = refundReason.split(",");
        if (strRefundStatus.length > 0) {
            if ("".equals(strRefundStatus[0])) {
                strRefundStatus = null;
            }
        }
        if (strRefundScenar.length > 0) {
            if ("".equals(strRefundScenar[0])) {
                strRefundScenar = null;
            }
        }
        if (strRefundReason.length > 0) {
            if ("".equals(strRefundReason[0])) {
                strRefundReason = null;
            }
        }
        List<AsRefund> list = service.selectRufundList(requestCtx,
                code, serviceCode, orderCode, customerid, mobile, fCreationDateStart, eCreationDateEnd, fFinishTimeStart, eFinishTimeEnd, strRefundStatus, strRefundScenar, strRefundReason);
        for (AsRefund asRefund : list) {
            if (!StringUtil.isEmpty(asRefund.getStatus())) {
                String status = iCodeService.getCodeMeaningByValue(requestCtx, "HMALL.AS.REFUND.STATUS", asRefund.getStatus());
                asRefund.setStatus(status);
            }
            if (!StringUtil.isEmpty(asRefund.getRefundScenario())) {
                String refundScenario = iCodeService.getCodeMeaningByValue(requestCtx, "HMALL.AS.REFUND_SCENARIO", asRefund.getRefundScenario());
                asRefund.setRefundScenario(refundScenario);
            }
            if (!StringUtil.isEmpty(asRefund.getReturnReason())) {
                String returnReason = iCodeService.getCodeMeaningByValue(requestCtx, "HMALL_AS_REFUND_REASON", asRefund.getReturnReason());
                asRefund.setReturnReason(returnReason);
            }
            if (!StringUtil.isEmpty(asRefund.getPayMode())) {
                String payMode = iCodeService.getCodeMeaningByValue(requestCtx, "HMALL.PAYMENT_TYPE", asRefund.getPayMode());
                asRefund.setPayMode(payMode);
            }
            if (!StringUtil.isEmpty(asRefund.getPayStatus())) {
                String payStatus = iCodeService.getCodeMeaningByValue(requestCtx, "SYS.YES_NO", asRefund.getPayStatus());
                asRefund.setPayStatus(payStatus);
            }
        }
        new ExcelUtil(AsRefund.class).exportExcel(list, "退款单列表", list.size(), request, response, "退款单列表.xlsx");
    }

}
