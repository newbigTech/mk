package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.markor.map.framework.soapclient.exceptions.WSCallException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.Paymentinfo;
import com.hand.hmall.om.mapper.PaymentinfoMapper;
import com.hand.hmall.om.service.IOrderService;
import com.hand.hmall.om.service.IPaymentinfoService;
import com.hand.hmall.util.Constants;
import com.hand.hmall.util.DateUtil;
import com.hand.hmall.ws.client.IPaymentPushClient;
import com.hand.hmall.ws.entities.PaymentRequestBody;
import com.hand.hmall.ws.entities.PaymentRequestItem;
import com.hand.hmall.ws.entities.PaymentResponseBody;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peng.chen
 * @version 0.1
 * @name PaymentinfoServiceImpl
 * @description 付款信息
 * @date 2017年5月25日18:53:32
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class PaymentinfoServiceImpl extends BaseServiceImpl<Paymentinfo> implements IPaymentinfoService {

    private static final String BUKRS = "0201";              // 公司代码
    private static final String ZKUNNR = "9520";              // 用户代码
    private static final String FINANCIAL_TYPE_PAY = "收款";  // 财务类型
    private static final String DATE_FORMAT = "yyyy-MM-dd";   // 财务类型
    private static final String TIME_FORMAT = "HH:mm:ss";   // 财务类型

    private static final String SUCCESS = "保存成功";
    private static final String ERROR = "保存失败";

    @Autowired
    private PaymentinfoMapper paymentinfoMapper;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IPaymentPushClient iPaymentPushClient;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Override
    public List<Paymentinfo> getInfoForBalance(IRequest request, Paymentinfo dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return paymentinfoMapper.getInfoForBalance(dto);
    }

    /**
     * 获取payMode=UNION settleTime=null的数据
     *
     * @param paymentinfo
     * @return
     */
    @Override
    public List<Paymentinfo> selectPaymentsByModeWithSettleTimeIsNull(Paymentinfo paymentinfo) {
        return paymentinfoMapper.selectPaymentsByModeWithSettleTimeIsNull(paymentinfo);
    }

    @Override
    public List<Paymentinfo> getPaymentinfoByOrderId(Long orderId) {
        List<Paymentinfo> paymentinfos = paymentinfoMapper.getPaymentinfoByOrderId(orderId);
        if (CollectionUtils.isEmpty(paymentinfos))
            return null;
        List<Paymentinfo> result = new ArrayList<>();
        for (Paymentinfo paymentinfo : paymentinfos) {
            if (paymentinfo.getPayAmount() == null)
                continue;
            if (paymentinfo.getRefundAmount() == null)
                paymentinfo.setCanRefundAmount(paymentinfo.getPayAmount().subtract(new BigDecimal(0)));
            else
                paymentinfo.setCanRefundAmount(paymentinfo.getPayAmount().subtract(paymentinfo.getRefundAmount()));
            result.add(paymentinfo);
        }
        return result;
    }

    public Integer updatePaymentIfoByNumberCode(Paymentinfo paymentinfo) {
        return paymentinfoMapper.updateByPrimaryKeySelective(paymentinfo);
    }

    @Override
    public void postToRetail(List<Paymentinfo> paymentinfos) throws WSCallException {
        if (CollectionUtils.isEmpty(paymentinfos)) {
            return;
        }

        PaymentRequestBody paymentRequestBody = new PaymentRequestBody();
        List<PaymentRequestItem> itemList = new ArrayList<>();
        for (Paymentinfo paymentinfo : paymentinfos) {
            PaymentRequestItem item = new PaymentRequestItem();
            item.setZSHDDH(paymentinfo.getNumberCode());
            item.setZCKH(paymentinfo.getPaymentinfoId());
            item.setZYWLX(FINANCIAL_TYPE_PAY);
            item.setZZFLX(paymentinfo.getPayMode());
            item.setBUKRS(BUKRS);
            item.setZKUNNR(ZKUNNR);
            if (paymentinfo.getOrderId() != null) {
                Order order = new Order();
                order.setOrderId(paymentinfo.getOrderId());
                order = iOrderService.selectByPrimaryKey(RequestHelper.newEmptyRequest(), order);
                if (order != null) {
                    item.setEXORD(order.getEscOrderCode());
                }
            }
            item.setZJYRQ(DateUtil.getdate(paymentinfo.getPayTime(), DATE_FORMAT));
            item.setZJYSJ(DateUtil.getdate(paymentinfo.getPayTime(), TIME_FORMAT));
            item.setZZJE(paymentinfo.getPayAmount().doubleValue());
            item.setZY06(paymentinfo.getOutTradeNo());
            item.setZY09("X");
            item.setZY01(paymentinfo.getOrderType());
            itemList.add(item);
        }
        paymentRequestBody.setItemList(itemList);
        PaymentResponseBody responseBody = iPaymentPushClient.paymentPush(paymentRequestBody);
        if (SUCCESS.equals(responseBody.getMsg())) {
            for (Paymentinfo paymentinfo : paymentinfos) {
                paymentinfo.setSyncflag(Constants.YES);
                paymentinfoMapper.updateByPrimaryKeySelective(paymentinfo);
            }
            iLogManagerService.logTrace(this.getClass(), "支付信息推送retail", null, SUCCESS);
        } else {
            iLogManagerService.logError(this.getClass(), "支付信息推送retail", null, ERROR);
        }
    }
}