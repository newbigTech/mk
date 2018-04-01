package com.hand.hmall.job;

import com.hand.common.job.JobConcurrentManager;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.log.dto.LogManager;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.StringUtils;
import com.hand.hmall.ws.client.IOrderPushClient;
import com.hand.hmall.ws.client.IOrderToEccPushClient;
import com.hand.hmall.ws.entities.*;
import com.markor.map.framework.remotejob.client.annotation.RemoteJob;
import com.markor.map.framework.remotejob.client.common.JobResult;
import com.markor.map.framework.remotejob.client.common.RemoteJobTask;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author peng.chen
 * @version 0.1
 * @name:
 * @Description: 订单推送retail接口
 * @date 2017/6/8 0008 下午 3:40
 */
@RemoteJob
public class OrderPushRetailJob extends AbstractJob implements RemoteJobTask {

    private static final String JOB_DESC = "订单推送retail接口job";

    @Autowired
    private IOrderPushClient client;
    @Autowired
    private IOrderToEccPushClient orderToEccPushClient;

    @Autowired
    private IConsignmentService consignmentService;

    @Autowired
    private ILogManagerService logManagerService;

    @Autowired
    private ISequenceGenerateService sequenceGenerateService;

    @Autowired
    private JobConcurrentManager jobConcurrentManager;

    @Autowired
    private ILogManagerService iLogManagerService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderEntryService orderEntryService;

    /**
     * JOB
     *
     * @param jobExecutionContext
     * @throws Exception
     */
    @Override
    public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {

        Logger logger = LoggerFactory.getLogger(OrderPushRetailJob.class);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");

        //排斥自身的redis锁
        Boolean lockBoolean = jobConcurrentManager.tryBegin();
        if (lockBoolean) {

            //排斥全款可发运job的业务redis锁
            jobConcurrentManager.beginWithLock("job_toRetail_mutex_canBeShiped");

            try {
                logger.info(" OrderPushRetailJob  locked" + formatter.format(new Date(System.currentTimeMillis())) + ">>>>:" + Thread.currentThread().getName());

                IRequest iRequest = RequestHelper.newEmptyRequest();
                iRequest.setRoleId(Constants.JOB_DEFAULT_USERID);
                iRequest.setLocale(Constants.JOB_DEFAULT_LANG);
                Map<String, Object> map = new HashMap<>();
                map.put("syncflag", "N");
                map.put("consignmentId", null);
                List<Consignment> consignmentList = consignmentService.selectSendRetailData(map);
                if (CollectionUtils.isNotEmpty(consignmentList)) {
                    for (Consignment consignment : consignmentList) {

                        //序列号
                        String sapCode = consignment.getSapCode();

                        LogManager log = new LogManager();
                        log.setStartTime(new Date());
                        log.setDataPrimaryKey(consignment.getConsignmentId());
                        log.setProgramName(this.getClass().getName());
                        log.setSourcePlatform(Constants.SOURCE_PLATFORM_ORDER_JOB);
                        if (StringUtils.isEmpty(sapCode)) {
                            log.setProgramDescription("订单推送Retail-新增");
                        } else {
                            log.setProgramDescription("订单推送Retail-修改");
                        }
                        log = logManagerService.logBegin(iRequest, log);

                        //创建订单
                        if (StringUtils.isEmpty(sapCode)) {
                            //如果是创建订单，过滤掉'TRADE_CLOSED'状态的发货单
                            if ("TRADE_CLOSED".equals(consignment.getStatus())) {
                                continue;
                            }

                            OrderRequestBody body = consignmentService.getBodyForOrderToRetail(iRequest, consignment, log);
                            if (log != null && log.getReturnMessage() != null) {
                                continue;
                            }

                            sapCode = sequenceGenerateService.getNextNumber("sapCode", 9L, "7");
                            char[] stringArr = sapCode.toCharArray();
                            String str = String.valueOf(stringArr[1]);
                            int i = Integer.parseInt(str);
                            i = i + 65;
                            StringBuffer sb = new StringBuffer(sapCode);
                            sb.replace(1, 2, String.valueOf((char) i));
                            sapCode = String.valueOf(sb);

                            body.setLvSo(sapCode);

                            try {
                                log.setMessage(JSONObject.fromObject(body).toString());
                                OrderResponseBody res = client.OrderPush(body);

                                List<ReturnItem> result = res.getGdtReturn().getItems();
                                if (CollectionUtils.isNotEmpty(result)) {
                                    Boolean flag = false;
                                    String returnMessage = "SapCode:" + sapCode;
                                    if ("S".equals(result.get(0).getTYPE())) {
                                        flag = true;
                                    }
                                    for (ReturnItem returnItem : result) {
                                        returnMessage = returnMessage + " 返回信息：" + returnItem.getMESSAGE() + ";";
                                    }
                                    log.setProcessStatus(flag ? "S" : "E");
                                    log.setReturnMessage(returnMessage);
                                    if (flag) {
                                        Consignment dto = new Consignment();
                                        dto.setConsignmentId(consignment.getConsignmentId());
                                        dto.setSapCode(sapCode);
                                        dto.setSyncflag(Constants.YES);
                                        consignmentService.updateByPrimaryKeySelective(iRequest, dto);
                                        //订单同步retail成功后，再同步Ecc
                                        orderToEcc(iRequest, consignment);
                                    }
                                }
                                logManagerService.logEnd(iRequest, log);
                            } catch (Exception e) {
                                log.setProcessStatus("E");
                                log.setReturnMessage(e.getMessage());
                                logManagerService.logEnd(iRequest, log);
                            }

                            //更新订单
                        } else {
                            OrderUpdateRequestbody requestbody = consignmentService.getBodyForOrderUpdateToRetail(iRequest, consignment, log);
                            if (log != null && log.getReturnMessage() != null) {
                                continue;
                            }

                            try {
                                log.setMessage(JSONObject.fromObject(requestbody).toString());
                                OrderUpdateResponseBody responseBody = client.orderUpdate(requestbody);

                                List<UpdateReturnItem> result = responseBody.gettReturn().getItems();
                                if (CollectionUtils.isNotEmpty(result)) {
                                    Boolean flag = false;
                                    String returnMessage = "SapCode:" + consignment.getSapCode();
                                    if ("S".equals(result.get(0).getTYPE())) {
                                        flag = true;
                                    }
                                    for (UpdateReturnItem returnItem : result) {
                                        returnMessage = returnMessage + " 返回信息：" + returnItem.getMSG() + ";";
                                    }
                                    log.setProcessStatus(flag ? "S" : "E");
                                    log.setReturnMessage(returnMessage);
                                    if (flag) {
                                        Consignment dto = new Consignment();
                                        dto.setConsignmentId(consignment.getConsignmentId());
                                        //发货单状态为“WAIT_FOR_DELIVERY“，且发货单‘是否暂挂’字段【CONSIGNMENT.PAUSE】='Y'时
                                        if (Constants.CON_STATUS_WAIT_FOR_DELIVERY.equals(consignment.getStatus()) && "Y".equals(consignment.getPause())) {
                                            dto.setPauseReason(""); //将发货单头表中“暂挂原因”字段的值置为空
                                            dto.setPause("N");
                                            /*if(consignment.getCanDelivery() != null && Constants.ORDER_TO_RETAIL_CAN_DELIVERY.equals(consignment.getCanDelivery())){
                                                dto.setPause("N");  //若是否可发运【CAN_DELIVERY】为'Y',则将【CONSIGNMENT.PAUSE】字段置为'N';
                                            }*/
                                        }
                                        dto.setSyncflag(Constants.YES);
                                        consignmentService.updateByPrimaryKeySelective(iRequest, dto);
                                        //订单同步retail成功后，再同步Ecc
                                        orderToEcc(iRequest, consignment);
                                    }
                                }
                                logManagerService.logEnd(iRequest, log);
                            } catch (Exception e) {
                                log.setProcessStatus("E");
                                log.setReturnMessage(e.getMessage());
                                logManagerService.logEnd(iRequest, log);
                            }
                        }
                    }

                }
            } catch (Exception e) {
                iLogManagerService.log(this.getClass(), JOB_DESC, null, e.getMessage(), JOB_DESC, "异常");
            } finally {
                jobConcurrentManager.finish();
                jobConcurrentManager.finish("job_toRetail_mutex_canBeShiped");
                logger.info("释放OrderPushRetailJob锁的时间" + formatter.format(new Date(System.currentTimeMillis())) + ">>>>:" + Thread.currentThread().getName());

            }
        } else {
            logger.info(">>>>>>>OrderPushRetailJob 有相同job 执行，尝试请求锁失败：" + Thread.currentThread().getName());
        }


    }

    //订单推送Ecc
    private void orderToEcc(IRequest iRequest, Consignment consignment) throws WSCallException {
        Order order = new Order();
        order.setOrderId(consignment.getOrderId());
        Order orderInfo = orderService.selectByPrimaryKey(iRequest, order);
        List<OrderEntry> orderEntryList = orderEntryService.selectRetailData(consignment.getConsignmentId());
        OrderToEccRequestBody orderToEccRequestBody = new OrderToEccRequestBody();
        OrderToEccRequestBody.TData tData = new OrderToEccRequestBody.TData();
        List<OrderToEccRequestBody.TData.OrderToEccItem> orderToEccItemList = new ArrayList<>();
        for (OrderEntry entry : orderEntryList) {
            OrderToEccRequestBody.TData.OrderToEccItem orderToEccItem = new OrderToEccRequestBody.TData.OrderToEccItem();
            orderToEccItem.setCode(orderInfo.getCode());
            orderToEccItem.setCodeLine(entry.getCode());
            orderToEccItem.setEscOrderCode(orderInfo.getEscOrderCode());
            orderToEccItem.setZmatnr(entry.getPin());
            orderToEccItemList.add(orderToEccItem);
        }
        tData.setItems(orderToEccItemList);
        orderToEccRequestBody.settData(tData);
        orderToEccPushClient.OrderToEccPush(orderToEccRequestBody);
    }

    @Override
    public JobResult execute() throws Exception {
        safeExecute(null);
        return JobResult.success(null);
    }
}

