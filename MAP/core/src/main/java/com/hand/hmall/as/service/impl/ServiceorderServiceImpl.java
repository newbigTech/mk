package com.hand.hmall.as.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.as.dto.*;
import com.hand.hmall.as.mapper.AsCompensateEntryMapper;
import com.hand.hmall.as.mapper.ServiceorderMapper;
import com.hand.hmall.as.service.*;
import com.hand.hmall.common.service.ISequenceGenerateService;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.Consignment;
import com.hand.hmall.om.dto.Order;
import com.hand.hmall.om.dto.OrderEntry;
import com.hand.hmall.om.dto.Paymentinfo;
import com.hand.hmall.om.mapper.OrderEntryMapper;
import com.hand.hmall.om.mapper.OrderMapper;
import com.hand.hmall.om.service.IConsignmentService;
import com.hand.hmall.om.service.IPaymentinfoService;
import com.hand.hmall.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xuxiaoxue
 * @version 0.1
 * @name ServiceorderServiceImpl
 * @description 服务单列表界面ServiceImpl实现类
 * @date 2017/7/17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceorderServiceImpl extends BaseServiceImpl<Serviceorder> implements IServiceorderService {
    @Autowired
    private ServiceorderMapper serviceorderMapper;
    @Autowired
    private IServiceorderEntryService serviceorderEntryService;
    @Autowired
    private ISequenceGenerateService sequenceGenerateService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderEntryMapper orderEntryMapper;
    @Autowired
    private IAsCompensateService asCompensateService;
    @Autowired
    private AsCompensateEntryMapper asCompensateEntryMapper;
    @Autowired
    private IAsReturnService asReturnService;
    @Autowired
    private IAsRefundService asRefundService;
    @Autowired
    private IRefundEntryService asRefundEntryService;
    @Autowired
    private IPaymentinfoService paymentinfoService;
    @Autowired
    private IConsignmentService consignmentService;

    @Override
    public List<Serviceorder> queryServiceOrderList(Serviceorder dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return serviceorderMapper.queryServiceOrderList(dto);
    }


    @Override
    public List<Serviceorder> queryServiceOrderListWithoutPage(Serviceorder dto) {
        return serviceorderMapper.queryServiceOrderList(dto);
    }

    /**
     * 根据订单ID查询服务单信息
     *
     * @param dto
     * @return
     */
    @Override
    public List<Serviceorder> queryServiceOrderListBySaleCode(Serviceorder dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return serviceorderMapper.queryServiceOrderListBySaleCode(dto);
    }


    /**
     * 查询服务单详 细信息
     *
     * @param dto
     * @return
     */
    @Override
    public List<Serviceorder> selectServiceOrderByCode(Serviceorder dto) {
        return serviceorderMapper.selectServiceOrderByCode(dto);
    }

    /**
     * 根据订单ID查询用户信息
     *
     * @param dto
     * @return
     */
    @Override
    public List<Serviceorder> selectUserInfoByOrderId(Serviceorder dto) {
        return serviceorderMapper.selectUserInfoByOrderId(dto);
    }

    @Override
    public Serviceorder saveCategory(IRequest iRequest, List<Serviceorder> dto, String serviceOrderId) {
        Serviceorder serviceorder = null;
        if (dto.get(0).getCode() != null) {
            List<Serviceorder> serviceorderList = this.selectServiceOrderByCode(dto.get(0));
            //更新
            if (serviceorderList != null && serviceorderList.size() == 1) {
                if (Constants.FINI.equals(dto.get(0).getStatus()) && dto.get(0).getFinishTime() == null) {
                    dto.get(0).setFinishTime(new Date());
                }
                serviceorder = this.updateByPrimaryKeySelective(iRequest, dto.get(0));
            }
        }//新增
        else {
            dto.get(0).setCode(sequenceGenerateService.getNextAsCode());
            dto.get(0).setCreationDate(new Date());
            serviceorder = this.insertSelective(iRequest, dto.get(0));
            serviceOrderId = serviceorder.getServiceOrderId() + "";
        }
        //保存订单行
        if (serviceOrderId != null) {
            if (dto.get(0).getServiceOrderEntries() != null && dto.get(0).getServiceOrderEntries().size() > 0) {
                saveServiceOrderEntry(dto.get(0).getServiceOrderEntries(), Long.parseLong(serviceOrderId), iRequest);
            }
        }
        return serviceorder;
    }

    /**
     * 根据服务单ID查询多媒体中的图片信息
     *
     * @param dto
     * @return
     */
    @Override
    public List<Serviceorder> queryMediaByServiceOrderId(Serviceorder dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return serviceorderMapper.queryMediaByServiceOrderId(dto);
    }

    @Override
    public void setAssiging(List<Long> soIds_, Long employeeId) {
        serviceorderMapper.setAssiging(soIds_, employeeId);
    }

    /**
     * 查询调用保价接口需要的订单数据
     *
     * @param orderId
     * @return
     */
    @Override
    public Order insuredOrder(Long orderId) {
        //查询订单信息
        Order order = new Order();
        order.setOrderId(orderId);
        order = orderMapper.selectByPrimaryKey(order);
        if (order != null) {
            //查询订单行信息
            OrderEntry orderEntry = new OrderEntry();
            orderEntry.setOrderId(orderId);
            List<OrderEntry> orderEntryList = orderEntryMapper.selectInsuredOrderEntry(orderId);
            if (CollectionUtils.isNotEmpty(orderEntryList)) {
                //将已退货数据存入保价接口需要的字段
                for (OrderEntry obj : orderEntryList) {
                    //促销保价字段名跟数据库不一致 需要转换
                    obj.setVproduct(obj.getVproductCode() != null ? obj.getVproductCode() : "");
                    obj.setReturnQuantity(obj.getReturnedQuantity());
                }
                order.setOrderEntryList(orderEntryList);
            }
            return order;
        } else {
            return null;
        }
    }

    /**
     * 查询订单是否可以保价
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean checkInsuredOrder(Long orderId) {
        List<Order> orderList = orderMapper.checkInsuredOrder(orderId);
        if (CollectionUtils.isNotEmpty(orderList)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否需要生成销售赔付单
     *
     * @param order
     * @return
     */
    @Override
    public boolean checkInsuredOrderExtraReduce(Order order) {
        if (order.getExtraReduce() != null && order.getExtraReduce() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成销售赔付单 更新订单 订单行信息.
     *
     * @param order
     * @param serviceOrderId
     */
    @Override
    public ResponseData insertAsCompensate(Order order, Long serviceOrderId, IRequest request) {
        ResponseData responseData = new ResponseData();
        //服务单信息
        Serviceorder serviceorder = new Serviceorder();
        serviceorder.setServiceOrderId(serviceOrderId);
        serviceorder = serviceorderMapper.selectByPrimaryKey(serviceorder);
        //用户信息
        Serviceorder userInfo = new Serviceorder();
        userInfo.setOrderId(order.getOrderId());
        List<Serviceorder> userInfoList = serviceorderMapper.selectUserInfoByOrderId(userInfo);
        if (CollectionUtils.isNotEmpty(userInfoList)) {
            userInfo = userInfoList.get(0);
        }
        try {
            //生成销售赔付单
            if (serviceorder != null && userInfo != null) {
                //赔付单头
                AsCompensate asCompensate = new AsCompensate();
                asCompensate.setCode(sequenceGenerateService.getNextAsCompensateCode());
                asCompensate.setStatus("FINI");
                asCompensate.setOrderId(order.getOrderId());
                asCompensate.setServiceId(serviceOrderId);
                asCompensate.setName(serviceorder.getName());
                asCompensate.setMobile(serviceorder.getMobile());
                asCompensate.setAddress(serviceorder.getAddress());
                asCompensate.setCompensateFee(BigDecimal.valueOf(order.getExtraReduce()));
                asCompensate = asCompensateService.insertSelective(request, asCompensate);
                //赔付单行
                AsCompensateEntry asCompensateEntry = new AsCompensateEntry();
                asCompensateEntry.setCompensateId(asCompensate.getCompensateId());
                asCompensateEntry.setCompensateType("PRICERELOAD");
                asCompensateEntry.setQuantity(1d);
                asCompensateEntry.setLineNumber("10");
                asCompensateEntry.setUnit("PC");
                asCompensateEntry.setUnitFee(BigDecimal.valueOf(order.getExtraReduce()));
                asCompensateEntry.setTotalFee(BigDecimal.valueOf(order.getExtraReduce()));
                asCompensateEntryMapper.insertSelective(asCompensateEntry);

                //更新订单信息
                order.setSyncflag("N");
                order.setSyncZmall("N");
                orderMapper.updateByPrimaryKeySelective(order);
                //更新订单行信息
                if (CollectionUtils.isNotEmpty(order.getOrderEntryList())) {
                    for (OrderEntry orderEntry : order.getOrderEntryList()) {
                        orderEntryMapper.updateByPrimaryKeySelective(orderEntry);
                    }
                }
                //更新发货单表
                List<Order> orderList = orderMapper.selectInsuredOrder(order.getOrderId());
                if (CollectionUtils.isNotEmpty(orderList)) {
                    for (Order obj : orderList) {
                        Consignment consignment = new Consignment();
                        consignment.setConsignmentId(obj.getConsignmentId());
                        consignment.setSyncflag("N");
                        consignment.setSyncZmall("N");
                        consignmentService.updateByPrimaryKeySelective(request, consignment);
                    }
                }

                //计算赔付费用 大于0即生成对应退款单
                AsReturn asReturn = new AsReturn();
                asReturn.setOrderId(order.getOrderId());
                asReturn.setWebsiteId("1");
                List<AsReturn> asReturnList = asReturnService.selectUserInfoByOrderId(asReturn);
                if (asReturnList != null && asReturnList.size() > 0) {
                    double referenceFee = asReturnList.get(0).getReferenceFee();
                    if (referenceFee > 0) {
                        //退款单头
                        AsRefund asRefund = new AsRefund();
                        asRefund.setCode(sequenceGenerateService.getNextRefundOrderCode());
                        asRefund.setOrderId(order.getOrderId());
                        asRefund.setServiceOrderId(serviceOrderId);
                        asRefund.setStatus("NEW");
                        asRefund.setName(serviceorder.getName());
                        asRefund.setMobile(serviceorder.getMobile());
                        asRefund.setAddress(serviceorder.getAddress());
                        asRefund.setReferenceSum(BigDecimal.valueOf(order.getExtraReduce()));
                        asRefund = asRefundService.insertSelective(request, asRefund);
                        List<AsRefund> asRefundList = new ArrayList<>();
                        asRefundList.add(asRefund);
                        responseData.setResp(asRefundList);
                        //查询支付信息
                        List<Paymentinfo> paymentinfoList = paymentinfoService.getPaymentinfoByOrderId(order.getOrderId());
                        if (CollectionUtils.isNotEmpty(paymentinfoList)) {
                            for (Paymentinfo paymentinfo : paymentinfoList) {
                                //退款单行
                                RefundEntry refundEntry = new RefundEntry();
                                refundEntry.setAsRefundId(asRefund.getAsRefundId());
                                refundEntry.setPayMode(paymentinfo.getPayMode());
                                refundEntry.setAccount(paymentinfo.getAccount());
                                refundEntry.setName(paymentinfo.getName());
                                refundEntry.setPaymentinfoId(paymentinfo.getPaymentinfoId());
                                asRefundEntryService.insertSelective(request, refundEntry);
                            }
                        }
                    }
                }
                responseData.setSuccess(true);
            }
        } catch (Exception e) {
            responseData.setSuccess(false);
            throw new RuntimeException();
        }
        return responseData;
    }

    /**
     * 保存服务单行
     *
     * @param list
     * @param iRequest
     */
    private void saveServiceOrderEntry(List<ServiceorderEntry> list, Long serviceOrderId, IRequest iRequest) {
        ServiceorderEntry serviceorderEntry;
        for (int i = 0; i < list.size(); i++) {
            //判断服务单行数量是否大于订单行数量 如果大于将数量改为订单行数量
            OrderEntry orderEntry = new OrderEntry();
            orderEntry.setOrderEntryId(list.get(i).getOrderEntryId());
            orderEntry = orderEntryMapper.selectByPrimaryKey(orderEntry);
            if (list.get(i).getQuantity() > orderEntry.getQuantity()) {
                list.get(i).setQuantity((long) orderEntry.getQuantity());
            }
            serviceorderEntry = serviceorderEntryService.selectByPrimaryKey(iRequest, list.get(i));
            if (serviceorderEntry != null) {
                serviceorderEntryService.updateByPrimaryKeySelective(iRequest, list.get(i));
            } else {
                list.get(i).setServiceOrderId(serviceOrderId);
                serviceorderEntryService.insertSelective(iRequest, list.get(i));
            }
        }
    }
}