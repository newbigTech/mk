package com.hand.hmall.om.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.aspect.OperationHistoryCache;
import com.hand.hmall.fnd.dto.FndBusinessLog;
import com.hand.hmall.fnd.service.IFndBusinessLogService;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.om.dto.*;
import com.hand.hmall.om.mapper.OrderBkMapper;
import com.hand.hmall.om.service.IConsignmentBkService;
import com.hand.hmall.om.service.IOrderBkService;
import com.hand.hmall.om.service.IOrderEntryBkService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 马君
 * @version 0.1
 * @name OrderBkServiceImpl
 * @description 订单备份Service接口实现类
 * @date 2017/8/4 11:38
 */
@Service
@Transactional
public class OrderBkServiceImpl extends BaseServiceImpl<OrderBk> implements IOrderBkService {

    private static final Logger logger = LoggerFactory.getLogger(OrderBkServiceImpl.class);

    @Autowired
    private IOrderEntryBkService iOrderEntryBkService;

    @Autowired
    private IConsignmentBkService iConsignmentBkService;

    @Autowired
    private OrderBkMapper mapper;

    @Autowired
    private IFndBusinessLogService iFndBusinessLogService;

    @Autowired
    private ILogManagerService iLogManagerService;

    /**
     * 保存操作记录
     * @param operationUser 操作人
     * @param operationType 操作类型
     * @param version 备份版本
     * @param orderId 订单id
     */
    @Override
    public void saveBusinessLog(Long operationUser, String operationType, Long version, Long orderId) {
        FndBusinessLog log = new FndBusinessLog();
        log.setOrderId(orderId);
        log.setOperationType(operationType);
        log.setCurrentVersion(version + 1);
        log.setLastVersion(version);
        log.setOperationUser(operationUser);
        log.setOperationContent(OperationHistoryCache.getAndClear());
        log.setOperationTime(new Date());
        iFndBusinessLogService.insertSelective(RequestHelper.newEmptyRequest(), log);
    }

    /**
     * 订单列表查询
     *
     * @param page
     * @param pageSize
     * @param code            订单编号
     * @param userId          用户ID
     * @param receiverMobile  收货人手机
     * @param startTime       下单开始日期
     * @param endTime         下单结束日期
     * @param strOrderStatus  订单状态
     * @param strDistribution 配送方式
     * @param vproduct        变式物料号
     * @param productId       商品编码
     * @return
     */
    @Override
    public List<OrderBk> selectOrderBkList(int page, int pageSize, String code, String escOrderCode, String userId, String locked, String receiverMobile, String startTime,

                                           String endTime, String[] strOrderStatus, String[] strDistribution, String[] strOrderTypes, String vproduct, String productId,String payRate,String pinCode) {
        PageHelper.startPage(page, pageSize);
        List<OrderBk> list = mapper.selectOrderBkList(code, escOrderCode, userId, locked, receiverMobile, startTime, endTime, strOrderStatus, strDistribution, strOrderTypes, vproduct, productId, payRate, pinCode);
        for (OrderBk a : list) {
            String orderStatus = a.getOrderStatus();
            if (orderStatus != null) {
                switch (orderStatus) {
                    case "TRADE_CLOSED_BY_UNIQLO":
                        a.setOrderStatus("交易取消");
                        break;
                    case "TRADE_CLOSED":
                        a.setOrderStatus("交易关闭");
                        break;
                    case "TRADE_FINISHED":
                        a.setOrderStatus("交易关闭");
                        break;
                    case "WAIT_SELLER_CONFIRM_GOODS":
                        a.setOrderStatus("待卖家收货");
                        break;
                    case "WAIT_BUYER_RETURN_GOODS":
                        a.setOrderStatus("待买家寄回商品");
                        break;
                    case "WAIT_BUYER_TAKE_GOODS":
                        a.setOrderStatus("待提货");
                        break;
                    case "WAIT_SELLER_SEND_GOODS__PICKUP":
                        a.setOrderStatus("已付款");
                        break;
                    case "WAIT_BUYER_CONFIRM_GOODS":
                        a.setOrderStatus("已发货");
                        break;
                    case "SELLER_CONSIGNED_PART":
                        a.setOrderStatus("部分发货");
                        break;
                    case "WAIT_SELLER_SEND_GOODS__EXPRESS":
                        a.setOrderStatus("待发货");
                        break;
                    case "WAIT_BUYER_PAY":
                        a.setOrderStatus("待付款");
                        break;
                    default:
                        a.setOrderStatus(" ");
                }
            }
        }
        return list;
    }

    /**
     * 订单详情界面查询订单
     *
     * @param dto 订单实体类
     * @return
     */
    @Override
    public List<OrderBk> selectInfoByOrderBkId(OrderBk dto) {
        return mapper.selectInfoByOrderBkId(dto);
    }

    /**
     * 计算订单备份的下一个版本
     * @param code 订单版本
     * @return
     */
    @Override
    public Long selectNextVersion(String code) {
        return mapper.selectNextVersion(code);
    }

    /**
     * 保存订单备份和日志
     * @param operationUser 操作人
     * @param operationType 操作类型
     */
    @Override
    public void saveOrderBkAndLog(Long operationUser, String operationType) {
        OrderBk orderBk = this.saveBkFromCache();
        Long orderId = OperationHistoryCache.getOrder().getOrderId();
        this.saveBusinessLog(operationUser, operationType, orderBk.getVersion(), orderId);
        iLogManagerService.logTrace(this.getClass(), "订单备份和操作日志", orderId,
                "订单[" + orderId + "]进行" + operationType + "操作, 操作人[" + operationUser + "]");
    }

    /**
     * 从线程cache中保存备份
     * @return
     */
    @Override
    public OrderBk saveBkFromCache() {

        IRequest iRequest = RequestHelper.newEmptyRequest();

        Order sourceOrder = OperationHistoryCache.getOrder();
        List<Consignment> consignmentList = OperationHistoryCache.getConsignments();
        List<OrderEntry> orderEntryList = OperationHistoryCache.getOrderEntries();

        OrderBk orderBk = new OrderBk();
        BeanUtils.copyProperties(sourceOrder, orderBk);
        orderBk.setVersion(this.selectNextVersion(sourceOrder.getCode()));
        this.insertSelective(iRequest, orderBk);

        Map<Long, Long> consignmentMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(consignmentList)) {
            for (Consignment consignment : consignmentList) {
                ConsignmentBk consignmentBk = new ConsignmentBk();
                BeanUtils.copyProperties(consignment, consignmentBk);
                consignmentBk.setConsignmentId(null);
                consignmentBk.setOrderId(orderBk.getOrderId());
                iConsignmentBkService.insertSelective(iRequest, consignmentBk);
                consignmentMap.put(consignment.getConsignmentId(), consignmentBk.getConsignmentId());
            }
        }

        if (CollectionUtils.isNotEmpty(orderEntryList)) {
            for (OrderEntry orderEntry : orderEntryList) {
                OrderEntryBk orderEntryBk = new OrderEntryBk();
                BeanUtils.copyProperties(orderEntry, orderEntryBk);
                orderEntryBk.setOrderEntryId(null);
                orderEntryBk.setOrderId(orderBk.getOrderId());
                if (orderEntry.getConsignmentId() != null) {
                    orderEntryBk.setConsignmentId(consignmentMap.get(orderEntry.getConsignmentId()));
                }
                iOrderEntryBkService.insertSelective(iRequest, orderEntryBk);
            }
        }
        return orderBk;
    }

    /**
     * 根据订单code和目录版本查询对应订单备份
     * @param dto       订单备份实体类
     * @return
     */
    @Override
    public OrderBk queryByVersionAndCode(OrderBk dto) {
        Long orderId = mapper.queryByVersionAndCode(dto);
        OrderBk ob = new OrderBk();
        ob.setOrderId(orderId);
        return ob;
    }
}
