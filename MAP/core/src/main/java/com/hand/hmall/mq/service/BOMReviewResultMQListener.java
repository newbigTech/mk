package com.hand.hmall.mq.service;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.om.service.IOrderEntryService;
import com.hand.hmall.pin.service.IPinService;
import com.hand.hmall.util.Constants;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BOM审核结果接收消息处理类
 *
 * @author alaowan
 * Created at 2017/12/26 13:01
 * @description
 */
@Service
public class BOMReviewResultMQListener {

    private static final String ORDER_BOM_APPROVED = "MAP0400";  //BOM审核

    @Autowired
    private IConsignmentService iConsignmentService;

    @Autowired
    private IOrderEntryService iOrderEntryService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    private IPinService iPinService;

    public ConsumeConcurrentlyStatus consumeMessage(MessageExt msg) {
        // {"pinCode":"P1000000CVY","approveStatus":"Y","refuseReason":""}
        String body = new String(msg.getBody());

        JSONObject jsonObject = JSONObject.fromObject(body);

        String pinCode = jsonObject.getString("pinCode");
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setPin(pinCode);

        // 根据pin码查询订单行
        List<OrderEntry> resultList = iOrderEntryService.select(orderEntry);
        if (CollectionUtils.size(resultList) != 1) {
            iLogManagerService.logTrace(this.getClass(), "V码审核消息处理类", null, "pin码[" + pinCode + "]不存在或不唯一");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        orderEntry = resultList.get(0);
        iLogManagerService.logTrace(this.getClass(), "V码审核消息处理类", null, "Message: " + body);

        if ("ZEST_approved".equalsIgnoreCase(msg.getTags())) {

            // 判断订单行状态是否为取消，如果是取消订单行则不作任何操作
            if (!Constants.ORDER_ENTRY_STATUS_CANCEL.equals(orderEntry.getOrderEntryStatus())) {

                // 判断订单行是否生成发货单
                if (orderEntry.getConsignmentId() != null) {

                    // 查询订单行对应的发货单
                    Consignment consignment = new Consignment();
                    consignment.setConsignmentId(orderEntry.getConsignmentId());
                    consignment = iConsignmentService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), consignment);

                    // 查询发货单下面所有未取消订单行
                    OrderEntry orderEntryParam = new OrderEntry();
                    orderEntryParam.setConsignmentId(orderEntry.getConsignmentId());
                    orderEntryParam.setOrderEntryStatus(Constants.ORDER_ENTRY_STATUS_NORMAL);
                    List<OrderEntry> orderEntryList = iOrderEntryService.select(orderEntryParam);

                    Long orderEntryId = orderEntry.getOrderEntryId();

                    // 筛选发货单下除当前行（已经BOM审核通过）外的其他行，判断是否全部BOM审通过
                    List<OrderEntry> others = orderEntryList.stream()
                            .filter(entry -> !entry.getOrderEntryId().equals(orderEntryId))
                            .collect(Collectors.toList());

                    // 如果发货单客审通过且发货单下所有订单行全部BOM审核通过，更新发货单状态为代发货、清空异常原因
                    if (Constants.YES.equals(consignment.getCsApproved()) && (others.isEmpty()
                            || others.stream().allMatch(entry -> Constants.YES.equals(entry.getBomApproved())))) {
                        consignment.setStatus(Constants.CON_STATUS_WAIT_FOR_DELIVERY);
                        consignment.setAbnormalReason("");
                        iConsignmentService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), consignment);
                    }
                }

                // 更新订单行的bomApproved
                orderEntry.setBomApproved(Constants.YES);
                iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);

                // 保存pin码节点消息
                iPinService.savePinInfos(Arrays.asList(orderEntry), Constants.EMPLOYEE_SYSTEM, ORDER_BOM_APPROVED, "已进行工艺审核");
            }

        } else if ("ZEST_rejected".equals(msg.getTags())) {
            // 若bom审核未通过，记录拒绝原因
            orderEntry.setBomApproved(Constants.NO);
            orderEntry.setBomRejectReason(jsonObject.getString("refuseReason"));
            iOrderEntryService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), orderEntry);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
