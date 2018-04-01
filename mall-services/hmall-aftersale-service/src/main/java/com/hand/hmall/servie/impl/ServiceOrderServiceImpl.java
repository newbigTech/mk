package com.hand.hmall.servie.impl;

import com.hand.hmall.dto.*;
import com.hand.hmall.exception.AfterSaleException;
import com.hand.hmall.mapper.*;
import com.hand.hmall.servie.IServiceOrderService;
import com.markor.map.external.setupservice.dto.SetupSequenceConditionDto;
import com.markor.map.external.setupservice.service.CreateSetupSequenceExternalService;
import com.markor.map.external.setupservice.service.ISetupSequenceHeaderExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @name IServiceOrderService
 * @Describe 服务单业务逻辑接口
 * @Author chenzhigang
 * @Date 2017/7/23
 */
@Service
public class ServiceOrderServiceImpl implements IServiceOrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServiceOrderMapper serviceOrderMapper;

    @Autowired
    private ServiceOrderEntryMapper serviceOrderEntryMapper;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private ReturnOrderMapper returnOrderMapper;
    @Autowired
    private RefundOrderMapper refundOrderMapper;
    @Autowired
    private SvsalesOrderMapper svsalesOrderMapper;
    @Autowired
    private ReturnOrderEntryMapper returnOrderEntryMapper;
    @Autowired
    private RefundOrderEntryMapper refundOrderEntryMapper;
    @Autowired
    private SvsalesOrderEntryMapper svsalesOrderEntryMapper;
    @Autowired
    private CreateSetupSequenceExternalService createSetupSequenceExternalService;
    @Autowired
    private ISetupSequenceHeaderExternalService iSetupSequenceHeaderExternalService;

    /**
     * @param escOrderCode - esc订单编号
     * @param webDisplay   - 网站显示名称
     * @return
     */
    @Override
    public List<ServiceOrder> queryServiceOrders(String escOrderCode, String webDisplay, List<String> conditions) {

        // 根据escOrderCode和外部网站名称查询对应的服务单列表
        List<ServiceOrder> serviceOrders = serviceOrderMapper.queryWebsiteServiceOrder(escOrderCode, webDisplay);

        List<ReturnOrder> returnOrders; // 退货单
        List<RefundOrder> refundOrders; // 退款单
        List<SvsalesOrder> svsalesOrders; // 服务销售单

        for (ServiceOrder so : serviceOrders) {

            Map userInfo = serviceOrderMapper.queryUserInfoByUserId(so.getUserId());
            if (userInfo == null) {
                // throw new AfterSaleException("not.found.user", "找不到ID为[" + so.getUserId() + "]的用户", null);
            } else {
                so.setCustomerid(userInfo.get("CUSTOMERID").toString());
            }

            if (conditions.contains("SOE")) {
                // 根据服务单查询服务单行列表
                so.setServiceOrderEntries(serviceOrderEntryMapper.selectByServiceOrder(so));
            }

            if (conditions.contains("RT") || conditions.contains("RTE")) {
                // 查询服务单对应的退货单，并设置到服务单中
                returnOrders = returnOrderMapper.selectByServiceOrder(so);
                so.setReturnOrders(returnOrders);
                if (conditions.contains("RTE")) {
                    for (ReturnOrder ro : returnOrders) {
                        // 查询退货单对应的退货单行信息
                        ro.setReturnOrderEntries(returnOrderEntryMapper.selectByReturnOrder(ro));
                    }
                }
            }

            if (conditions.contains("RF") || conditions.contains("RFE")) {
                // 查询服务单对应的退款单，并设置到服务单中
                refundOrders = refundOrderMapper.selectByServiceOrder(so);
                so.setRefundOrders(refundOrders);
                if (conditions.contains("RFE")) {
                    for (RefundOrder ro : refundOrders) {
                        // 查询退款单对应的退款单行信息
                        ro.setRefundOrderEntries(refundOrderEntryMapper.selectByRefundOrder(ro));
                    }
                }
            }

            if (conditions.contains("SS") || conditions.contains("SSE")) {
                // 查询服务单对应的服务销售单，并设置到服务单中
                svsalesOrders = svsalesOrderMapper.selectByServiceOrder(so);
                so.setSvsalesOrders(svsalesOrders);
                if (conditions.contains("SSE")) {
                    for (SvsalesOrder svsalesOrder : svsalesOrders) {
                        // 查询售后服务单对应的售后服务单行信息
                        svsalesOrder.setSvsalesOrderEntries(svsalesOrderEntryMapper.selectBySvsalesOrder(svsalesOrder));
                    }
                }
            }

            if (conditions.contains("M")) {
                // 查询服务单对应的媒体信息列表，并设置到服务单
                List<Map> mediaMap = serviceOrderMapper.selectMediaURLs(so);
                mediaMap.forEach(m -> {
                    String url = m.get("URL") != null ? m.get("URL").toString() : "";
                    String desc = m.get("IMAGE_DESCRIBE") != null ? m.get("IMAGE_DESCRIBE").toString() : "";
                    m.clear();
                    m.put("url", url);
                    m.put("desc", desc);
                });
                so.setMediaLinks(mediaMap);
            }
        }
        return serviceOrders;
    }

    @Override
    public List<RefundOrder> findByCondition(Long orderId) {
        return refundOrderMapper.selectByCondition(orderId);
    }

    @Transactional
    @Override
    public void createServiceOrder(ServiceOrder serviceOrder) {

        // 根据escOrderCode查询订单信息(封装在Map结构中)
        Map orderInfoMap = serviceOrderMapper.queryByEscOrderCode(serviceOrder.getEscOrderCode());
        if (orderInfoMap == null) {
            throw new AfterSaleException("not.exist.escOrderCode", "hmall系统中不存在外部订单号" + serviceOrder.getEscOrderCode(), null);
        }
        if (orderInfoMap.get("USER_ID") instanceof BigDecimal) {
            serviceOrder.setUserId(((BigDecimal) orderInfoMap.get("USER_ID")).toPlainString());
        }

        serviceOrder.setOrderId(((BigDecimal) orderInfoMap.get("ORDER_ID")).longValue());

        SetupSequenceConditionDto serviceOrderConditionDto = createSetupSequenceExternalService.createSetupSequence(16L, 9L, "serviceOrderCode", "", "AS", "CP"
                , "", "", "", "", "", "", "", "", "",
                "");
        String serviceOrderCode = iSetupSequenceHeaderExternalService.encode(serviceOrderConditionDto).getCode();

        logger.info("get service.order.code: " + serviceOrderCode);
        serviceOrder.setCode(serviceOrderCode);
        serviceOrder.setStatus("NEW");

        serviceOrder.setObjectVersionNumber(1L);
        serviceOrder.setCreatedBy(1L);
        serviceOrder.setCreationDate(new Date());
        serviceOrder.setLastUpdatedBy(1L);
        serviceOrder.setLastUpdateDate(new Date());
        serviceOrderMapper.insert(serviceOrder);

        for (ServiceOrderEntry soe : serviceOrder.getServiceOrderEntries()) {

            // 生成时自动关联服务单ID
            soe.setServiceOrderId(serviceOrder.getServiceOrderId());

            // 根据PIN码查询订单行相关信息
            Map orderEntryInfo = serviceOrderMapper.queryOrderEntryInfoByPin(soe.getPin());
            if (orderEntryInfo == null) {
                throw new AfterSaleException("not.exist.orderEntry.PIN", "根据PIN码[" + soe.getPin() + "]找不到订单行", null);
            }

            // 根据PIN码取订单行上的order_entry_id
            soe.setOrderEntryId(((BigDecimal) orderEntryInfo.get("ORDER_ENTRY_ID")).longValue());

            Object _lineNumber_ = orderEntryInfo.get("LINE_NUMBER");
            if (_lineNumber_ instanceof BigDecimal) {
                soe.setLineNumber(((BigDecimal) _lineNumber_).longValue());
            } else {
                throw new AfterSaleException("orderEntry.lineNumber.missing", "订单行[PIN=" + soe.getPin() + "]序号缺失", null);
            }

            // 根据PIN码取订单行上的单位
            soe.setUnit(orderEntryInfo.get("UNIT") == null ? null : orderEntryInfo.get("UNIT").toString());

            // 根据PIN码取订单行上的商品编码
            soe.setProductId(orderEntryInfo.get("PRODUCT_ID") == null ? null
                    : ((BigDecimal) orderEntryInfo.get("PRODUCT_ID")).longValue());

            // 根据PIN码取订单行上的变式物料号
            soe.setVproductCode(orderEntryInfo.get("VPRODUCT_CODE") == null ? null : orderEntryInfo.get("VPRODUCT_CODE").toString());

            // 根据PIN码取订单行上的套装号
            soe.setSutiCode(orderEntryInfo.get("SUTI_CODE") == null ? null : orderEntryInfo.get("SUTI_CODE").toString());

            // 根据PIN码取订单行上的行优惠金额
            soe.setDiscountFee(orderEntryInfo.get("DISCOUNT_FEE") == null ? null
                    : ((BigDecimal) orderEntryInfo.get("DISCOUNT_FEE")).doubleValue());

            // 根据PIN码取订单行上的实际单价
            soe.setUnitFee(orderEntryInfo.get("UNIT_FEE") == null ? null
                    : ((BigDecimal) orderEntryInfo.get("UNIT_FEE")).doubleValue());

            // 根据PIN码取订单行上的是否赠品
            soe.setSutiCode(orderEntryInfo.get("IS_GIFT") == null ? null : orderEntryInfo.get("IS_GIFT").toString());

            soe.setSyncflag("N");

            soe.setObjectVersionNumber(1L);
            soe.setCreatedBy(1L);
            soe.setCreationDate(new Date());
            soe.setLastUpdatedBy(1L);
            soe.setLastUpdateDate(new Date());

            SetupSequenceConditionDto orderEntryConditionDto = createSetupSequenceExternalService.createSetupSequence(36L, 9L, "ODE", "", "ODE", "CP"
                    , "", "", "", "", "", "", "", "", "",
                    "");
            String orderEntryCode = iSetupSequenceHeaderExternalService.encode(orderEntryConditionDto).getCode();
            logger.info("get order.entry.code: " + orderEntryCode);
            soe.setCode(orderEntryCode);
            serviceOrderEntryMapper.insert(soe);

        }

        for (Map mediaMap : serviceOrder.getMediaLinks()) {
            Media media = new Media();
            media.setServiceId(serviceOrder.getServiceOrderId().toString());
            media.setServiceOrderId(serviceOrder.getServiceOrderId());
            media.setUrl(mediaMap.get("url") != null ? mediaMap.get("url").toString() : null);
            media.setImageDescribe(mediaMap.get("urlDes") != null ? mediaMap.get("urlDes").toString() : null);
            media.setCode(mediaMap.get("url") + "#" + System.currentTimeMillis());
            media.setSyncflag("N");

            media.setObjectVersionNumber(1L);
            media.setCreatedBy(1L);
            media.setCreationDate(new Date());
            media.setLastUpdatedBy(1L);
            media.setLastUpdateDate(new Date());
            mediaMapper.insert(media);
        }

    }

}
