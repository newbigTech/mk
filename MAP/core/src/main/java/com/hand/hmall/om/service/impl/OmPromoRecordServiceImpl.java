package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.mapper.OmPromoRecordMapper;
import com.hand.hmall.om.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hmall.om.dto.OmPromoRecord;
import com.hand.hmall.om.service.IOmPromoRecordService;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.support.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author yuxiaoli
 * @version 0.1
 * @name OmPromoRecordServiceImpl
 * @description 事后促销记录Service实现类
 * @date 2017/10/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OmPromoRecordServiceImpl extends BaseServiceImpl<OmPromoRecord> implements IOmPromoRecordService {

    @Autowired
    private OmPromoRecordMapper omPromoRecordMapper;

    @Autowired
    private IOrderService orderService;

    /**
     * 根据传进来的参数俩判断是符合条件用户还是候补用户
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<OmPromoRecord> selectPromoRecord(IRequest request, OmPromoRecord dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return omPromoRecordMapper.selectPromoRecord(dto);
    }

    /**
     * 将订单转化成调用“事后促销资格重新判定”微服务的参数
     * @param request
     * @param dto
     * @return
     */
    @Override
    public Map<String, Object> changeToParam(IRequest request, OmPromoRecord dto) {
        Map<String, Object> map = new HashMap<>();
        Assert.notNull(dto.getOrderId(), "订单id为空!");
        Assert.notNull(dto.getCustomerid(), "用户账号为空!");
        Order o = new Order();
        o.setOrderId(dto.getOrderId());
        Order order = orderService.selectByPrimaryKey(request, o);
        Assert.notNull(order, "订单已经不存在!");
        Assert.notNull(order.getOrderCreationtime(), "订单创建时间为空!");
        Assert.notNull(order.getOrderAmount(), "订单金额为空!");
        Assert.notNull(order.getSalechannelId(), "渠道为空!");

        //5个必输项
        map.put("orderId", dto.getOrderId());
        map.put("orderCreationtime", order.getOrderCreationtime());
        map.put("orderAmount", order.getOrderAmount());
        map.put("customerId", dto.getCustomerid());

        map.put("code", order.getCode());
        map.put("escOrderCode", order.getEscOrderCode());
        map.put("orderStatus", order.getOrderStatus());
        map.put("userId", order.getUserId());
        map.put("currencyId", order.getCurrencyId());
        map.put("websiteCode", order.getWebsiteId());
        map.put("channelCode", order.getSalechannelId());
        map.put("storeCode", order.getStoreId());

        map.put("paymentAmount", order.getPaymentAmount());
        map.put("discountFee", order.getDiscountFee());
        map.put("orderCreationtime", order.getOrderCreationtime());
        map.put("buyerMemo", order.getBuyerMemo());
        map.put("isInvoiced",order.getIsInvoiced());
        map.put("invoiceType",order.getInvoiceType());
        map.put("invoiceName",order.getInvoiceName());
        map.put("invoiceUrl",order.getInvoiceUrl());
        map.put("postFee",order.getPostFee());
        map.put("fixFee",order.getFixFee());
        map.put("totalcon",order.getTotalcon());
        map.put("receiverName",order.getReceiverName());
        map.put("receiverCountry",order.getReceiverCountry());
        map.put("receiverState",order.getReceiverState());
        map.put("receiverCity",order.getReceiverCity());
        map.put("receiverDistrict",order.getReceiverDistrict());
        map.put("receiverAddress",order.getReceiverAddress());
        map.put("receiverZip",order.getReceiverZip());
        map.put("receiverMobile",order.getReceiverMobile());
        map.put("receiverPhone",order.getReceiverPhone());
        map.put("estimateDeliveryTime",order.getEstimateDeliveryTime());
        map.put("estimateConTime",order.getEstimateConTime());
        map.put("payRate",order.getPayRate());
        map.put("payStatus",order.getPayStatus());
        map.put("splitAllowed",order.getSplitAllowed());
        map.put("shippingType",order.getShippingType());
        map.put("locked",order.getLocked());
        map.put("couponFee",order.getCouponFee());
        map.put("totalDiscount",order.getTotalDiscount());
        map.put("chosenCoupon",order.getChosenCoupon());
        map.put("chosenPromotion",order.getChosenPromotion());
        map.put("isIo",order.getIsIo());
        map.put("isOut",order.getIsOut());
        map.put("invoiceEntityId",order.getInvoiceEntityId());
        map.put("invoiceEntityAddr",order.getInvoiceEntityAddr());
        map.put("invoiceEntityPhone",order.getInvoiceEntityPhone());
        map.put("invoiceBankName",order.getInvoiceBankName());
        map.put("invoiceBankAccount",order.getInvoiceBankAccount());
        map.put("tradeFinishTime",order.getTradeFinishTime());
        map.put("orderType",order.getOrderType());

        return map;
    }

    /**
     * 查询某事后促销规则下'WAIT_FINI', 'FINISH'两个状态的记录个数
     * @param request
     * @param dto
     * @return
     */
    @Override
    public List<OmPromoRecord> selectFinishCount(IRequest request, OmPromoRecord dto) {
        return omPromoRecordMapper.selectFinishCount(dto);
    }
}